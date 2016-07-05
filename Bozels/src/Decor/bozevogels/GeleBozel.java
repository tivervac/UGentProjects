package decor.bozevogels;

import decor.Objecten;
import java.awt.Color;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class GeleBozel extends Objecten {
    
    public GeleBozel(SpelModel model) {
        super(new Color(255,100,0), 10.0f, 0.1f, 0.9f, Float.MAX_VALUE, Float.MAX_VALUE, model, "yellow", false);
    }
}
