package org.aikodi.lang.funky.executors;

import org.aikodi.chameleon.core.declaration.CommonDeclaration;
import org.aikodi.chameleon.core.declaration.SimpleNameSignature;

/**
 * Entities that can execute behavior in a use case.
 * 
 * @author Marko van Dooren
 */
public abstract class ExecutingEntity extends CommonDeclaration {

   /**
    * Create a new executing entity with the given name.
    * 
    * @param name The name of the executing entity.
    */
   public ExecutingEntity(String name) {
      super(new SimpleNameSignature(name));
   }
   
   /**
    * Just for cloning.
    */
   protected ExecutingEntity() {
   }

}
