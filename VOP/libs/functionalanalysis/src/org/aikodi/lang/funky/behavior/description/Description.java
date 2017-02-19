package org.aikodi.lang.funky.behavior.description;

import org.aikodi.chameleon.core.element.Element;

import com.google.java.contract.Ensures;

/**
 * A description of something. In its most basic form, the description
 * consists of a text. More structure can be added, though, by
 * introducing macro's, including cross-references to concepts
 * in the domain model, use cases,....
 * 
 * Given that the description is in natural language, no interesting
 * behavior can be placed here other than designating the {@link #toString()}
 * method to returning a useful text for the description. For proper
 * visualization and editing, you will probably have to use a pattern
 * such as a Chain of Responsibility to do a case analysis.
 * 
 * @author Marko van Dooren
 */
public interface Description extends Element {

   /**
    * @return A string that describes behavior.
    */
   @Ensures({"result != null"})
   String toString();

    /**
     * Returns a readable representation of this usecase
     * @return
     */
    String text();
}
