package obst;

/**
 *
 * @author Titouan Vervack
 */
public abstract class Sbst extends AbstractTree {

    @Override
    public int add(int key) {
        int result = (super.add(key));
        splay(added);
        return result;
    }

    @Override
    public int contains(int key) {
        int result = super.contains(key);
        splay(searched);
        return result;
    }

    protected abstract void splay(Node start);
}
