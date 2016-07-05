package modules.processing.parsing;

import models.Activity;
import models.Module;
import models.Section;
import models.document.Document;
import models.document.HTMLDocument;
import org.jsoup.nodes.Element;
import utils.ProcessingException;

import java.util.Optional;

/**
 * This class parses a ToC in the form of a list
 *
 * @author Stefaan Vermassen
 */
public class ListParser extends TocParser<HTMLDocument> {
    @Override
    public Document parse(HTMLDocument document) throws ProcessingException {
        //Get all the lists that are not nested inside another list
        //Find the list with most childnodes, this will probably be the TOC
        Optional<Element> toc = document.getHtmlDocument()
                .select("ul").stream().filter(l -> !l.parent().tag().equals("li"))
                .sorted((e1, e2) -> e1.childNodes().size() > e2.childNodes().size() ? -1 : 1).findFirst();
        if (toc.isPresent()) {
            document = parseDoc(document, toc.get());

            if(toc.get().parent() != null){
                if (!toc.get().parent().select(":root > a[href]").isEmpty()) {
                    document.setTitle(toc.get().parent().select(":root > a[href]").get(0).ownText());
                }
            }
        }
        return document;
    }

    public HTMLDocument parseDoc(HTMLDocument document, Element toc) throws ProcessingException {
        for (Element e : toc.children()) {
            String chapterName = "";
            //If a href tag as direct child of this li element exists
            if (!e.select(":root > a[href]").isEmpty()) {
                chapterName = e.select(":root > a[href]").get(0).ownText();
            } else {
                chapterName = e.text();
            }
            document = parseMod(document, e, chapterName);

            if(document.getModules().isEmpty()){
                if (!e.select(":root > a").isEmpty() && !e.select(":root > a").get(0).attr("href").isEmpty()) {
                    Module mod = new Module();
                    Section section = new Section();
                    Element linkElement = e.select(":root > a").get(0);
                    mod.setTitle(linkElement.ownText());
                    section.setTitle(linkElement.ownText());
                    setActivity(linkElement, section, document);
                    mod.addSection(section);
                }

            }

            /*if (module.getSections().isEmpty()) {
                //No sublevels found, create new activity with this element's content

                //If there is a link assigned to this listelement
                if (!e.select(":root > a").isEmpty() && !e.select(":root > a").get(0).attr("href").isEmpty()) {
                    Section section = new Section();
                    Element linkElement = e.select(":root > a").get(0);
                    section.setTitle(linkElement.ownText());
                    setActivity(linkElement, section, document);
                    module.addSection(section);
                }

            }*/


        }
        return document;
    }

    public HTMLDocument parseMod(HTMLDocument document, Element listElement, String chapterName) throws ProcessingException {
        Module parentmod = new Module();
        parentmod.setChapter(chapterName);
        if (!listElement.select(":root > a[href]").isEmpty()) {
            parentmod.setTitle(listElement.select(":root > a[href]").get(0).ownText());
        } else {
            parentmod.setTitle(listElement.text());
        }
        if (!listElement.select(":root > a").isEmpty() && !listElement.select(":root > a").get(0).attr("href").isEmpty()) {
            Section parentSection = new Section();
            Element parentLinkElement = listElement.select(":root > a").get(0);
            parentSection.setTitle(parentLinkElement.ownText());
            setActivity(parentLinkElement, parentSection, document);
            parentmod.addSection(parentSection);
            document.addModule(parentmod);
        }

        for (Element chapter : listElement.select(":root > ul")) {
            //All nested ul lists for listElement
            for (Element c : chapter.children()) {
                //Children from the nested ul list, Mobile Setup and Computer Setup in the example
                Module mod = new Module();
                mod.setChapter(chapterName);
                if (!c.select(":root > a[href]").isEmpty()) {
                    mod.setTitle(c.select(":root > a[href]").get(0).ownText());
                } else {
                    mod.setTitle(c.text());
                }
                mod = parseSec(document, mod, c);

                if (mod.getSections().isEmpty()) {
                    //No sublevels found, create new activity with this element's content

                    //If there is a link assigned to this listelement
                    if (!c.select(":root > a").isEmpty() && !c.select(":root > a").get(0).attr("href").isEmpty()) {
                        Section section = new Section();
                        Element linkElement = c.select(":root > a").get(0);
                        section.setTitle(linkElement.ownText());
                        setActivity(linkElement, section, document);
                        mod.addSection(section);
                    }

                }
                document.addModule(mod);

            }

        }
        return document;
    }

    private void setActivity(Element linkElement, Section s, HTMLDocument d) throws ProcessingException {
        Activity a = new Activity(listener);
        String url = linkElement.attr("href").toLowerCase().startsWith("http")? linkElement.attr("href") : d.getPrefix() + linkElement.attr("href");
        a.setTitle(linkElement.ownText());
        a.setUrl(url);
        s.setActivity(a);
    }

    public Module parseSec(HTMLDocument document, Module m, Element listElement) throws ProcessingException {
        int weight=0;
        Section parentSec = new Section();
        parentSec.setWeight(weight++);
        if (!listElement.select(":root > a[href]").isEmpty()) {
            parentSec.setTitle(listElement.select(":root > a[href]").get(0).ownText());
        } else {
            parentSec.setTitle(listElement.text());
        }
        if (!listElement.select(":root > a").isEmpty() && !listElement.select(":root > a").get(0).attr("href").isEmpty()) {

            Element parentLinkElement = listElement.select(":root > a").get(0);
            setActivity(parentLinkElement, parentSec, document);
            m.addSection(parentSec);

        }
        for (Element section : listElement.select(":root > ul")) {
            for (Element c : section.children()) {
                if (!c.select(":root > a[href]").isEmpty() && !c.select(":root > a").get(0).attr("href").isEmpty()) {
                    Section s = new Section();
                    s.setWeight(weight++);
                    if (!c.select(":root > a[href]").isEmpty()) {
                        s.setTitle(c.select(":root > a[href]").get(0).ownText());
                    } else {
                        s.setTitle(c.text());
                    }
                    s = parseAct(document, s, c);
                    if (s.getActivity() == null) {
                        //No sublevels found, create new activity with this element's content
                        //If there is a link assigned to this listelement
                        if (!c.select(":root > a").isEmpty() && !c.select(":root > a").get(0).attr("href").isEmpty()) {
                            setActivity(c.select(":root > a").get(0), s, document);
                        }

                    }
                    m.addSection(s);
                }
            }
        }
        return m;
    }

    public Section parseAct(HTMLDocument document, Section sec, Element listElement) throws ProcessingException {
        for (Element section : listElement.select(":root > ul")) {
            for (Element c : section.children()) {
                if (!c.select(":root > a[href]").isEmpty() && !c.select(":root > a").get(0).attr("href").isEmpty()) {
                    setActivity(c.select(":root > a").get(0), sec, document);
                }
            }
        }
        return sec;
    }


}
