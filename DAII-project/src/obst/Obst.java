package obst;

import java.util.HashSet;

/**
 *
 * @author Titouan Vervack
 */
public abstract class Obst extends AbstractTree {

    @Override
    public void optimize() {
        Node[] tree = new Node[size];
        treeToArray(root, tree);
        index = 0;
        int[][] freqSums = new int[size][size];
        generateFreqSums(freqSums, tree);
        //Optimal cost from node i to node j
        //Because 0 to n-1 is the entire tree, this is the value for the optimal tree
        int[][] cost = new int[size][size];
        generateCostTable(tree, freqSums, cost);
    }

    private void generateFreqSums(int[][] freqSums, Node[] tree) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int freqSum = 0;
                for (int k = i; k <= j; k++) {
                    freqSum += tree[k].getWeight();
                }
                freqSums[i][j] = freqSum;
            }
        }
    }

    protected void buildOptimalTree(Node[] tree, int[][] freqSums, int[][] roots) {
        //Prepare tree for restructuring
        root = tree[roots[0][size - 1]];
        root.parentReset();
        int length = size;
        size = 0;
        //To check which indices have already been added
        HashSet<Integer> set = new HashSet<>(length);
        buildOptimalTree(tree, freqSums, roots, set, 0, length - 1, length, root);
    }

    private void buildOptimalTree(Node[] tree, int[][] freqSums, int[][] roots, HashSet<Integer> set, int i, int j, int length, Node parent) {
        if (i < 0 || j < 0 || i >= length || j >= length || (i > j)) {
            return;
        }
        int arrayIndex = roots[i][j];
        if (!set.add(arrayIndex)) {
            return;
        }
        
        Node current = tree[arrayIndex];
        current.parentReset();
        parent.addChild(current);
        current.setParent(parent);
        //leftChild
        if (arrayIndex >= 0) {
            //Optimal root of tree[i] to tree[k-1]
            buildOptimalTree(tree, freqSums, roots, set, i, arrayIndex - 1, length, current);
        }
        //rightChild
        if (arrayIndex < length) {
            //Optimal root of tree[k+1] to tree[j]
            buildOptimalTree(tree, freqSums, roots, set, arrayIndex + 1, j, length, current);
        }
    }

    protected abstract void generateCostTable(Node[] tree, int[][] freqSums, int[][] cost);
}
