package obst;

/**
 *
 * @author Titouan Vervack
 *
 * A case dependent bottom up way of splaying. 
 * The second version.
 */
public class Sbst2 extends Sbst {

    @Override
    protected void splay(Node start) {
        while (start != root && start != null && start.getParent() != root) {
            Node parent = start.getParent();
            Node grandparent = parent.getParent();
            Node nextSplay = grandparent.getParent();
            Node A = null;
            Node B = null;
            Node C = null;
            Node tree2 = null;
            Node tree3 = null;

            //Find the splay tops and the childtrees
            if (grandparent.getLeftChild() == parent) {
                A = grandparent;
                if (parent.getLeftChild() == start) {
                    B = parent;
                    C = start;
                    tree2 = C.getRightChild();
                } else if (parent.getRightChild() == start) {
                    C = parent;
                    B = start;
                    tree2 = B.getLeftChild();
                }
                tree3 = B.getRightChild();
            } else if (grandparent.getRightChild() == parent) {
                C = grandparent;
                if (parent.getLeftChild() == start) {
                    A = parent;
                    B = start;
                    tree3 = B.getRightChild();
                } else if (parent.getRightChild() == start) {
                    B = parent;
                    A = start;
                    tree3 = A.getLeftChild();
                }
                tree2 = B.getLeftChild();
            }
            Node tree1 = C.getLeftChild();
            Node tree4 = A.getRightChild();
            //Fix the splay tops' order
            B.setLeftChild(C);
            B.setRightChild(A);
            B.setParent(nextSplay);
            C.setParent(B);
            A.setParent(B);

            //Add the trees
            C.setLeftChild(tree1);
            C.setRightChild(tree2);
            A.setLeftChild(tree3);
            A.setRightChild(tree4);

            //Give the trees parents if they're not null
            if (tree2 != null) {
                tree2.setParent(C);
            }
            if (tree3 != null) {
                tree3.setParent(A);
            }
            //Prepare for the next splay in case needed
            if (nextSplay == null) {
                //Adjust root
                root = B;
                break;
            }
            nextSplay.addChild(B);
            start = B;
        }
    }

    @Override
    public String toString() {
        return "Sbst2";
    }
}
