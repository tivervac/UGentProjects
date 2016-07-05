package models.document;

import models.Module;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents some kind of Document, it could be an HTML, JSON, parsed,... document
 *
 * @author Titouan Vervack
 */
public interface Document {
    String getText();

    default String getTitle() {
        return "AMMA Test Course";
    }

    String getUrl();

    default List<String> newUrls() {
        return new ArrayList<>();
    }

    default boolean hasModules() {
        return !getModules().isEmpty();
    }

    default List<Module> getModules() {
        return new ArrayList<>();
    }

    default void addModule(Module module) {
    }
}
