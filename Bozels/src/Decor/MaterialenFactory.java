package decor;

import decor.bozevogels.BlauweBozel;
import decor.bozevogels.GeleBozel;
import decor.bozevogels.RodeBozel;
import decor.bozevogels.WitteBozel;
import decor.doelen.GrootDoel;
import decor.doelen.KleinDoel;
import decor.materialen.*;
import java.util.HashMap;
import modellen.SpelModel;

/**
 *
 * @author Titouan Vervack
 */
public class MaterialenFactory extends HashMap{

    //Maak een HashMap waarmee we strings kunnen linken aan een materiaal
    public MaterialenFactory(SpelModel model) {
        put("wood", new Hout(model));
        put("ice", new Ijs(model));
        put("metal", new Metaal(model));
        put("stone", new Steen(model));
        put("solid", new Vast(model));
        put("red", new RodeBozel(model));
        put("blue", new BlauweBozel(model));
        put("white", new WitteBozel(model));
        //Zwart doet hetzelfde als een witte bozel en is dus ook een witte
        put("black", new WitteBozel(model));
        put("yellow", new GeleBozel(model));
        put("small", new KleinDoel(model));
        put("big", new GrootDoel(model));
    }
}
