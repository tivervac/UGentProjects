package org.aikodi.lang.funky.executors;

import java.util.ArrayList;
import java.util.List;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.lang.funky.usecase.UseCase;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

/**
 * A subject is a system whose behavior is described by use cases.
 * 
 * @author Marko van Dooren
 */
public class Subject extends ExecutingEntity {

   private Multi<CrossReference<? extends UseCase>> _useCases = new Multi<>(this,1,-1,"use cases");

   /**
    * Create a new subject with the given name.
    * 
    * @param name The name of the subject. The name is used
    *             for the signature of the subject.
    */
   public Subject(String name) {
      super(name);
   }

   @Override
   public Subject cloneSelf() {
      return new Subject(name());
   }

   /**
    * Return the use cases that describe the behavior of this subject.
    * 
    * @return A list of use cases that describe the behavior of this subject.
    * @throws LookupException A reference to a use case could not be resolved.
    */
   @Ensures("result != null")
   public List<UseCase> useCases() throws LookupException {
      List<UseCase> result = new ArrayList<>();
      for(CrossReference<? extends UseCase> cref: _useCases.getOtherEnds()) {
         result.add(cref.getElement());
      }
      return result;
   }

   /**
    * Add a cross-reference to a use case that describes the behavior of this
    * subject.
    * 
    * @param reference A cross-reference to a use case.
    */
   @Requires({"reference != null"})
   @Ensures({"useCaseCrossReference().get(useCaseCrossReference().size() - 1) == reference",
      "useCaseCrossReference().subList(0,useCaseCrossReference().size() - 1).equals(old(useCaseCrossReference()))"})
   public void addUseCaseReference(CrossReference<? extends UseCase> reference) {
      add(_useCases,reference);
   }
   
   /**
    * @return the cross-references to the use cases that describe the behavior
    * of this subject.
    */
   @Ensures({"result != null"})
   public List<CrossReference<? extends UseCase>> useCaseCrossReference() {
      return _useCases.getOtherEnds();
   }
}
