package decor.bozevogels;

import decor.Objecten;
import java.awt.Color;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class RodeBozel extends Objecten {

    public RodeBozel(SpelModel model) {
        super(Color.RED, 10.0f, 0.3f, 0.9f, Float.MAX_VALUE, Float.MAX_VALUE, model, "red", false);
    }
}
