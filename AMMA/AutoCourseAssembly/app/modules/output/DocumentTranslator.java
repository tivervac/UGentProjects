package modules.output;

import models.document.Document;

/**
 * Translates a Document to some other format
 *
 * @author Titouan Vervack
 */
public interface DocumentTranslator<T> {

    T writeDocument(Document document);
}
