package decor.doelen;

import decor.Objecten;
import java.awt.Color;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class GrootDoel extends Objecten {

    public GrootDoel(SpelModel model) {
        super(Color.PINK, 1.0f, 0.1f, 0.9f, 5.0f, 10.0f, model, "big", true);
    }
}
