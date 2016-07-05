package decor.materialen;

import decor.Objecten;
import java.awt.Color;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class Vast extends Objecten {

    public Vast(SpelModel model) {
        super(Color.BLACK, 0.0f, 0.1f, 1.0f, 0.0f, 0.0f, model, "solid", false);
    }
}
