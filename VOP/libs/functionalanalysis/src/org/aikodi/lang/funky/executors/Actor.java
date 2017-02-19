package org.aikodi.lang.funky.executors;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

/**
 * A class of actors.
 * 
 * @author Marko van Dooren
 */
public class Actor extends ExecutingEntity {

   /**
    * Create a new actor with the given name.
    * 
    * @param name The name of the actor.
    */
   @Requires({"name != null"})
   @Ensures({"name().equals(name)"})
   public Actor(String name) {
      super(name);
   }

   public Actor() {
      super("");
   }

   @Override
   protected Actor cloneSelf() {
      return new Actor();
   }
}
