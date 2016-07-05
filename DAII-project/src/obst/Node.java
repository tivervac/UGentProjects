package obst;

/**
 *
 * @author Titouan Vervack
 */
public class Node {

    private int key;
    private Node parent;
    private Node leftChild;
    private Node rightChild;
    private int weight;

    public Node(int key) {
        this.key = key;
        parent = null;
        leftChild = null;
        rightChild = null;
        weight = 1;
    }

    public Node(int key, Node parent) {
        this(key);
        this.parent = parent;
    }

    public void addChild(Node child) {
        if (child.getKey() < key) {
            leftChild = child;
        } else {
            rightChild = child;
        }
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void addWeight() {
        weight++;
    }

    public int getKey() {
        return key;
    }

    public void reset() {
        leftChild = null;
        rightChild = null;
    }

    public void parentReset() {
        reset();
        parent = null;
    }
}
