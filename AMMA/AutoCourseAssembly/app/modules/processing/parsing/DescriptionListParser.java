package modules.processing.parsing;

import models.Activity;
import models.Module;
import models.Section;
import models.document.Document;
import models.document.HTMLDocument;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.ProcessingException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class parses a ToC in the form of a description list
 *
 * @author Titouan Vervack
 */
public class DescriptionListParser extends TocParser<HTMLDocument> {

    private int weight = 0;

    @Override
    public Document parse(HTMLDocument document) throws ProcessingException {
        // Should only be one top level dl
        Element element = document.getHtmlDocument().select("html > body dl").first();
        int depth = determineDepth(element);
        if (depth < 2) {
            String error = "Not enough levels detected in the DescriptionList.";
            System.err.println("***ERROR***\n\t" + error);
            throw new ProcessingException(error);
        }

        parseElement(document, element);

        return document;
    }

    private int determineDepth(Element element) {
        return determineDepth(element, 0);
    }

    private int determineDepth(Element element, int level) {
        int result = level;
        Elements dt = element.select("dt");
        if (!dt.isEmpty()) {
            result = ++level;
            Elements dl = element.select("dd > dl");
            for (Element d : dl) {
                int temp = determineDepth(d, level);
                if (temp > result) {
                    result = temp;
                }
            }
        }

        return result;
    }

    private Activity parseActivity(HTMLDocument d, Element href) throws ProcessingException {
        Activity activity = new Activity(listener);
        activity.setTitle(href.ownText());
        String url = href.attr("href").toLowerCase().startsWith("http")? href.attr("href") : d.getPrefix() + href.attr("href");

        activity.setUrl(url);

        return activity;
    }

    private List<Section> parseSections(HTMLDocument d, Element element) throws ProcessingException {
        List<Section> sections = new ArrayList<>();
        Elements children = getDirectChildren(element, "dt");
        for (Element child : children) {
            Element href = child.select("a[href]").first();
            sections.add(createSection(d, href));
        }

        return sections;
    }

    private Section createSection(HTMLDocument d, Element href) throws ProcessingException {
        Section section = new Section();
        section.setTitle(href.ownText());
        section.setWeight(weight++);
        section.setActivity(parseActivity(d, href));

        return section;
    }

    private void parseElement(HTMLDocument d, Element element) throws ProcessingException {
        Elements children = getDirectChildren(element, "dt");

        for (Element child : children) {
            Element next = child.nextElementSibling();
            int depth = -1;
            if (next != null && next.tagName().equals("dd")) {
                Element e = new Element(element.tag(), element.baseUri(), element.attributes());
                e.appendChild(child);
                e.appendChild(next);
                depth = determineDepth(e);
            }
            // Skip single elements
            if (depth < 2) {
                continue;
            }

            Element href = child.select("a[href]").first();
            parseModule(d, child, depth >= 3 ? href.ownText() : null);
        }

    }

    private void parseModule(HTMLDocument d, Element element, String chapter) throws ProcessingException {
        Module module = new Module();
        Element href = element.select("a[href]").first();
        module.setTitle(href.ownText());

        module.addSection(createSection(d, href));

        Element sibling = element.nextElementSibling();
        if (sibling != null && sibling.tagName().equals("dd") && sibling.children().first().tagName().equals("dl")) {
            parseSections(d, sibling.children().first()).forEach(module::addSection);
        }
        if (chapter != null && !chapter.isEmpty()) {
            module.setChapter(chapter);
        }

        d.addModule(module);
    }

    private Elements getDirectChildren(Element element, String tag) {
        Elements children = element.children();
        Iterator<Element> iterator = children.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().tagName().equals(tag)) {
                iterator.remove();
            }
        }

        return children;
    }
}
