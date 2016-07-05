package utils.concurrency;

import models.document.Document;
import modules.processing.parsing.DocumentParser;
import utils.ProcessingException;
import utils.observer.StateListener;

import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * @author Titouan Vervack
 */
public class ParseThread<T extends Document> extends Thread {
    private final DocumentParser<T> parser;
    private final Queue<String> urls;
    private final Queue<T> documents;
    private final List<Document> results;
    private final Set<String> done;
    private final StateListener listener;

    public ParseThread(DocumentParser<T> parser, Queue<String> urls, Queue<T> documents, List<Document> results, Set<String> done, StateListener listener) {
        this.urls = urls;
        this.documents = documents;
        this.parser = parser;
        this.results = results;
        this.done = done;
        this.listener = listener;
    }

    @Override
    public void run() {
        while (!urls.isEmpty() || !documents.isEmpty()) {
            if (documents.isEmpty()) {
                continue;
            }
            T source = documents.peek();
            try {
                Document d = parser.parse(source, listener);
                d.newUrls().stream().filter(url -> !done.contains(url)).forEach(url -> {
                    done.add(url);
                    urls.add(url);
                });

                // Make sure we do not accidentally empty the queues by removing after adding
                documents.poll();
                results.add(d);
            } catch (ProcessingException e) {
                System.err.println("***ERROR***\n\tError while parsing " + source.getUrl());
            }
        }
    }
}
