package decor;

import modellen.SpelModel;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

/**
 *
 * @author Titouan Vervack
 */
public class Blok {

    public Blok(float x, float y, String material, float angle, float width, float height, SpelModel model) {
        //Zoek het materiaal
        Objecten mat = model.getMap().get(material);
        //Maak een blok in JBox2D aan
        World wereld = model.getWorld();
        BodyDef rechthoek = new BodyDef();
        rechthoek.position.set(new Vec2(x, y));
        rechthoek.angle = -angle;
        if (material.equals("solid")) {
            rechthoek.type = BodyType.STATIC;
        } else {
            rechthoek.type = BodyType.DYNAMIC;
        }
        Body blok = wereld.createBody(rechthoek);
        PolygonShape blokShape = new PolygonShape();
        blokShape.setAsBox(width / 2, height / 2);

        FixtureDef blokFD = new FixtureDef();
        blokFD.shape = blokShape;
        blokFD.density = mat.getDichtheid();
        blokFD.friction = mat.getWrijving();
        blokFD.restitution = mat.getRestitutie();
        blok.createFixture(blokFD);
        
        //Voeg de blok toe aan de nodige lijsten
        model.addBody(blok, material);
        model.addSchade(blok);
    }
}
