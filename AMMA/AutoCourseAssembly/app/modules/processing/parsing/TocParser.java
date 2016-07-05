package modules.processing.parsing;

import models.document.Document;
import utils.ProcessingException;
import utils.observer.StateListener;

/**
 * This class parses a ToC of an HTMLDocument
 *
 * @author Titouan Vervack
 */
public abstract class TocParser<T extends Document> implements DocumentParser<T> {

    protected StateListener listener;

    public Document parse(T document, StateListener listener) throws ProcessingException {
        this.listener = listener;
        return parse(document);
    }

    protected abstract Document parse(T document) throws ProcessingException;
}
