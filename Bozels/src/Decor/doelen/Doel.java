package decor.doelen;

import decor.Objecten;
import modellen.SpelModel;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

/**
 *
 * @author Titouan Vervack
 */
public class Doel {

    private static final float KLEIN = (float) 2.5;
    private static final float GROOT = 4;

    public Doel(float x, float y, String type, SpelModel model) {
        //Zoek het materiaal
        Objecten mat = model.getMap().get(type);
        World wereld = model.getWorld();
        //Maak het juiste doel in JBox2D aan
        BodyDef rechthoek = new BodyDef();
        rechthoek.position.set(new Vec2(x, y));
        rechthoek.type = BodyType.DYNAMIC;
        Body blok = wereld.createBody(rechthoek);
        PolygonShape blokShape = new PolygonShape();

        if (type.equals("small")) {
            blokShape.setAsBox(KLEIN / 2, KLEIN / 2);
        } else {
            blokShape.setAsBox(GROOT / 2, GROOT / 2);
        }

        FixtureDef blokFD = new FixtureDef();
        blokFD.shape = blokShape;
        blokFD.density = mat.getDichtheid();
        blokFD.friction = mat.getWrijving();
        blokFD.restitution = mat.getRestitutie();
        blok.createFixture(blokFD);
        
        //Voeg het doel toe aan de nodige lijsten
        model.addBody(blok, type);
        model.addDoel(blok);
        model.addSchade(blok);
    }
}
