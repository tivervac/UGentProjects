package obst;

import java.util.Comparator;

/**
 *
 * @author Titouan Vervack
 */
public class NodeWrapperComparator implements Comparator<NodeWrapper> {

    @Override
    public int compare(NodeWrapper o1, NodeWrapper o2) {
        return o1.index - o2.index;
    }
}
