package modules.input;

import models.document.Document;
import utils.ProcessingException;

/**
 * Grabs some kind of Document from the net
 *
 * @author Titouan Vervack
 */
public interface DocumentGrabber<T extends Document> {

    T grab(String prefix, String url) throws ProcessingException;
}
