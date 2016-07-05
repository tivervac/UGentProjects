package be.itech.dao;

import be.itech.model.Confirmation;
import be.itech.model.Event;
import be.itech.model.Message;
import be.itech.model.Person;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Titouan Vervack & Caroline De Brouwer
 */
public class DAO {

    private static final String API_URL = "http://events.restdesc.org";
    private static final String JSON_HEADER = "application/json";
    public static final String ERROR_TAG = "API Error";

    private DAO() {
    }

    /*
     * HELPER METHODS
     */
    private static JSONObject searchQuery(String url) throws DataAccessException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        request.setHeader("Accept", "application/json");
        try {
            HttpResponse response = client.execute(request);
            return getBody(response);
        } catch (IOException e) {
            request.abort();
            throw new DataAccessException(e.getMessage());
        } catch (IllegalStateException e) {
            request.abort();
            throw new DataAccessException(e.getMessage());
        } catch (JSONException e) {
            request.abort();
            throw new DataAccessException(e.getMessage());
        }
    }

    public static boolean deleteQuery(String url) throws DataAccessException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpDelete request = new HttpDelete(url);
        request.setHeader("Accept", "application/json");
        try {
            HttpResponse response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            return statusCode == HTTP_NO_CONTENT;
        } catch (IOException e) {
            request.abort();
            throw new DataAccessException(e.getMessage());
        } catch (IllegalStateException e) {
            request.abort();
            throw new DataAccessException(e.getMessage());
        }
    }

    private static void patchQuery(String url, JSONObject json) throws DataAccessException {
        pQuery(url, json, true);
    }

    private static JSONObject postQuery(String url, JSONObject json) throws DataAccessException {
        return pQuery(url, json, false);
    }

    private static JSONObject pQuery(String url, JSONObject json, boolean patch) throws DataAccessException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpEntityEnclosingRequestBase request;
        if (patch) {
            request = new HttpPatch(url);
        } else {
            request = new HttpPost(url);
        }
        request.setHeader(new BasicHeader("Accept", JSON_HEADER));
        request.setHeader(new BasicHeader(HTTP.CONTENT_TYPE, JSON_HEADER));

        try {
            StringEntity se = new StringEntity(json.toString());
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, JSON_HEADER));
            request.setEntity(se);
            HttpResponse response = client.execute(request);
            if (response.getEntity() == null) {
                return null;
            } else {
                return getBody(response);
            }
        } catch (UnsupportedEncodingException e) {
            request.abort();
            throw new DataAccessException(e.getMessage());
        } catch (IOException e) {
            request.abort();
            throw new DataAccessException(e.getMessage());
        } catch (JSONException e) {
            request.abort();
            throw new DataAccessException(e.getMessage());
        }
    }

    private static JSONObject getBody(HttpResponse response) throws IOException, JSONException {
        InputStream bodyStream = response.getEntity().getContent();
        BufferedReader bodyReader = new BufferedReader(new InputStreamReader(bodyStream));
        StringBuilder body = new StringBuilder();
        String chunk;
        while ((chunk = bodyReader.readLine()) != null) {
            body.append(chunk);
        }
        return new JSONObject(body.toString());
    }

    private static Date parseDate(String start) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        return ("null".equals(start)) ? null : formatter.parse(start);
    }

    private static Date parseBirthDate(String birthDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        return ("null".equals(birthDate)) ? null : formatter.parse(birthDate);
    }

    private static Event parseEvent(JSONObject jsonEvent) throws DataAccessException {
        try {
            if (jsonEvent == null) {
                return null;
            }
            String eventTitle = jsonEvent.getString("title");
            String eventDescription = jsonEvent.getString("description");
            Date eventStart = parseDate(jsonEvent.getString("start"));
            Date eventEnd = parseDate(jsonEvent.getString("end"));
            Date eventCreated = parseDate(jsonEvent.getString("created_at"));
            Date eventUpdated = parseDate(jsonEvent.getString("updated_at"));
            String eventUrl = jsonEvent.getString("url");
            String eventIndex = jsonEvent.getString("index");

            JSONObject confirmations = jsonEvent.getJSONObject("confirmations");
            JSONArray people = confirmations.getJSONArray("list");

            List<Confirmation> confirmationsList = new ArrayList<Confirmation>();
            for (int i = 0; i < people.length(); i++) {
                JSONObject jsonConfirmation = people.getJSONObject(i);
                boolean personGoing = jsonConfirmation.getBoolean("going");
                JSONObject jsonPerson = jsonConfirmation.getJSONObject("person");
                String personName = jsonPerson.getString("name");
                String personUrl = jsonPerson.getString("url");
                Person p = new Person(personName, personUrl);
                String confirmUrl = jsonConfirmation.getString("url");
                confirmationsList.add(new Confirmation(personGoing, p, confirmUrl));
            }

            String confirmationsUrl = confirmations.getString("url");

            JSONObject jsonMessages = jsonEvent.getJSONObject("messages");
            String messagesUrl = jsonMessages.getString("url");

            JSONArray messages = jsonMessages.getJSONArray("list");
            List<Message> messagesList = new ArrayList<Message>();
            for (int i = 0; i < messages.length(); i++) {
                messagesList.add(parseMessage(messages.getJSONObject(i)));
            }

            return new Event(eventTitle, eventDescription, eventStart, eventEnd, eventCreated,
                    eventUpdated, eventUrl, eventIndex, confirmationsUrl, messagesUrl, confirmationsList, messagesList);
        } catch (JSONException ex) {
            throw new DataAccessException(ex.getMessage());
        } catch (ParseException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private static Person parsePerson(JSONObject jsonPerson) throws DataAccessException {
        try {
            if (jsonPerson == null) {
                return null;
            }
            String personName = jsonPerson.getString("name");
            String personEmail = jsonPerson.getString("email");
            Date personBirth = parseBirthDate(jsonPerson.getString("birth_date"));
            Date personCreated = parseDate(jsonPerson.getString("created_at"));
            Date personUpdated = parseDate(jsonPerson.getString("updated_at"));
            String personUrl = jsonPerson.getString("url");
            String peopleIndex = jsonPerson.getString("index");

            JSONArray events = jsonPerson.getJSONArray("events");
            List<Event> personEventsList = new ArrayList<Event>();
            for (int i = 0; i < events.length(); i++) {
                JSONObject event = events.getJSONObject(i);
                String eventTitle = event.getString("title");
                String eventUrl = event.getString("url");
                personEventsList.add(new Event(eventTitle, eventUrl));
            }
            return new Person(personName, personEmail, personBirth, personCreated, personUpdated, personUrl, peopleIndex, personEventsList);
        } catch (JSONException ex) {
            throw new DataAccessException(ex.getMessage());
        } catch (ParseException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private static Message parseMessage(JSONObject jsonMessage) throws JSONException, ParseException {
        String messageText = jsonMessage.getString("text");
        Date created_at = parseDate(jsonMessage.getString("created_at"));
        JSONObject jsonPerson = jsonMessage.getJSONObject("person");
        String personName = jsonPerson.getString("name");
        String personUrl = jsonPerson.getString("url");
        Person p = new Person(personName, personUrl);
        String messageUrl = jsonMessage.getString("url");
        return new Message(messageText, created_at, p, messageUrl);
    }

    private static JSONObject buildPerson(Person p) throws DataAccessException {
        try {
            JSONObject json = new JSONObject();
            JSONObject jsonPerson = new JSONObject();

            if (!p.getName().isEmpty()) {
                jsonPerson.put("name", p.getName());
            }
            if (!p.getEmail().isEmpty()) {
                jsonPerson.put("email", p.getEmail());
            }
            Calendar c = Calendar.getInstance(TimeZone.getDefault());
            c.setTime(p.getBirth());
            jsonPerson.put("birth_date", c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH));
            json.put("person", jsonPerson);

            return json;
        } catch (JSONException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private static JSONObject buildEvent(Event e) throws DataAccessException {
        try {
            JSONObject json = new JSONObject();
            JSONObject jsonEvent = new JSONObject();

            if (!e.getTitle().isEmpty()) {
                jsonEvent.put("title", e.getTitle());
            }
            if (!e.getTitle().isEmpty()) {
                jsonEvent.put("description", e.getDescription());
            }
            Calendar s = Calendar.getInstance(TimeZone.getDefault());
            s.setTime(e.getStart());
            Calendar end = Calendar.getInstance(TimeZone.getDefault());
            end.setTime(e.getEnd());
            jsonEvent.put("start", s.get(Calendar.YEAR) + "-" + (s.get(Calendar.MONTH) + 1) + "-" + s.get(Calendar.DAY_OF_MONTH) + "T"
                    + s.get(Calendar.HOUR_OF_DAY) + ":" + s.get(Calendar.MINUTE) + ":00.000Z");
            jsonEvent.put("end", end.get(Calendar.YEAR) + "-" + (end.get(Calendar.MONTH) + 1) + "-" + end.get(Calendar.DAY_OF_MONTH) + "T"
                    + end.get(Calendar.HOUR_OF_DAY) + ":" + end.get(Calendar.MINUTE) + ":00.000Z");
            json.put("event", jsonEvent);

            return json;
        } catch (JSONException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private static JSONObject buildMessage(Message m) throws DataAccessException {
        try {
            JSONObject json = new JSONObject();
            JSONObject jsonMessage = new JSONObject();
            JSONObject person = new JSONObject();
            person.put("url", m.getPerson().getUrl());
            person.put("name", m.getPerson().getName());
            jsonMessage.put("person", person);
            if (!m.getText().isEmpty()) {
                jsonMessage.put("text", m.getText());
            }
            json.put("message", jsonMessage);

            return json;
        } catch (JSONException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public static JSONObject editParticipantStatus(String confirmationsUrl, Person p, boolean going) throws DataAccessException {
        try {
            JSONObject json = new JSONObject();
            JSONObject confirmation = new JSONObject();
            JSONObject person = new JSONObject();
            person.put("url", p.getUrl());
            person.put("name", p.getName());
            confirmation.put("person", person);
            confirmation.put("going", String.valueOf(going));
            json.put("confirmation", confirmation);
            return postQuery(confirmationsUrl, json);
        } catch (JSONException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    /*
     * GENERAL SEARCH QUERIES
     */
    public static List<String> getHomePage() throws DataAccessException {
        try {
            JSONObject bodyObject = searchQuery(API_URL);
            List<String> homepage = new ArrayList<String>();
            homepage.add(bodyObject.getString("title"));
            homepage.add(bodyObject.getString("events"));
            homepage.add(bodyObject.getString("people"));
            return homepage;
        } catch (JSONException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    public static List<Event> getEvents(String url) throws DataAccessException {
        try {
            JSONObject bodyObject = searchQuery(url);
            JSONArray events = bodyObject.getJSONArray("events");

            List<Event> eventsList = new ArrayList<Event>();
            for (int i = 0; i < events.length(); i++) {
                JSONObject event = events.getJSONObject(i);
                String eventTitle = event.getString("title");
                Date eventStart = parseDate(event.getString("start"));
                String eventUrl = event.getString("url");
                eventsList.add(new Event(eventTitle, eventStart, eventUrl));
            }

            return eventsList;
        } catch (JSONException e) {
            throw new DataAccessException(e.getMessage());
        } catch (ParseException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public static List<Person> getPeople(String url) throws DataAccessException {
        try {
            JSONObject bodyObject = searchQuery(url);
            JSONArray people = bodyObject.getJSONArray("people");

            List<Person> peopleList = new ArrayList<Person>();
            for (int i = 0; i < people.length(); i++) {
                JSONObject person = people.getJSONObject(i);
                String personName = person.getString("name");
                String personUrl = person.getString("url");
                peopleList.add(new Person(personName, personUrl));
            }

            return peopleList;
        } catch (JSONException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /*
     * SPECIFIC SEARCH QUERIES
     */
    public static Person getPerson(String url) throws DataAccessException {
        return parsePerson(searchQuery(url));
    }

    public static Event getEvent(String url) throws DataAccessException {
        return parseEvent(searchQuery(url));
    }

    /*
     * ADDITIONS
     */
    public static Person addPerson(Person p) throws DataAccessException {
        return parsePerson(postQuery(p.getPeopleIndex(), buildPerson(p)));
    }

    public static Event addEvent(Event e) throws DataAccessException {
        return parseEvent(postQuery(e.getIndex(), buildEvent(e)));
    }

    public static Event addMessage(String messagesUrl, Message m) throws DataAccessException {
        return parseEvent(postQuery(messagesUrl, buildMessage(m)));
    }

    /*
     * PATCHES
     */
    public static void patchPerson(Person p) throws DataAccessException {
        patchQuery(p.getUrl(), buildPerson(p));
    }

    public static void patchEvent(Event e) throws DataAccessException {
        patchQuery(e.getUrl(), buildEvent(e));
    }

    public static void patchMessage(Message m) throws DataAccessException {
        patchQuery(m.getUrl(), buildMessage(m));
    }
}
