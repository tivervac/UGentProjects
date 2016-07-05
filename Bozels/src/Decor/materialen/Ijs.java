package decor.materialen;

import decor.Objecten;
import java.awt.Color;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class Ijs extends Objecten {

    public Ijs(SpelModel model) {
        super(Color.BLUE, 1.0f, 0.0f, 0.1f, 15.0f, 10.0f, model, "ice", true);
    }
}
