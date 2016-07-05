package obst;

import java.util.Arrays;

/**
 *
 * @author Titouan Vervack
 */
public abstract class AbstractTree implements BST {

    protected Node root;
    protected Node added;
    protected Node searched;
    protected int size;

    public AbstractTree() {
        root = null;
        added = null;
        searched = null;
        size = 0;
    }

    @Override
    public int contains(int key) {
        Node current = root;
        int compares = 0;
        while (current != null) {
            compares++;
            searched = current;
            if (key == current.getKey()) {
                current.addWeight();
                return compares;
            }
            current = (key < current.getKey()) ? current.getLeftChild() : current.getRightChild();
        }
        return -compares;
    }

    public int add(Node node) {
        int result = add(node.getKey());
        added.setWeight(node.getWeight());
        return result;
    }

    @Override
    public int add(int key) {
        //First add
        if (root == null) {
            root = new Node(key);
            added = root;
            size++;
            return 0;
        }

        Node current = root;
        int compares = 0;
        Node previous = null;
        while (current != null) {
            compares++;
            //Key already in tree
            if (key == current.getKey()) {
                current.addWeight();
                added = current;
                return -compares;
            }
            previous = current;
            current = (key < current.getKey()) ? current.getLeftChild() : current.getRightChild();
        }
        previous.addChild((added = new Node(key, previous)));
        size++;
        return compares;
    }

    @Override
    public int cost() {
        return cost(root, 0);
    }

    private int cost(Node node, int depth) {
        if (node == null) {
            return 0;
        }
        return (node.getWeight() * (depth + 1)) + cost(node.getLeftChild(), depth + 1)
                + cost(node.getRightChild(), depth + 1);
    }

    public void printTree() {
        printTree(root, 0, 0, "M");
    }

    //Print de boom uit
    public void printTree(Node node, int parent, int depth, String pos) {
        if (node == null) {
            return;
        }
        System.out.print(pos + "-");
        for (int i = 0; i < depth; i++) {
            System.out.print("--");
        }
        System.out.println(node.getKey() + " (" + parent + ") " + node.getWeight());
        printTree(node.getLeftChild(), node.getKey(), depth + 1, "L");
        printTree(node.getRightChild(), node.getKey(), depth + 1, "R");
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void balance() {
        //Add the keys to an array in a sorted way
        Node[] balanced = new Node[size];
        treeToArray(root, balanced);
        index = 0;
        //Reset the original tree and rebuild it
        root = null;
        size = 0;
        balancedBuild(this, balanced);
    }

    private void balancedBuild(AbstractTree o, Node[] balanced) {
        if (balanced.length == 1) {
            o.add(balanced[0]);
        } else if (balanced.length != 0) {
            int halfSize = balanced.length / 2;
            o.add(balanced[halfSize]);
            balancedBuild(o, Arrays.copyOfRange(balanced, 0, (halfSize)));
            balancedBuild(o, Arrays.copyOfRange(balanced, (halfSize) + 1, balanced.length));
        }
    }

    protected static int index = 0;

    protected void treeToArray(Node current, Node[] tree) {
        if (current != null) {
            treeToArray(current.getLeftChild(), tree);
            tree[index++] = current;
            treeToArray(current.getRightChild(), tree);
        }
    }

    @Override
    public void optimize() {
    }
}
