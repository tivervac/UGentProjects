package decor.bozevogels;

import decor.Objecten;
import java.awt.Color;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class BlauweBozel extends Objecten {
    
    public BlauweBozel(SpelModel model) {
        super(Color.BLUE, 8.0f, 0.7f, 1.0f, Float.MAX_VALUE, Float.MAX_VALUE, model, "blue", false);
    }
}
