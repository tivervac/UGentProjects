package org.aikodi.lang.funky.behavior;

import com.google.java.contract.Ensures;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.lang.funky.executors.ExecutingEntity;
import org.aikodi.lang.funky.virtualmachine.VirtualProcess;

import java.util.ArrayList;
import java.util.List;

/**
 * An interface for behavior in a model for functional analysis.
 * 
 * @author Marko van Dooren
 */
public interface Behavior extends Element {

   /**
    * @return The entity that initiates the behavior.
    * @throws LookupException 
    */
   @Ensures({"result != null"})
   ExecutingEntity executor() throws LookupException;

    /**
     * Simulates the behavior of a node
     * Calls the host back with the next nodes in the flow
     * @param parent the process that's executing this node
     */
    void execute(VirtualProcess parent);

    /**
     * Used to find all next steps, inside or outside this Behavior
     * @param current the last node visited, the node we want to find the next node of
     * @return list all possible next steps
     */
    default List<Behavior> findNext(Behavior current){
        // Default implementation
        // Get the next nodes from the parent
        List<Behavior> result = new ArrayList<>();

        Behavior parent = nearestAncestor(Behavior.class);
        if (parent != null){
            result = parent.findNext(this);
        }

        return result;
    }

}
