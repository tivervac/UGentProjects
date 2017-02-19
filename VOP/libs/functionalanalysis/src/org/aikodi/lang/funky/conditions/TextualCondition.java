package org.aikodi.lang.funky.conditions;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.util.association.Single;
import org.aikodi.lang.funky.behavior.description.Description;

import com.google.java.contract.Ensures;

/**
 * A condition that is expressed in natural language or a mix of natural
 * language and structured elements.
 * 
 * @author Marko van Dooren
 */
public class TextualCondition extends ElementImpl implements Condition {
   
   private Single<Description> _description = new Single<>(this,true);
   
   /**
    * Create a new textual condition from the given description.
    * 
    * @param description
    */
   public TextualCondition(Description description) {
      setDescription(description);
   }

   /**
    * Set the description for this step.
    * 
    * @param description A description of the behavior of this step.
    */
   @Ensures({"description() == description"})
   public void setDescription(Description description) {
      set(_description, description);
   }

   /**
    * @return The description of the behavior of this step.
    */
   public Description description() {
      return _description.getOtherEnd();
   }

   @Override
   protected Element cloneSelf() {
      return new TextualCondition(null);
   }


}
