package org.aikodi.lang.funky.behavior;

import org.aikodi.lang.funky.conditions.Condition;

/**
 * A while loop. A while loop executes its body as long as the 
 * condition is satisfied.
 * 
 * @author Marko van Dooren
 */
public class WhileLoop extends Loop {

   /**
    * Create a new while loop with the given condition and body.
    * 
    * @param condition The condition that determines when the while loop will
    *                  stop executing.
    * @param behavior The body of the while loop.
    */
   public WhileLoop(Condition condition, Behavior behavior) {
      super(condition, behavior);
   }

   @Override
   public WhileLoop cloneSelf() {
      return new WhileLoop(null, null);
   }
}
