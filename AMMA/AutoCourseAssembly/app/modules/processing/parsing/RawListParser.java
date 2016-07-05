package modules.processing.parsing;

import models.Activity;
import models.Module;
import models.Section;
import models.document.Document;
import models.document.HTMLDocument;
import modules.input.HTMLGrabber;
import org.jsoup.nodes.Element;
import utils.ProcessingException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * This class parses a ToC in the form of a list of links
 *
 * @author Wouter Pinnoo
 */
public class RawListParser extends TocParser<HTMLDocument> {
    @Override
    public Document parse(HTMLDocument document) throws ProcessingException {
        String baseUrl = document.getUrl();
        for (Element e : document.getHtmlDocument().select("html > body > a")) {
            Item item = new Item();
            item.e = e;

            int depth = parseRecursively(document, item, baseUrl, 1);
            if (depth >= 3) {
                for (Item child : item.children) {
                    Module m = parseModule(document, child);
                    m.setChapter(item.e.ownText());
                    document.addModule(m);
                }
            } else if (depth == 2) {
                Module module = parseModule(document, item);
                document.addModule(module);
            } else {
                Module module = new Module();
                module.setTitle(item.e.ownText());
                Section section = parseSection(document, item, 0);
                module.addSection(section);
                document.addModule(module);
            }
        }
        return document;
    }

    private int parseRecursively(HTMLDocument document, Item item, String baseUrl, int currentDepth) throws ProcessingException {
        String link = formatUriString(baseUrl, item.e.attr("href"));
        HTMLGrabber grabber = new HTMLGrabber();
        HTMLDocument grabbedDocument = grabber.grab(document.getPrefix(), link);

        if (currentDepth == 1
                || grabbedDocument.getHtmlDocument().select("p").isEmpty()) {

            final int[] maxDepth = {currentDepth};

            for (Element childElement : grabbedDocument.getHtmlDocument().select("html > body > a")) {
                Item child = new Item();
                String newBaseUrl = grabbedDocument.getUrl();
                String newLink = formatUriString(newBaseUrl, childElement.attr("href"));

                child.link = newLink;
                child.e = childElement;
                item.children.add(child);

                int newDepth = parseRecursively(document, child, grabbedDocument.getUrl(), currentDepth + 1);
                maxDepth[0] = Math.max(maxDepth[0], newDepth);
            }

            return maxDepth[0];
        } else {
            return currentDepth;
        }
    }

    private class Item {
        List<Item> children = new ArrayList<>();
        Element e;
        String link;
    }

    private Module parseModule(HTMLDocument d, Item item) throws ProcessingException {
        Module module = new Module();
        module.setTitle(item.e.ownText());

        if (!item.children.isEmpty())
        {
            int sectionWeight = 0;
            for (Item child : item.children) {
                Section section = parseSection(d, child, sectionWeight++);
                module.addSection(section);
            }
        } else
        {
            Section section = parseSection(d, item, 0);
            module.addSection(section);
        }

        return module;
    }

    private Section parseSection(HTMLDocument d, Item item, int sectionWeight) throws ProcessingException {
        Section section = new Section();
        section.setTitle(item.e.ownText());
        section.setWeight(sectionWeight);

        Activity activity = new Activity(listener);
        String url = item.link.toLowerCase().startsWith("http") ? item.link : d.getPrefix() + item.link;
        activity.setUrl(url);
        activity.setTitle(item.e.ownText());

        section.setActivity(activity);

        return section;
    }

    private String formatUriString(String baseUrl, String link) throws ProcessingException {
        if (link.isEmpty()) {
            return null;
        }

        if (!link.startsWith("http")) {

            try {
                if (baseUrl.endsWith("#")) {
                    baseUrl = baseUrl.substring(0, link.indexOf("#"));
                }

                if (baseUrl.endsWith(".html") || baseUrl.endsWith(".htm")) {
                    URI baseUrlObj = new URI(baseUrl);
                    String path = baseUrlObj.getPath();
                    int lastPathLength = path.length() - path.lastIndexOf('/');
                    baseUrl = baseUrl.substring(0, baseUrl.length() - lastPathLength);
                }

                if (!baseUrl.endsWith("/")) {
                    baseUrl += "/";
                }

                URI uriLink = new URI(baseUrl).resolve(link);
                link = uriLink.toString();
            } catch (Exception exception) {
                String error = "Malformed link: " + link + "\n\t" + exception.getMessage();
                System.err.println("***ERROR***\n\t" + error);
                throw new ProcessingException(error);
            }
        }

        if (link.contains("#")) {
            link = link.substring(0, link.indexOf("#"));
        }
        if (!link.toLowerCase().startsWith("http") && !link.toLowerCase().startsWith("https")) {
            return null;
        }
        return link;
    }

}
