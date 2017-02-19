package org.aikodi.lang.funky.usecase;

import org.aikodi.chameleon.util.association.Single;
import org.aikodi.lang.funky.behavior.Behavior;

import com.google.java.contract.Ensures;
import org.aikodi.lang.funky.behavior.Block;
import org.aikodi.lang.funky.behavior.description.TextualDescription;
import org.aikodi.lang.funky.conditions.TextualCondition;

import java.util.HashMap;
import java.util.List;

/**
 * A class of regular use cases with a normal behavior.
 * 
 * This is a separate class because UML also has extending use cases. An
 * extending use case, however, cannot have a normal flow, as that would
 * override the normal flow of the extended use case, making the
 * extension mostly pointless.
 * 
 * @author Marko van Dooren
 *
 */
public class RegularUseCase extends UseCase {

   private Single<Behavior> _normalFlow = new Single<>(this,true);

   /**
    * Create a new use case with the given name.
    * 
    * @param name The name of the use case.
    */
   @Ensures({"name().equals(name)"})
   public RegularUseCase(String name) {
      super(name);
   }

   /**
    * Just for cloning.
    */
   protected RegularUseCase() {
      super();
   }

   @Override
   protected RegularUseCase cloneSelf() {
      return new RegularUseCase();
   }

   /**
    * Set the normal flow of this use case.
    * 
    * @param normalFlow The normal behavior of this use case.
    */
   public void setNormalFlow(Behavior normalFlow) {
      set(_normalFlow, normalFlow);
   }
   
   /**
    * @return The normal flow of this use case.
    */
   public Behavior normalFlow() {
      return _normalFlow.getOtherEnd();
   }


}
