package org.aikodi.lang.funky.builder;

import java.util.function.Consumer;

import org.aikodi.lang.funky.behavior.description.TextualDescription;
import org.aikodi.lang.funky.conditions.Condition;
import org.aikodi.lang.funky.conditions.TextualCondition;

import com.google.java.contract.Ensures;

/**
 * A builder that creates basic conditions.
 * 
 * @author Marko van Dooren
 *
 * @param <P> The type of the parent builder.
 */
public class ConditionBuilder<P> extends Builder<P,Condition> {

   /**
    * Create a new condition builder with the given parent builder and consumer.
    * The objects that are created will be passed to the consumer.
    * 
    * Both parameters can be null.
    * 
    * @param parent The parent builder.
    * @param consumer A consumer that will process the created objects.
    */
   @Ensures({"parent() == parent",
             "consumer() == consumer"})
   public ConditionBuilder(P parent, Consumer<Condition> consumer) {
      super(parent,consumer);
   }
   
   /**
    * Create a new textual condition from a text and pass it to the
    * consumer.
    * 
    * @param text A text that describes the condition
    * @return the parent builder.
    */
   public P text(String text) {
      consumer().accept(new TextualCondition(new TextualDescription(text)));
      return parent();
   }

}
