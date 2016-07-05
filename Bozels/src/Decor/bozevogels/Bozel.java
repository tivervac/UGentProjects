package decor.bozevogels;

import decor.Objecten;
import modellen.SpelModel;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

/**
 *
 * @author Titouan Vervack
 */
public class Bozel {

    public Bozel(float x, float y, String kleur, int id, SpelModel model) {
        Body b;
        //Zoek het materiaal
        Objecten mat = model.getMap().get(kleur);
        World wereld = model.getWorld();
        //Maak de juiste bozel in JBox2D aan
        if (kleur.equals("blue")) {
            BodyDef balBD = new BodyDef();
            balBD.position.set(new Vec2(x, y));
            balBD.type = BodyType.DYNAMIC;
            balBD.angle = 0f;
            b = wereld.createBody(balBD);

            CircleShape balShape = new CircleShape();
            balShape.m_radius = 1;

            FixtureDef balFD = new FixtureDef();
            balFD.shape = balShape;
            balFD.density = mat.getDichtheid();
            balFD.friction = mat.getWrijving();
            balFD.restitution = mat.getRestitutie();
            b.createFixture(balFD);
        } else if (kleur.equals("yellow")) {
            BodyDef rechthoek = new BodyDef();
            rechthoek.position.set(new Vec2(x, y));
            rechthoek.type = BodyType.DYNAMIC;
            rechthoek.angle = 0f;
            b = wereld.createBody(rechthoek);
            PolygonShape blokShape = new PolygonShape();
            blokShape.setAsBox(1f, 0.5f);

            FixtureDef blokFD = new FixtureDef();
            blokFD.shape = blokShape;
            blokFD.density = mat.getDichtheid();
            blokFD.friction = mat.getWrijving();
            blokFD.restitution = mat.getRestitutie();

            b.createFixture(blokFD);
        } else if (kleur.equals("red")) {
            BodyDef driehoek = new BodyDef();
            driehoek.position.set(new Vec2(x, y));
            driehoek.type = BodyType.DYNAMIC;
            driehoek.angle = 0f;
            b = wereld.createBody(driehoek);
            float h = (float) Math.sqrt(3);
            float z = (float) ((float) h * Math.sqrt(3f / 4));
            PolygonShape triangle = new PolygonShape();
            triangle.set(new Vec2[]{
                        new Vec2(0f, h * 2f / 3f),
                        new Vec2(-z / 2f, -h / 3f),
                        new Vec2(z / 2f, -h / 3f)
                    }, 3);
            FixtureDef blokFD = new FixtureDef();
            blokFD.shape = triangle;
            blokFD.density = mat.getDichtheid();
            blokFD.friction = mat.getWrijving();
            blokFD.restitution = mat.getRestitutie();
            b.createFixture(blokFD);
            //Zwart en wit zijn dezelfde bozel
        } else {
            BodyDef vierkant = new BodyDef();
            vierkant.position.set(new Vec2(x, y));
            vierkant.type = BodyType.DYNAMIC;
            vierkant.angle = 0f;
            b = wereld.createBody(vierkant);
            PolygonShape blokShape = new PolygonShape();
            blokShape.setAsBox(1, 1);

            FixtureDef blokFD = new FixtureDef();
            blokFD.shape = blokShape;
            blokFD.density = mat.getDichtheid();
            blokFD.friction = mat.getWrijving();
            blokFD.restitution = mat.getRestitutie();

            b.createFixture(blokFD);
        }
        //Voeg de bozel toe aan de nodige lijsten en zet hem onactief
        b.setActive(false);
        model.addBozel(b, id);
        model.addBody(b, kleur);
        model.addSchade(b);
    }
}
