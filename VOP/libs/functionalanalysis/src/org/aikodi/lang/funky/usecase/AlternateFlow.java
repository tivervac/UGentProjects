package org.aikodi.lang.funky.usecase;

import org.aikodi.chameleon.core.declaration.SimpleNameSignature;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.lang.funky.behavior.Behavior;
import org.aikodi.lang.funky.conditions.Condition;

import com.google.java.contract.Ensures;

/**
 * An alternate flow in a use case. An alternate flow describes
 * behavior that is executed when a particular condition is satisfied
 * during the execution of the use case.
 * 
 * @author Marko van Dooren
 */
public class AlternateFlow extends AbstractFlow {

   /**
    * Create a new alternate flow with the given name, condition, and behavior.
    * 
    * @param name The name of the alternate flow.
    * @param condition The condition under which the alternate flow is executed.
    * @param behavior The behavior of the alternate flow.
    */
   @Ensures({"name().equals(name)",
             "condition() == condition",
             "behavior() == behavior"})
   public AlternateFlow(String name, Condition condition, Behavior behavior) {
      super(new SimpleNameSignature(name),condition, behavior);
   }
   
   /**
    * Create a new alternate flow with the given signature, condition, and behavior.
    * 
    * @param signature The signature of the alternate flow.
    * @param condition The condition under which the alternate flow is executed.
    * @param behavior The behavior of the alternate flow.
    */
   @Ensures({"signature() == signature",
             "condition() == condition",
             "behavior() == behavior"})
   public AlternateFlow(SimpleNameSignature signature, Condition condition, Behavior behavior) {
      super(signature,condition,behavior);
   }
   
   @Override
   protected Element cloneSelf() {
      return new AlternateFlow((SimpleNameSignature)null,null,null);
   }

    @Override
    public boolean isSuccessful(){
        return true;
    }

}
