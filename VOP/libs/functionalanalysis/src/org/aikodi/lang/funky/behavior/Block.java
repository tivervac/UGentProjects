package org.aikodi.lang.funky.behavior;

import be.kuleuven.cs.distrinet.rejuse.association.Association;
import com.google.java.contract.Ensures;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.lang.funky.executors.ExecutingEntity;
import org.aikodi.lang.funky.virtualmachine.VirtualProcess;

import java.util.ArrayList;
import java.util.List;

/**
 * A flow consists of a series of behavior describing elements. The children can
 * be atomic instructions or composite flows.
 * 
 * @author Marko van Dooren
 */
public class Block extends ElementImpl implements Behavior {

   private Multi<Behavior> _elements = new Multi<>(this,1,-1,"children of a block");

   public Block(List<? extends Behavior> behs) {
      setBehaviors(behs);
   }

   public Block(Behavior element) {
      add(element);
   }
   
   public Block() {
      
   }

   /**
    * @return The elements of this block.
    */
   @Ensures({"result != null"})
   public List<Behavior> elements() {
      return _elements.getOtherEnds();
   }

   public void add(Behavior b) {
      add(_elements, b);
   }

   public void addAll(List<? extends Behavior> elements) {
      for (Behavior element : elements) {
         add(element);
      }
   }

   public void addInFront(List<? extends Behavior> elements) {
      for (Behavior element : elements) {
         _elements.addInFront((Association) element.parentLink());
      }
   }

   public void addBefore(Behavior element, List<? extends Behavior> elements) {
      for (Behavior e : elements) {
         _elements.addBefore(element, (Association) e.parentLink());
      }
   }

   public void addBehavior(int index, Behavior b) {
      if (index >= 0 && index < elements().size() && b != null) {
         _elements.addAtIndex((Association) b.parentLink(), index);
      }
   }

   public void removeBehavior(Behavior b) {
      remove(_elements, b);
   }

   /**
    * Remove the behavior at the given index.
    * 
    * @param index The index of the behavior to be removed.
    */
   public void removeBehavior(int index) {
      if (index >= 0 && index < _elements.size()) {
         Behavior t = _elements.elementAt(index + 1);
         _elements.remove((Association) t.parentLink());
      }
   }

   public void setBehaviors(List<? extends Behavior> behs) {
      _elements.clear();
      if (behs != null) {
         for (Behavior b : behs) {
            add(b);
         }
      }
   }

   @Override
   public Block cloneSelf() {
      return new Block();
   }

   @Override
   public ExecutingEntity executor() throws LookupException {
      if (elements().isEmpty()) {
         return null;
      } else {
         return elements().get(0).executor();
      }
   }

    @Override
    public void execute(VirtualProcess parent) {
        // Continue with the first child
        Behavior child = nearestDescendants(Behavior.class).get(0);
        parent.advance(child);
    }

    @Override
    public List<Behavior> findNext(Behavior current){
        // This so far is the only class with an actual 'next' part
        List<Behavior> result = new ArrayList<>();

        // Find the position of the current node
        boolean currentFound = false;
        boolean nextFound = false;
        for (Behavior ch : nearestDescendants(Behavior.class)){
            if (currentFound){
                result.add(ch);

                nextFound = true;
                break;
            }


            if (ch.equals(current)){
                currentFound = true;
            }
        }

        // If the next step is not in this block
        if (!nextFound){
            // Get the parent
            Behavior parent = nearestAncestor(Behavior.class);

            // If this node has a parent
            if (parent != null){
                // Find any possible next node after this block
                List<Behavior> brothers = parent.findNext(this);

                result.addAll(brothers);
                /*
                for (Behavior brother : brothers){
                    List<Behavior> nephews = brother.nearestDescendants(Behavior.class);

                    if (!nephews.isEmpty()){
                        // Add the first child of every next node
                        result.add(nephews.get(0));
                    } else {
                        // If there are no childs in that node, return the node itself
                        result.add(brother);
                    }
                }*/
            }
        }

        return result;
    }

}
