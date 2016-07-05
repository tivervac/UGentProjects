package modules.processing.parsing;

import akka.event.LoggingAdapter;
import models.document.HTMLDocument;
import utils.ProcessingException;

/**
 * @author Feliciaan De Palmenaer
 */
public class TocParserSelector {

    private final LoggingAdapter logger;

    public TocParserSelector(LoggingAdapter logger) {
        this.logger = logger;
    }

    public TocParser<HTMLDocument> getParser(HTMLDocument document) throws ProcessingException {
        int num_links = document.getHtmlDocument().select("a").size();
        int num_dl = document.getHtmlDocument().select("dt").size();
        int num_li = document.getHtmlDocument().select("li").size();

        String prefix = "Selected parser: ";
        if (num_dl > num_links * 2 / 3) {
            logger.info(prefix + "Description List Parser");
            return new DescriptionListParser();
        } else if (num_li > num_links * 2 / 3) {
            logger.info(prefix + "List Parser");
            return new ListParser();
        } else { // raw list parser
            logger.info(prefix + "Raw List Parser");
            return new RawListParser();
        }
    }
}
