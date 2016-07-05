package modules.processing.ner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import models.document.Document;
import models.document.HTMLDocument;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import play.libs.Json;
import utils.ProcessingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Uses NERD API.
 * See http://nerd.eurecom.fr/documentation
 *
 * @author Wouter Pinnoo
 */
public class NerdApiEntityRecognizer implements EntityRecogniser {
    private static final String NERD_API_KEY = "10pcbn3v760qp6oh3pj7l5ruoij4hu2m";
    private static final int MAX_NUMBER_OF_TAGS = 5;

    private String doPOST(String url, List<NameValuePair> headers, List<NameValuePair> urlParameters) throws ProcessingException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);

        for (NameValuePair header : headers) {
            post.addHeader(header.getName(), header.getValue());
        }

        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            HttpResponse response = client.execute(post);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            System.err.println("Error in NERD::doPOST");
            throw new ProcessingException(e.getMessage());
        }
    }

    private String doGET(String url, List<NameValuePair> headers, List<NameValuePair> urlParameters) throws ProcessingException {
        HttpClient client = HttpClientBuilder.create().build();

        try {
            URIBuilder builder = new URIBuilder(url);
            builder.addParameters(urlParameters);

            HttpGet request = new HttpGet(builder.build());

            for (NameValuePair header : headers) {
                request.addHeader(header.getName(), header.getValue());
            }

            HttpResponse response = client.execute(request);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException | URISyntaxException e) {
            System.err.println("Error in NERD::doGET");
            throw new ProcessingException(e.getMessage());
        }
    }

    /**
     * Submit (text of) document to NERD API.
     *
     * @param text text to be analysed
     * @return documentId, to be used in the {@link #runExtractor(int, ExtractorType) runExtractor} method
     */
    private int submitText(String text) throws ProcessingException {
        List<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("key", NERD_API_KEY));
        urlParameters.add(new BasicNameValuePair("uri", text));

        String result = doPOST("http://nerd.eurecom.fr/api/document", headers, urlParameters);
        try {
            JsonNode jsonNode = Json.parse(result);
            if (jsonNode.hasNonNull("idDocument")) {
                return jsonNode.get("idDocument").asInt();
            }
        } catch (Exception ex) {
            System.err.println("Error in NERD::submitText");
            throw new ProcessingException(ex.getLocalizedMessage());
        }


        return -1;
    }

    /**
     * Tell NERD API to start the extractor
     *
     * @param documentId    documentId retrieved from {@link #submitText(String) submitText} method
     * @param extractorType preferred extractor to use
     * @return annotationId, to be used in the {@link #getEntities(int) getEntities} method
     */
    private int runExtractor(int documentId, ExtractorType extractorType) throws ProcessingException {
        List<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("key", NERD_API_KEY));
        urlParameters.add(new BasicNameValuePair("extractor", extractorType.getApiName()));
        urlParameters.add(new BasicNameValuePair("idDocument", String.valueOf(documentId)));

        String result = doPOST("http://nerd.eurecom.fr/api/annotation", headers, urlParameters);
        JsonNode jsonNode = Json.parse(result);
        if (jsonNode.hasNonNull("idAnnotation")) {
            return jsonNode.get("idAnnotation").asInt();
        }

        return -1;
    }

    /**
     * Retrieve annotations from the NERD API
     *
     * @param annotationId annotationId retrieved from the {@link #runExtractor(int, ExtractorType) runExtractor} method
     * @return list of Entity objects, containing a label + confidence and relevance level
     */
    private List<Entity> getEntities(int annotationId) throws ProcessingException {
        List<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Accept", "application/json"));

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("key", NERD_API_KEY));
        urlParameters.add(new BasicNameValuePair("idAnnotation", String.valueOf(annotationId)));

        String result = doGET("http://nerd.eurecom.fr/api/entity", headers, urlParameters);
        JsonNode jsonNode = Json.parse(result);

        List<Entity> sortedEntities = new ArrayList<>();
        if (jsonNode.isArray()) {
            Map<String, Entity> entities = new HashMap<>();
            for (final JsonNode entity : jsonNode) {
                String label = entity.get("label").asText();
                Entity e;
                if (entities.containsKey(label)) {
                    e = entities.get(label);
                    e.numberOfOccurrences++;
                } else {
                    e = new Entity();
                    e.confidence = entity.get("confidence").asDouble(0);
                    e.relevance = entity.get("relevance").asDouble(0);
                    e.label = label;
                    e.numberOfOccurrences = 1;
                }
                entities.put(label, e);
            }

            sortedEntities.addAll(entities.values());
            Collections.sort(sortedEntities, (o1, o2) -> {
                double resultComparison = o2.numberOfOccurrences - o1.numberOfOccurrences;
                if (resultComparison == 0.0) {
                    resultComparison = o2.relevance - o1.relevance;
                    if (resultComparison == 0.0) {
                        resultComparison = o2.confidence - o1.confidence;
                    }
                }
                return resultComparison == 0.0 ? 0 : (resultComparison > 0.0 ? 1 : -1);
            });

        }

        return sortedEntities;
    }

    @Override
    public List<String> recognise(Document document) throws ProcessingException {
        // TODO error handling after execution of each method
        int documentId = submitText(document.getText());
        return recognise(document, documentId);
    }

    @Override
    public List<String> recognise(String url) throws ProcessingException {
        // TODO error handling after execution of each method
        int documentId = submitText(url);
        Document document = new HTMLDocument(null, url);
        return recognise(document, documentId);
    }

    private List<String> recognise(Document document, int documentId) throws ProcessingException {
        int annotationId = runExtractor(documentId, ExtractorType.COMBINED);
        List<Entity> entities = getEntities(annotationId);

        int numberOfTags = Math.min(MAX_NUMBER_OF_TAGS, entities.size());
        List<String> tags = new ArrayList<>(numberOfTags);

        for (int i = 0; i < numberOfTags; i++) {
            tags.add(entities.get(i).label);
        }

        return tags;
    }

    private enum ExtractorType {
        COMBINED("combined"),
        ALCHEMY("alchemyapi"),
        DANDELION("dandelionapi"),
        DBPEDIA_SPOTLIGHT("dbspotlight"),
        LUPEDIA("lupedia"),
        OPENCALAIS("opencalais"),
        SAPLO("saplo"),
        SEMITAGS("semitags"),
        TEXT_RAZOR("textrazor"),
        THD("thd"),
        WIKIMETA("wikimeta"),
        YAHOO_CONTENT_ANALYSIS("yahoo"),
        ZEMANTA("zemanta");

        protected String apiName;

        ExtractorType(String apiName) {
            this.apiName = apiName;
        }

        protected String getApiName() {
            return apiName;
        }
    }

    private class Entity {
        String label;
        double confidence;
        double relevance;
        int numberOfOccurrences;
    }
}
