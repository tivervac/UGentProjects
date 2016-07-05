package obst;

import java.util.Arrays;

/**
 *
 * @author Titouan Vervack
 *
 * A case independent bottom up way of splaying. 
 * The first version.
 */
public class Sbst1 extends Sbst {

    @Override
    protected void splay(Node start) {
        //Arrays because they're faster than lists
        Node[] splay = new Node[3];
        NodeWrapper[] buitenBomen = new NodeWrapper[4];
        //If we're not in the root or any of his children => semi-splay
        while (start != null && start != root && start.getParent() != root) {
            //Find the nodes on which we have to semi-splay
            splay[0] = start;
            splay[1] = start.getParent();
            splay[2] = start.getParent().getParent();
            //Find the children trees of the splaynodes
            //Put the trees into a wrapper for sorting purposes
            buitenBomen[0] = new NodeWrapper(start.getLeftChild(), 1 * -1);
            buitenBomen[1] = new NodeWrapper(start.getRightChild(), 1 * 1);
            //Select the right tree depending on which of the 4 cases we encountered
            buitenBomen[2] = (splay[1].getLeftChild() == start)
                    ? new NodeWrapper(splay[1].getRightChild(), 2 * 1)
                    : new NodeWrapper(splay[1].getLeftChild(), 2 * -1);
            buitenBomen[3] = (splay[2].getLeftChild() == splay[1])
                    ? new NodeWrapper(splay[2].getRightChild(), 3 * 1)
                    : new NodeWrapper(splay[2].getLeftChild(), 3 * -1);
            //Remember the parent of the top splaynode, so we can continue splaying later on
            Node nextSplay = splay[2].getParent();
            //Put the nodes in the correct position
            Arrays.sort(splay, new NodeComparator());
            Arrays.sort(buitenBomen, new NodeWrapperComparator());

            //Cut away the children from the bottom 2 splaynodes
            splay[0].reset();
            splay[2].reset();

            for (int i = 0; i < 2; i++) {
                //Fill the free spots on each splaynode with the original children trees
                //And set the parents of the children trees
                if (buitenBomen[i].node != null) {
                    splay[0].addChild(buitenBomen[i].node);
                    buitenBomen[i].node.setParent(splay[0]);
                }
                if (buitenBomen[i + 2].node != null) {
                    splay[2].addChild(buitenBomen[i + 2].node);
                    buitenBomen[i + 2].node.setParent(splay[2]);
                }
            }
            //Change the original order to the semi-splayed order
            splay[1].addChild(splay[0]);
            splay[1].addChild(splay[2]);
            //Now fix the parents to complete the sorting
            splay[0].setParent(splay[1]);
            splay[1].setParent(nextSplay);
            splay[2].setParent(splay[1]);
            //Continue splaying if needed
            if (nextSplay == null) {
                //Adjust root
                root = splay[1];
                break;
            }
            nextSplay.addChild(splay[1]);
            start = splay[1];
        }
    }

    @Override
    public String toString() {
        return "Sbst1";
    }
}
