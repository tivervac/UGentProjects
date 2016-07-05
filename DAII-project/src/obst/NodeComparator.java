package obst;

import java.util.Comparator;

/**
 *
 * @author Titouan Vervack
 */
public class NodeComparator implements Comparator<Node> {

    @Override
    public int compare(Node o1, Node o2) {
        if(o1.getKey() > o2.getKey()) {
            return 1;
        } else if (o1.getKey() < o2.getKey()) {
            return -1;
        } else {
            return 0;
        }
    }
}
