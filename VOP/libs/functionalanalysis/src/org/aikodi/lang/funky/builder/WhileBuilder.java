package org.aikodi.lang.funky.builder;

import java.util.function.Consumer;

import org.aikodi.lang.funky.behavior.Behavior;
import org.aikodi.lang.funky.behavior.WhileLoop;

public class WhileBuilder<T> extends Builder<T,Behavior> {

   private WhileLoop _while;
   
   public WhileBuilder(T parent, Consumer<Behavior> consumer) {
      super(parent, consumer);
      _while = new WhileLoop(null, null);
      consumer.accept(_while);
   }

   public ConditionBuilder<WhileBuilder<T>> condition() {
      return new ConditionBuilder<WhileBuilder<T>>(this, c -> {_while.setCondition(c);});
   }
   
   public BlockBuilder<T> openBlock() {
      return new BlockBuilder<T>(parent(), b -> {_while.setBody(b);});
   }
}
