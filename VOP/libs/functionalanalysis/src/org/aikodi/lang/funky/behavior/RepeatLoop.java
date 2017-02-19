package org.aikodi.lang.funky.behavior;

import org.aikodi.lang.funky.conditions.Condition;

/**
 * A repeat loop. A repeat loop always executes its body at least once
 * until the condition is satisfied.
 * 
 * @author Marko van Dooren
 */
public class RepeatLoop extends Loop {

   /**
    * Create a new repeat loop with the given condition and body.
    * 
    * @param condition The condition that determines when the repeat loop will
    *                  stop executing.
    * @param behavior The body of the repeat loop.
    */
   public RepeatLoop(Condition condition, Behavior behavior) {
      super(condition, behavior);
   }

   @Override
   public RepeatLoop cloneSelf() {
      return new RepeatLoop(null, null);
   }

}
