package models.document;

import models.Module;
import utils.ProcessingException;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Titouan Vervack
 */
public class HTMLDocument implements Document {
    private org.jsoup.nodes.Document htmlDocument;
    private String title;
    private String url;
    private String prefix;
    private List<Module> modules;

    public HTMLDocument(org.jsoup.nodes.Document htmlDocument, String url) {
        this.htmlDocument = htmlDocument;
        this.url = url;
        this.modules = new ArrayList<>();
        title = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        prefix = url.substring(0, url.lastIndexOf('/'));
    }

    public HTMLDocument(org.jsoup.nodes.Document htmlDocument, String url, String prefix) {
        this.htmlDocument = htmlDocument;
        this.url = url;
        this.modules = new ArrayList<>();
        title = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        this.prefix = prefix;
    }

    @Override
    public String getText() {
        return htmlDocument.body().text();
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public org.jsoup.nodes.Document getHtmlDocument() {
        return htmlDocument;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public List<Module> getModules() {
        return modules;
    }

    @Override
    public void addModule(Module module) {
        modules.add(module);
    }
}
