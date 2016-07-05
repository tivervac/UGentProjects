package decor.materialen;

import decor.Objecten;
import java.awt.Color;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class Hout extends Objecten {

    public Hout(SpelModel model) {
        super(new Color(123, 49, 11), 0.75f, 0.3f, 0.8f, 10.0f, 12.0f, model, "wood", true);
    }
}
