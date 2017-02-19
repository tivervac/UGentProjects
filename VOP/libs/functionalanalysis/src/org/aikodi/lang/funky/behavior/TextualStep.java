package org.aikodi.lang.funky.behavior;

import com.google.java.contract.Ensures;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.util.association.Single;
import org.aikodi.lang.funky.behavior.description.Description;
import org.aikodi.lang.funky.executors.ExecutingEntity;
import org.aikodi.lang.funky.virtualmachine.VirtualProcess;

import java.util.List;

/**
 * A step in a use case whose behavior is described in natural language.
 * 
 * @author Marko van Dooren
 */
public class TextualStep extends AbstractStep {

   private Single<Description> _description = new Single<>(this,true);

   /**
    * Create a new step with the given description.
    * 
    * @param description A description of the behavior of this step.
    * @param executorReference A cross-reference to the executing entity that
    *                          executes this step.
    */
   @Ensures({"description() == description"})
   public TextualStep(Description description, CrossReference<? extends ExecutingEntity> executorReference) {
      super(executorReference);
      setDescription(description);
   }
   
   /**
    * Just for cloning.
    */
   protected TextualStep() {      
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
   protected TextualStep cloneSelf() {
      return new TextualStep();
   }

   /**
    * {@inheritDoc}
    * 
    * If there is a description, the method calls toString() on the description
    * and returns the result. Otherwise, it returns the empty string.
    * 
    * This method is just here for debugging.
    */
   @Override
   public String toString() {
      Description description = description();
      return description == null ? "" : description.toString();
   }

    /**
     * @return Get the full text of this step
     */
    public String text(){
        return description().text();
    }

    @Override
    public void execute(VirtualProcess parent) {
        parent.handleStep(this);

        List<Behavior> next = findNext(this);
        for (Behavior n : next){
            parent.advance(n);
        }

        if (next.isEmpty()){
            parent.terminate();
        }
    }
}
