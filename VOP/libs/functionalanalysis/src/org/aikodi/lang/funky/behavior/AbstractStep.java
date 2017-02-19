package org.aikodi.lang.funky.behavior;

import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.util.association.Single;
import org.aikodi.lang.funky.executors.ExecutingEntity;

import com.google.java.contract.Ensures;

/**
 * A class of basic steps in a use case. A basic step is executed by some
 * executing entity.
 * 
 * @author Marko van Dooren
 */
public abstract class AbstractStep extends ElementImpl implements Behavior {

   private Single<CrossReference<? extends ExecutingEntity>> _executingActor = new Single<>(
         this, true, "reference to an executing entity");

   /**
    * Create a new step that is executed by the {@link ExecutingEntity}
    * that is referenced by the given cross-reference.
    * 
    * @param executorReference A cross-reference to the executing entity that
    *                          executes this step.
    */
   public AbstractStep(CrossReference<? extends ExecutingEntity> executorReference) {
      setExecutingEntity(executorReference);
   }

   /**
    * Just for cloning.
    */
   protected AbstractStep() {
      super();
   }

   /**
    * {@inheritDoc}
    * 
    * @return The entity referenced by the {@link #executingEntityReference()}. If no
    *         reference is set, null is returned.
    * @throws LookupException
    *            The entity could not be resolved.
    */
   @Ensures({"(executingEntityReference() == null && result == null)"
         + "|| (executingEntityReference() != null && result == executingEntityReference().getElement())"})
   public ExecutingEntity executor() throws LookupException {
      if (executingEntityReference() != null) {
         return executingEntityReference().getElement();
      } else {
         return null;
      }
   }
   
   /**
    * Set the {@link CrossReference} to the executing entity.
    * 
    * @param executor
    *           A cross-reference to the executing entity.
    */
   public void setExecutingEntity(CrossReference<? extends ExecutingEntity> executor) {
      set(_executingActor, executor);
   }

    /**
     * @return Get the full text of this step
     */
    public String text(){
        return "abstract";
    }

   /**
    * @return A cross-reference to the entity that executes this step.
    */
   public CrossReference<? extends ExecutingEntity> executingEntityReference() {
      return _executingActor.getOtherEnd();
   }

}
