package modules.processing.ner;

import models.document.Document;
import utils.ProcessingException;

import java.util.List;

/**
 * This class recognizes entities in a Document
 *
 * @author Titouan Vervack
 */
public interface EntityRecogniser {

    List<String> recognise(Document document) throws ProcessingException;

    List<String> recognise(String url) throws ProcessingException;
}
