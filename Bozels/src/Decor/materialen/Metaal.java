package decor.materialen;

import decor.Objecten;
import java.awt.Color;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class Metaal extends Objecten {

    public Metaal(SpelModel model) {
        super(Color.CYAN, 3.0f, 0.2f, 0.3f, 18.0f, 52.0f, model, "metal", true);
    }
}
