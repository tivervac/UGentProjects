package decor.doelen;

import decor.Objecten;
import java.awt.Color;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class KleinDoel extends Objecten {
    
    public KleinDoel(SpelModel model) {
        super(Color.GREEN, 1.0f, 0.1f, 0.9f, 7.0f, 10.0f, model, "small", true);
    }
}
