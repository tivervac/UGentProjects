package org.aikodi.lang.funky.behavior;

import com.google.java.contract.Ensures;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.util.association.Single;
import org.aikodi.lang.funky.executors.ExecutingEntity;
import org.aikodi.lang.funky.virtualmachine.VirtualProcess;


/**
 * A step that indicate the return from a flow to a particular labeled step.
 * 
 * @author Marko van Dooren
 */
public class Return extends AbstractStep {

   private Single<CrossReference<? extends LabeledBehavior>> _target = new Single<>(
         this, true, "reference to the target step");
   
   public Return(CrossReference<? extends LabeledBehavior> target, CrossReference<? extends ExecutingEntity> executorReference) {
      super(executorReference);
      setTarget(target);
   }
   
   /**
    * Just for cloning
    */
   protected Return() {
      
   }
   
   /**
    * {@inheritDoc}
    * 
    * @return The next step that will be executed. This is the step referenced 
    *         by the {@link #targetReference()}. If no reference is set, 
    *         null is returned.
    * @throws LookupException
    *            The step could not be resolved.
    */
   @Ensures({"(targetReference() == null && result == null)"
         + "|| (targetReference() != null && result == targetReference().getElement())"})
   public LabeledBehavior target() throws LookupException {
      if (targetReference() != null) {
         return targetReference().getElement();
      } else {
         return null;
      }
   }
   
   /**
    * Set the {@link CrossReference} to the next step.
    * 
    * @param target
    *           A cross-reference to the step that will be executed next.
    */
   public void setTarget(CrossReference<? extends LabeledBehavior> target) {
      set(_target, target);
   }

   /**
    * @return A cross-reference to the next step.
    */
   public CrossReference<? extends LabeledBehavior> targetReference() {
      return _target.getOtherEnd();
   }

   
   
   @Override
   protected Element cloneSelf() {
      return new Return();
   }

    @Override
    public void execute(VirtualProcess parent) {
        try {
            Behavior next = target();
            parent.advance(next);
        } catch (LookupException e) {
            e.printStackTrace();
        }
    }
}
