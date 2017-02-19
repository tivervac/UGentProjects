package org.aikodi.lang.funky.usecase;

import org.aikodi.chameleon.core.declaration.SimpleNameSignature;
import org.aikodi.lang.funky.behavior.Behavior;
import org.aikodi.lang.funky.conditions.Condition;

import com.google.java.contract.Ensures;

/**
 * An exceptional flow does not return normall.
 * 
 * @author Marko van Dooren
 */
public class ExceptionalFlow extends AbstractFlow {

   /**
    * Create a new exceptional flow with the given name, condition, and behavior.
    * 
    * @param name The name of the exceptional flow.
    * @param condition The condition under which the exceptional flow is executed.
    * @param behavior The behavior of the exceptional flow.
    */
   @Ensures({"name().equals(name)",
      "condition() == condition",
      "behavior() == behavior"})
   public ExceptionalFlow(String name, Condition condition, Behavior behavior) {
      super(new SimpleNameSignature(name), condition, behavior);
   }
   
   /**
    * Create a new exceptional flow with the given signature, condition, and behavior.
    * 
    * @param signature The signature of the exceptional flow.
    * @param condition The condition under which the exceptional flow is executed.
    * @param behavior The behavior of the exceptional flow.
    */
   @Ensures({"signature() == signature",
             "condition() == condition",
             "behavior() == behavior"})
   public ExceptionalFlow(SimpleNameSignature signature, Condition condition, Behavior behavior) {
      super(signature,condition,behavior);
   }

   @Override
   protected ExceptionalFlow cloneSelf() {
      return new ExceptionalFlow((SimpleNameSignature)null,null,null);
   }


    @Override
    public boolean isSuccessful(){
        return false;
    }
}
