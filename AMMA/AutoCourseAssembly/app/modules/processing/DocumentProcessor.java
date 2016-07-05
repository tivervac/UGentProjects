package modules.processing;

import akka.event.LoggingAdapter;
import models.Module;
import models.ParserTask.Status;
import models.document.Document;
import modules.input.DocumentGrabber;
import modules.output.DocumentTranslator;
import modules.processing.parsing.TocParser;
import utils.observer.StateChangedEvent;
import utils.observer.StateListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the main class that handles the processing of a Document.
 * It will make the calls to all the needed manipulations and will output the Document.
 *
 * @author Titouan Vervack
 */
public abstract class DocumentProcessor {

    protected static final boolean PRINT = true;

    protected List<StateListener> listeners = new ArrayList<>();

    public abstract <S extends Document, T extends Document> List<T> process(S document, TocParser<S> parser, DocumentTranslator<T> translator, LoggingAdapter adapter) throws InterruptedException;

    public abstract <T extends Document> List<T> recognize(Document document, DocumentTranslator<T> translator) throws InterruptedException;

    public void addListener(StateListener e) {
        listeners.add(e);
    }

    public boolean removeListener(StateListener e) {
        return listeners.remove(e);
    }

    protected void stateChanged(Status status) {
        stateChanged(status, "");
    }

    protected void stateChanged(Status status, String message) {
        for (StateListener listener : listeners) {
            listener.stateChanged(new StateChangedEvent(status, message));
        }
    }
}
