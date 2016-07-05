package decor;

import modellen.SpelModel;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 *
 * @author Titouan Vervack
 */
public class Bal {

    public Bal(float x, float y, String material, float angle, float width, float height, SpelModel model) {
        //Zoek het materiaal
        Objecten mat = model.getMap().get(material);
        //Maak een bal in JBox2D aan
        BodyDef balBD = new BodyDef();
        balBD.position.set(new Vec2(x, y));
        balBD.angle = angle;
        if (material.equals("solid")) {
            balBD.type = BodyType.STATIC;
        } else {
            balBD.type = BodyType.DYNAMIC;
        }
        Body bal = model.getWorld().createBody(balBD);

        CircleShape balShape = new CircleShape();
        balShape.m_radius = width/2;

        FixtureDef balFD = new FixtureDef();
        balFD.shape = balShape;
        balFD.density = mat.getDichtheid();
        balFD.friction = mat.getWrijving();
        balFD.restitution = mat.getRestitutie();
        bal.createFixture(balFD);
        
        //Voeg de bal toe aan de nodige lijsten
        model.addBody(bal, material);
        model.addSchade(bal);
    }
}
