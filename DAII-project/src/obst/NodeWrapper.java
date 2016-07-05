package obst;

/**
 *
 * @author Titouan Vervack
 */
public class NodeWrapper {

    /* Holds a weight for a child tree in semi-splay
     * If you project all the child tree's of a semi-splay tree onto a line,
     * they will be in the right order. By using the formula: 
     * depth * (- leftChild * rightChild) you can project them the right way
     * @depth: a number in the range [1-3], 1 being the deepest depth,
     * 2 being the middel depth, 3 being the top depth of the 3 splay nodes.
     * @leftChild: 1 when the child tree is a left child, else it's 0
     * @rightChild: 1 when the child tree is a right child, else it's 0
     */
    public int index;
    public Node node;

    public NodeWrapper(Node node, int weight) {
        this.index = weight;
        this.node = node;
    }
}
