package org.aikodi.lang.funky.behavior;

import com.google.java.contract.Ensures;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.util.association.Single;
import org.aikodi.lang.funky.executors.ExecutingEntity;
import org.aikodi.lang.funky.usecase.AbstractFlow;
import org.aikodi.lang.funky.virtualmachine.VirtualProcess;

/**
 * A branching point redirects the execution to another flow when the condition
 * of the flow is satisfied. The branching point keeps a cross-reference to
 * the other flow, and stores the behavior that must be executed when the condition
 * of the other flow is not satisfied.
 * 
 * @author Marko van Dooren
 *
 * @param <F> The type of the other flow.
 */
public class BranchingPoint<F extends AbstractFlow> extends ElementImpl implements Behavior {

   private Single<CrossReference<? extends F>> _target = new Single<>(this,true,"target flow");
   private Single<Behavior> _behavior = new Single<>(this,true,"behavior");
   
   /**
    * Create a new branching point with the given target reference and
    * normal behavior.
    * 
    * @param targetReference A cross-reference to the alternative flow that
    *                        may be executed.
    * @param normalBehavior The normal behavior that is executed when the
    *                       condition of the alternative flow is not satisfied.
    */
   public BranchingPoint(CrossReference<? extends F> targetReference, Behavior normalBehavior) {
      setTarget(targetReference);
      setNormalBehavior(normalBehavior);
   }
   
   /**
    * {@inheritDoc}
    * 
    * @return The target flow referenced by the {@link #targetReference()}. If no
    *         target reference is set, null is returned.
    * @throws LookupException
    *            The target flow could not be resolved.
    */
   @Ensures({"(targetReference() == null && result == null)"
         + "|| (targetReference() != null && result == targetReference().getElement())"})
   public F target() throws LookupException {
      if (targetReference() != null) {
         return targetReference().getElement();
      } else {
         return null;
      }
   }
   
   /**
    * Set the {@link CrossReference} to the target of this branching point.
    * 
    * @param target
    *           A cross-reference to the target of this branching point.
    */
   public void setTarget(CrossReference<? extends F> target) {
      set(_target, target);
   }

   /**
    * @return A cross-reference to the target of this branching point.
    */
   public CrossReference<? extends F> targetReference() {
      return _target.getOtherEnd();
   }

   /**
    * {@inheritDoc}
    * 
    * @return The initiator of the normal behavior.
    */
   @Ensures({"result == normalBehavior().initiator()"})
   @Override
   public ExecutingEntity executor() throws LookupException {
      return normalBehavior().executor();
   }

    @Override
    public void execute(VirtualProcess parent) {
        // Execute both the normal flow and branch
        try {
            AbstractFlow target = target();
            parent.advance(target.behavior(), target.condition(), this);
        } catch (LookupException | NullPointerException e) {
            // Do nothing
        }

        if (normalBehavior() != null) {
            parent.advance(normalBehavior());
        }
    }

    /**
    * @return the normal behavior of this branching point. This behavior
    *         is executed the condition of the target flow is not satisfied.
    */
   public Behavior normalBehavior() {
      return _behavior.getOtherEnd();
   }

   /**
    * Set the normal behavior of this branching point.
    * 
    * @param behavior The normal behavior that is executed when the condition
    *                 of the target flow is not satisfied.
    */
   @Ensures({"behavior() == behavior"})
   public void setNormalBehavior(Behavior behavior) {
      set(_behavior,behavior);
   }

   @Override
   protected Element cloneSelf() {
      return new BranchingPoint<F>(null, null);
   }
}
