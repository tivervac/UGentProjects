package obst;

/**
 *
 * @author Titouan Vervack 
 * A non-recursive, dynamic programming way of doing optimize. 
 * The final version.
 */
public class Obst4 extends Obst {

    //We cannot calculate the cost like we used to
    //Instead we will calculate it the way we do when we do matrix multiplications
    //Fill the diagonal, then fill the diagonal above it and so on
    //Continue until you reach the upper right corner
    @Override
    protected void generateCostTable(Node[] tree, int[][] freqSums, int[][] cost) {
        //Calculate the middle diagonal first
        int[][] roots = new int[size][size];
        for (int i = 0; i < size; i++) {
            cost[i][i] = freqSums[i][i];
            roots[i][i] = i;
        }
        //The amount of diagonals
        for (int diags = 0; diags < size - 1; diags++) {
            //The row
            for (int i = 0; i < size - (diags + 1); i++) {
                //The collumn for the upper part
                int j = diags + i + 1;
                //Init cost at max so that you can check for lower costs
                cost[i][j] = Integer.MAX_VALUE;
                //Try all keys in tree[i..j] as root
                for (int k = i; k <= j; k++) {
                    //Left tree in case we chose k as root
                    int first = (k == 0) ? 0 : cost[i][k - 1];
                    //Right tree in case we chose k as root
                    int second = (k == size - 1) ? 0 : cost[k + 1][j];
                    int c = freqSums[i][j] + first + second;
                    //You found a lower cost!
                    if (c < cost[i][j]) {
                        cost[i][j] = c;
                        roots[i][j] = k;
                    }
                }
            }
        }
        buildOptimalTree(tree, freqSums, roots);
    }

    @Override
    public String toString() {
        return "Obst4";
    }
}
