package decor.bozevogels;

import decor.Objecten;
import java.awt.Color;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class WitteBozel extends Objecten {
    
    public WitteBozel(SpelModel model) {
        super(Color.LIGHT_GRAY, 5.0f, 0.0f, 0.2f, Float.MAX_VALUE, Float.MAX_VALUE, model, "white", false);
    }
}
