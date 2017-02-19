package org.aikodi.lang.funky.behavior;

import com.google.java.contract.Ensures;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.util.association.Single;
import org.aikodi.lang.funky.conditions.Condition;
import org.aikodi.lang.funky.executors.ExecutingEntity;
import org.aikodi.lang.funky.virtualmachine.VirtualProcess;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class for loops in behavior.
 * 
 * @author Marko van Dooren
 */
public abstract class Loop extends ElementImpl implements Behavior {

   private Single<Condition> _condition = new Single<>(this,true,"condition of the loop");
   private Single<Behavior> _body = new Single<>(this,true,"body of the loop");

   /**
    * Create a new loop with the given condition and body.
    * 
    * @param condition The condition that determines when the loop will
    *                  stop executing.
    * @param body The body of the loop.
    */
   @Ensures({"condition() == condition",
             "body() == body"})
   public Loop(Condition condition, Behavior body) {
      setBody(body);
      setCondition(condition);
   }

   /**
    * @return the body of this loop.
    */
   public Behavior body() {
      return _body.getOtherEnd();
   }

   /**
    * Set the body of this loop.
    * @param body The new body of the loop.
    */
   @Ensures({"body() == body"})
   public void setBody(Behavior body) {
      set(_body,body);
   }

   /**
    * @return the condition of this loop.
    */
   public Condition condition() {
      return _condition.getOtherEnd();
   }

   /**
    * Set the condition of this loop.
    * @param condition The new condition of the loop.
    */
   public void setCondition(Condition condition) {
      set(_condition, condition);
   }

   /**
    * {@inheritDoc}
    * 
    * @return The initiator of the body. Null if there is no body.
    */
   @Ensures({"(body() == null && result == null) || (body != null && result = body().executor())"})
   @Override
   public ExecutingEntity executor() throws LookupException {
      return body().executor();
   }

    /**
     * Specialized implementation
     * Adds Loop node to the next nodes
     * @param current the last node visited, the node we want to find the next node of
     * @return list of next nodes
     */
    @Override
    public List<Behavior> findNext(Behavior current){
        List<Behavior> result = new ArrayList<>();

        Behavior parent = nearestAncestor(Behavior.class);
        if (parent != null){
            result = parent.findNext(this);
        }

        result.add(this);

        return result;
    }

    //TODO split while/repeat
    // Current behaviour is while
    @Override
    public void execute(VirtualProcess process) {
        process.advance(body(), condition(), this);

    }


}
