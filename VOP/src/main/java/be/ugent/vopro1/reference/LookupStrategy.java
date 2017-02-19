package be.ugent.vopro1.reference;

import org.aikodi.chameleon.core.declaration.Declaration;

import java.util.List;
import java.util.Map;

/**
 * Provides a way to retrieve funky references to and from an object
 */
public interface LookupStrategy {

    /**
     * Retrieves the ancestors of an object as specified in the configuration
     *
     * @param config Configuration for the ancestor lookup
     * @return Map of typeNames to lists of Declarations of that type that are an ancestor
     */
    Map<String, List<? extends Declaration>> findAncestors(LookupConfig config);

    /**
     * Retrieves the descendants of an object as specified in the configuration
     *
     * @param config Configuration for the descendant lookup
     * @return Map of typeNames to lists of Declarations of that type that are a descendant
     */
    Map<String, List<? extends Declaration>> findDescendants(LookupConfig config);
}
