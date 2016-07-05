package decor.materialen;

import decor.Objecten;
import java.awt.Color;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class Steen extends Objecten {

    public Steen(SpelModel model) {
        super(Color.GRAY, 4.0f, 0.0f, 0.9f, 20.0f, 50.0f, model, "stone", true);
    }
}
