package org.aikodi.lang.funky.builder;

import java.util.function.Consumer;

import org.aikodi.lang.funky.usecase.AbstractFlow;

public class AbstractFlowBuilder<T,F extends AbstractFlow,S extends AbstractFlowBuilder<T,F,S>> extends Builder<T,F> {

   private F _flow;
   
   public AbstractFlowBuilder(T parent, Consumer<F> consumer, F flow) {
      super(parent, consumer);
      _flow = flow;
      consumer().accept(flow);
   }
   
   protected F alternateFlow() {
      return _flow;
   }
   
   public BlockBuilder<T> execute() {
      BlockBuilder<T> builder = new BlockBuilder<T>(parent(),b -> { alternateFlow().setBehavior(b);});
      return builder;
   }

   public ConditionBuilder<S> when() {
      ConditionBuilder<S> builder = 
            new ConditionBuilder<S>((S)this, (c -> {alternateFlow().setCondition(c);}));
      return builder;
   }
   


}
