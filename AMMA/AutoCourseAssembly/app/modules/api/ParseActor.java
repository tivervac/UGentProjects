package modules.api;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import models.ParserTask;
import models.ParserTask.Status;
import models.document.HTMLDocument;
import models.document.JsonDocument;
import modules.input.DocumentGrabber;
import modules.input.HTMLGrabber;
import modules.output.JsonTranslator;
import modules.processing.DocumentProcessor;
import modules.processing.SimpleDocumentProcessor;
import modules.processing.parsing.TocParser;
import modules.processing.parsing.TocParserSelector;
import utils.ProcessingException;
import utils.concurrency.DownloadThread;
import utils.observer.StateChangedEvent;
import utils.observer.StateListener;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Copyright(C) 2015 Ghent University
 *
 * @author Stefaan Vermassen (Stefaan.Vermassen@UGent.be)
 * @since 29/02/2016.
 */
public class ParseActor extends AbstractActor implements StateListener {

    private static final boolean FASTMODE = false;
    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    private ParserTask task;

    public ParseActor() {
        task = null;
        receive(ReceiveBuilder.
                match(APINotifications.ParseRequestNotification.class, this::handleParseRequest).build());
    }

    public void handleParseRequest(APINotifications.ParseRequestNotification s) {
        task = ParserTask.FIND.where().eq("id", s.getTaskId()).findUnique();

        DocumentProcessor processor = new SimpleDocumentProcessor();
        // Add a listener that will listen for state changes
        processor.addListener(this);

        try {
            stateChanged(Status.DOWNLOADING);
            logger.info("New URL request: " + s.getUrl());
            DocumentGrabber<HTMLDocument> grabber = new HTMLGrabber();
            HTMLDocument document = getDocument(s.getPrefix(), s.getUrl(), grabber, this);
            if (document == null) {
                stateChanged(Status.ERROR, "Document is not available");
                return;
            }
            TocParserSelector tps = new TocParserSelector(logger);
            TocParser<HTMLDocument> parser = tps.getParser(document);

            List<JsonDocument> results = processor.process(document, parser, new JsonTranslator(), logger);
            task.setResult(results.get(0).getText());
            stateChanged(Status.DONE_WITHOUT_TAGS);

            if (!FASTMODE) {
                results = processor.recognize(document, new JsonTranslator());
                task.setResult(results.get(0).getText());
                stateChanged(Status.DONE_WITH_TAGS);
            }
        } catch (InterruptedException | ProcessingException e) {
            stateChanged(Status.ERROR, e.getMessage());
        }
    }

    private HTMLDocument getDocument(String prefix, String url, DocumentGrabber<HTMLDocument> grabber, StateListener task) throws InterruptedException {
        Queue<String> urls = new ConcurrentLinkedQueue<>();
        Queue<HTMLDocument> documents = new ConcurrentLinkedQueue<>();
        urls.add(url);

        Thread t = new DownloadThread<>(prefix, grabber, urls, documents, task);
        t.start();
        t.join();

        return documents.poll();
    }

    private void stateChanged(Status status) {
        stateChanged(new StateChangedEvent(status));
    }

    private void stateChanged(Status status, String message) {
        stateChanged(new StateChangedEvent(status, message));
    }

    @Override
    public void stateChanged(StateChangedEvent e) {
        if (task == null) {
            return;
        }

        task.setStatus(e.status());
        logger.info("Status changed to: " + e.status());
        if (!e.message().isEmpty()) {
            task.setResult(e.message());
        }

        if (e.status().equals(Status.ERROR)) {
            logger.error(e.message());
        }
        task.update();
    }
}