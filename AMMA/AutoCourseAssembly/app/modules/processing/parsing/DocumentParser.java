package modules.processing.parsing;

import models.document.Document;
import utils.ProcessingException;
import utils.observer.StateListener;

/**
 * This class parses a certain Document
 *
 * @author Titouan Vervack
 */
public interface DocumentParser<T> {

    Document parse(T document, StateListener listener) throws ProcessingException;
}
