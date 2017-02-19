package org.aikodi.lang.funky.builder;

import java.util.function.Consumer;

import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.lang.funky.behavior.Behavior;
import org.aikodi.lang.funky.behavior.BranchingPoint;
import org.aikodi.lang.funky.behavior.LabeledBehavior;
import org.aikodi.lang.funky.behavior.Return;
import org.aikodi.lang.funky.behavior.TextualStep;
import org.aikodi.lang.funky.behavior.description.Description;
import org.aikodi.lang.funky.behavior.description.TextualDescription;
import org.aikodi.lang.funky.executors.ExecutingEntity;
import org.aikodi.lang.funky.usecase.AlternateFlow;
import org.aikodi.lang.funky.usecase.ExceptionalFlow;

public class BehaviorBuilder<T> extends Builder<T,Behavior> {

   public BehaviorBuilder(T parent, Consumer<Behavior> consumer) {
      super(parent, consumer);
   }

   public T step(String text, String qualifiedNameOfExecutor) {
      consumer().accept(
            new TextualStep(
                  new TextualDescription(text),
                  createExecutorReference(qualifiedNameOfExecutor)));
      return parent();
   }

   private NameReference<ExecutingEntity> createExecutorReference(String executor) {
      return new NameReference<ExecutingEntity>(executor, ExecutingEntity.class);
   }
   
   public T step(Description description, String executor) {
      consumer().accept(new TextualStep(description, createExecutorReference(executor)));
      return parent();
   }
   
   public BlockBuilder<T> openBlock() {
      BlockBuilder<T> builder = new BlockBuilder<T>(parent(),b -> {consumer().accept(b);});
      return builder;
   }
   
   public BehaviorBuilder<T> label(String label) {
      return label(label, l -> {});
   }
   
   public BehaviorBuilder<T> alternate(String target) {
      BranchingPoint<AlternateFlow> result = new BranchingPoint<AlternateFlow>(
            new NameReference<AlternateFlow>(target,AlternateFlow.class), null);
      BehaviorBuilder<T> builder = 
            new BehaviorBuilder<T>(parent(),b -> {result.setNormalBehavior(b);}
            );
      consumer().accept(result);
      return builder;

   }
   
   public BehaviorBuilder<T> exception(String target) {
      BranchingPoint<ExceptionalFlow> result = new BranchingPoint<ExceptionalFlow>(
            new NameReference<ExceptionalFlow>(target,ExceptionalFlow.class), null);
      BehaviorBuilder<T> builder = 
            new BehaviorBuilder<T>(parent(),b -> {result.setNormalBehavior(b);}
            );
      consumer().accept(result);
      return builder;

   }
   
   public BehaviorBuilder<T> label(String label, Consumer<LabeledBehavior> peeker) {
      LabeledBehavior result = new LabeledBehavior(label, null);
      BehaviorBuilder<T> builder = 
            new BehaviorBuilder<T>(parent(),b -> {result.setBehavior(b);}
            );
      peeker.accept(result);
      consumer().accept(result);
      return builder;
   }
   
   public T returnTo(String targetStep, String executor) {
      consumer().accept(
            new Return(
                  new NameReference<LabeledBehavior>(targetStep, LabeledBehavior.class),
                  createExecutorReference(executor)
                  ));
      return parent();
   }
   
   public WhileBuilder<T> doWhile() {
      WhileBuilder<T> builder = new WhileBuilder<T>(parent(),b -> {consumer().accept(b);});
      return builder;
   }

}
