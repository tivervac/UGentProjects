package obst;

/**
 *
 * @author Titouan Vervack 
 * A half-recursive, half-dynamic programming way of doing optimize. 
 * The third version.
 */
public class Obst3 extends Obst {

    @Override
    protected void generateCostTable(Node[] tree, int[][] freqSums, int[][] cost) {
        int[][] roots = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cost[i][j] = getCost(i, j, freqSums, cost, 0, roots);
            }
        }
        buildOptimalTree(tree, freqSums, roots);
    }

    private int getCost(int i, int j, int[][] freqSum, int[][] cost, int depth, int[][] roots) {
        if (j < i) {
            return 0;
        }
        if (i == j) {
            roots[i][j] = i;
            return freqSum[i][j];
        }
        int min = Integer.MAX_VALUE;
        //Try every kth key as root
        for (int k = i; k <= j; k++) {
            int first = (k == 0) ? 0 : cost[i][k - 1];
            if (first == 0) {
                first = (k == 0) ? 0 : getCost(i, k - 1, freqSum, cost, depth + 1, roots);
            }
            int second = (k == size - 1) ? 0 : cost[k + 1][j];
            if (second == 0) {
                second = (k == size - 1) ? 0 : getCost(k + 1, j, freqSum, cost, depth + 1, roots);
            }
            int c = freqSum[i][j] + first + second;
            if (c < min) {
                min = c;
                roots[i][j] = k;
            }
        }
        //Now get the minimal cost and add the frequency
        return min;
    }

    @Override
    public String toString() {
        return "Obst3";
    }
}
