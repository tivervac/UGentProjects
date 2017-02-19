package org.aikodi.lang.funky.builder;

import java.util.function.Consumer;

import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.lang.funky.behavior.LabeledBehavior;
import org.aikodi.lang.funky.usecase.AlternateFlow;

public class AlternateFlowBuilder<T> extends AbstractFlowBuilder<T, AlternateFlow,AlternateFlowBuilder<T>> {

   public AlternateFlowBuilder(String name, T parent, Consumer<AlternateFlow> consumer) {
      super(parent,consumer, new AlternateFlow(name, null, null));
   }
 
}
