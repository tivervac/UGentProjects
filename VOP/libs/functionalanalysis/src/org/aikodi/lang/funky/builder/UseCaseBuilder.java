package org.aikodi.lang.funky.builder;

import java.util.function.Consumer;

import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.lang.funky.executors.Actor;
import org.aikodi.lang.funky.usecase.RegularUseCase;
import org.aikodi.lang.funky.usecase.UseCase;

/**
 * A builder for use cases.
 * 
 * @author Marko van Dooren
 *
 * @param <P> The type of the parent builder.
 */
public class UseCaseBuilder<P> extends Builder<P, UseCase> {

   private RegularUseCase _useCase;
    
   /**
    * Create a builder for a new use case with the given name. The 
    * parent and consumer are set to the given parent and consumer.
    * 
    * @param name The name of the use case that is created.
    * @param parent The parent builder to which control returns when this
    *               builder is done.
    * @param consumer The consumer that will process the use case that is created.
    */
   public UseCaseBuilder(String name, P parent, Consumer<UseCase> consumer) {
      super(parent, consumer);
      _useCase = new RegularUseCase(name);
      consumer().accept(useCase());
   }
   
   /**
    * @return The use case that is built by this builder.
    */
   public RegularUseCase useCase() {
      return _useCase;
   }

   public BlockBuilder<UseCaseBuilder<P>> normalFlow() {
      BlockBuilder<UseCaseBuilder<P>> builder = 
            new BlockBuilder<UseCaseBuilder<P>>(this, b -> {useCase().setNormalFlow(b);});
      return builder;
   }
   
   public AlternateFlowBuilder<UseCaseBuilder<P>> alternateFlow(String name) {
      AlternateFlowBuilder<UseCaseBuilder<P>> builder = 
            new AlternateFlowBuilder<UseCaseBuilder<P>>(name, this,a -> {_useCase.addAlternateFlow(a);});
      return builder;
   }
   
   public ConditionBuilder<UseCaseBuilder<P>> precondition() {
      return new ConditionBuilder<UseCaseBuilder<P>>(this, c -> {_useCase.addPrecondition(c);});
   }
   
   public ConditionBuilder<UseCaseBuilder<P>> postcondition() {
      return new ConditionBuilder<UseCaseBuilder<P>>(this, c -> {_useCase.addPostcondition(c);});
   }
   
   public UseCaseBuilder<P> primaryActor(String qualifiedName) {
      _useCase.addPrimaryActor(new NameReference<Actor>(qualifiedName, Actor.class));
      return this;
   }
   
   public P endUseCase() {
      return parent();
   }
}
