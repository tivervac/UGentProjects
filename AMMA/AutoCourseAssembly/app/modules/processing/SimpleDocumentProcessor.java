package modules.processing;

import akka.event.LoggingAdapter;
import models.Module;
import models.ParserTask.Status;
import models.Section;
import models.document.Document;
import modules.output.DocumentTranslator;
import modules.processing.ner.EntityRecogniser;
import modules.processing.ner.NerdApiEntityRecognizer;
import modules.processing.parsing.TocParser;
import utils.ProcessingException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Titouan Vervack
 */
public class SimpleDocumentProcessor extends DocumentProcessor {

    private LoggingAdapter logger;

    @Override
    public <S extends Document, T extends Document> List<T> process(S document, TocParser<S> parser, DocumentTranslator<T> translator, LoggingAdapter logger) throws InterruptedException {
        this.logger = logger;

        List<T> results = new ArrayList<>();

        stateChanged(Status.PARSING);
        Document parsed;
        try {
            parsed = parser.parse(document, listeners.get(0));
        } catch (ProcessingException e) {
            stateChanged(Status.ERROR, e.getMessage());
            return results;
        }

        stateChanged(Status.WRITING_WITHOUT_TAGS);
        results.add(translator.writeDocument(parsed));

        return results;
    }

    @Override
    public <T extends Document> List<T> recognize(Document document, DocumentTranslator<T> translator) throws InterruptedException {
        stateChanged(Status.RECOGNISING);

        List<T> results = new ArrayList<>();

        final List<Module> modules = new ArrayList<>();
        modules.addAll(document.getModules());

        int i = 0;
        for (Module module : modules) {
            if (PRINT) {
                logger.info("Recognising module " + (++i) + "/" + modules.size() + ": " + module.getTitle());
            }

            int j = 0;
            for (Section section : module.getSections()) {
                String url = section.getActivity().getUrl();

                if (PRINT) {
                    logger.info("\tRecognising section " + (++j) + "/" + module.getSections().size() + ": " + section.getTitle());
                }

                EntityRecogniser recogniser = new NerdApiEntityRecognizer();
                List<String> tags = new ArrayList<>();
                try {
                    tags = recogniser.recognise(url);
                } catch (ProcessingException e) {
                    logger.error(String.format("%s cannot be recognised, and has the following error: %s", url, e.getLocalizedMessage()));
                    stateChanged(Status.RECOGNISING_ERROR, e.getLocalizedMessage());
                }
                section.addTags(tags);
                module.addTags(tags);
            }
        }

        stateChanged(Status.WRITING_WITH_TAGS);
        results.add(translator.writeDocument(document));

        return results;
    }
}
