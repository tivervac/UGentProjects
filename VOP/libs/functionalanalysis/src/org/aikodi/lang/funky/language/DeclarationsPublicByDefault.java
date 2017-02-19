package org.aikodi.lang.funky.language;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.property.PropertyRule;

import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;

import com.google.java.contract.Ensures;

/**
 * A rule that makes all declarations public by default unless
 * otherwise specified.
 * 
 * @author Marko van Dooren
 */
public class DeclarationsPublicByDefault extends PropertyRule<Declaration> {

   public DeclarationsPublicByDefault() {
      super(Declaration.class);
   }

   /**
    * {@inheritDoc}
    * 
    * Only property {@link Funky#PUBLIC} is in the resulting set of properties.
    */
   @Ensures({"result.contains(language(Funky.class).PUBLIC)",
             "result.size() == 1"})
   @Override
   public PropertySet<Element, ChameleonProperty> suggestedProperties(Declaration declaration) {
      return createSet(language(Funky.class).PUBLIC);
   }
   
}