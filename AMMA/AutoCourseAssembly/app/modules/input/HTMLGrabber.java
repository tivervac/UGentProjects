package modules.input;

import models.document.HTMLDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import utils.ProcessingException;

/**
 * This class is responsible for grabbing HTML documents from the net
 *
 * @author Titouan Vervack
 */
public class HTMLGrabber implements DocumentGrabber<HTMLDocument> {

    @Override
    public HTMLDocument grab(String prefix, String url) throws ProcessingException {
        HTMLDocument htmlDoc;
        try {
            Document doc = Jsoup.connect(url).get();
            if (prefix.isEmpty()) {
                htmlDoc = new HTMLDocument(doc, url);
            } else {
                htmlDoc = new HTMLDocument(doc, url, prefix);
            }
        } catch (Exception ex) {
            System.err.println("Exception in HTMLGrabber");
            throw new ProcessingException(ex.getMessage());
        }

        return htmlDoc;
    }
}
