package utils.concurrency;

import models.ParserTask;
import models.document.Document;
import modules.input.DocumentGrabber;
import utils.ProcessingException;
import utils.observer.StateChangedEvent;
import utils.observer.StateListener;

import java.util.Queue;

/**
 * @author Titouan Vervack
 */
public class DownloadThread<T extends Document> extends Thread {

    private final Queue<String> urls;
    private final DocumentGrabber<T> grabber;
    private final Queue<T> documents;
    private final StateListener listener;
    private final String prefix;

    public DownloadThread(String prefix, DocumentGrabber<T> grabber, Queue<String> urls, Queue<T> documents, StateListener listener) {
        this.grabber = grabber;
        this.urls = urls;
        this.documents = documents;
        this.listener = listener;
        this.prefix = prefix;
    }

    @Override
    public void run() {
        while (!urls.isEmpty()) {
            String url = urls.poll();
            T d;
            try {
                d = grabber.grab(prefix, url);
            } catch (ProcessingException e) {
                listener.stateChanged(new StateChangedEvent(ParserTask.Status.ERROR, e.getMessage()));
                continue;
            }
            documents.add(d);
        }
    }
}
