package org.aikodi.lang.funky.usecase;

import org.aikodi.chameleon.core.declaration.CommonDeclaration;
import org.aikodi.chameleon.core.declaration.Signature;
import org.aikodi.chameleon.util.association.Single;
import org.aikodi.lang.funky.behavior.Behavior;
import org.aikodi.lang.funky.conditions.Condition;

import com.google.java.contract.Ensures;

public abstract class AbstractFlow  extends CommonDeclaration {

   private Single<Behavior> _behavior = new Single<>(this,true);
   private Single<Condition> _condition = new Single<>(this,true);

   public AbstractFlow() {
      super();
   }

   /**
    * Create a new alternate flow with the given name, condition, and behavior.
    * 
    * @param signature The name of the alternate flow.
    * @param condition The condition under which the alternate flow is executed.
    * @param behavior The behavior of the alternate flow.
    */
   @Ensures({"signature() == signature",
      "condition() == condition",
      "behavior() == behavior"})
   public AbstractFlow(Signature signature, Condition condition, Behavior behavior) {
      super(signature);
      setCondition(condition);
      setBehavior(behavior);
   }

   /**
    * @return The behavior of this alternate flow.
    */
   public Behavior behavior() {
      return _behavior.getOtherEnd();
   }

   /**
    * Set the new behavior of this alternate flow.
    * 
    * @param behavior The new behavior
    */
   @Ensures({"behavior() == behavior"})
   public void setBehavior(Behavior behavior) {
      set(_behavior, behavior);
   }

   /**
    * @return The condition for this alternate flow.
    */
   public Condition condition() {
      return _condition.getOtherEnd();
   }

   /**
    * Set the new condition of this alternate flow.
    * 
    * @param condition The new condition
    */
   @Ensures({"condition() == condition"})
   public void setCondition(Condition condition) {
      set(_condition, condition);
   }


    public boolean isSuccessful(){
        return true;
    }

   
}
