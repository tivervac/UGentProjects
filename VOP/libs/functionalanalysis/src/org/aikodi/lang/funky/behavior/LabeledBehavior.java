package org.aikodi.lang.funky.behavior;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;
import org.aikodi.chameleon.core.declaration.CommonDeclaration;
import org.aikodi.chameleon.core.declaration.SimpleNameSignature;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.util.association.Single;
import org.aikodi.lang.funky.executors.ExecutingEntity;
import org.aikodi.lang.funky.virtualmachine.VirtualProcess;

/**
 * A behavior that can be referenced. This class acts as a decorator
 * for another {@link Behavior} and only added the signature.
 * 
 * @author Marko van Dooren
 */
public class LabeledBehavior extends CommonDeclaration implements Behavior {

   private Single<Behavior> _behavior = new Single<>(this,true);
   
   /**
    * Create a new named behavior with the given name and actual behavior.
    * 
    * @param name The name by which the behavior can be referenced.
    * @param behavior The actual behavior.
    */
   @Requires({"name != null"})
   @Ensures({"name().equals(name)",
             "behavior() == behavior"})
   public LabeledBehavior(String name, Behavior behavior) {
      super(new SimpleNameSignature(name));
      setBehavior(behavior);
   }

   /**
    *  Just for cloning
    */
   protected LabeledBehavior() {
   }
   
   @Override
   protected Element cloneSelf() {
      return new LabeledBehavior();
   }

   /**
    * {@inheritDoc}
    * 
    * @return The initiator of the decorated behavior.
    */
   @Ensures({"result == behavior().initiator()"})
   @Override
   public ExecutingEntity executor() throws LookupException {
      return behavior().executor();
   }

    @Override
    public void execute(VirtualProcess parent) {
       //Execute the behavior
       parent.advance(behavior());
    }

    /**
    * @return the behavior of this named behavior.
    */
   public Behavior behavior() {
      return _behavior.getOtherEnd();
   }

   /**
    * Set the behavior of this named behavior.
    * 
    * @param behavior The actual behavior that is decorated by this
    *                 named behavior.
    */
   @Ensures({"behavior() == behavior"})
   public void setBehavior(Behavior behavior) {
      set(_behavior,behavior);
   }
}
