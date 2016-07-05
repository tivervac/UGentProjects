package jbox;

import java.util.ArrayList;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

/**
 *
 * @author Titouan Vervack
 */
public class ExplosionRayCastCallback implements RayCastCallback {

    //Overgenomen uit de PDF
    //Hou ook de rays bij zodat je de lijnen kan tekenen
    private Vec2 bomb;
    private Vec2 hitpoint;
    private Fixture closest;
    private float minf = 1.0f;
    private ArrayList<Lijntjes> lijnen = new ArrayList<Lijntjes>();

    public ExplosionRayCastCallback(Vec2 bomb) {
        this.bomb = bomb.clone();
    }

    @Override
    public float reportFixture(Fixture fix, Vec2 p, Vec2 normal, float f) {
        if (f < minf) {
            minf = f;
            closest = fix;
            hitpoint = p.clone();
        }
        return f;
    }

    public ArrayList<Lijntjes> getRays() {
        return lijnen;
    }

    public void addRay() {
        if (hitpoint != null) {
            lijnen.add(new Lijntjes(closest, hitpoint));
        }
        minf = 1.0f;
    }

    public void explode() {
        final float maxForce = 1000000f;
        for (Lijntjes r : lijnen) {
            Vec2 fDir = r.getBegin().sub(bomb);
            float dist = fDir.normalize();
            Vec2 force = fDir.mul(maxForce / (dist + 1));
            r.getFD().getBody().applyForce(force, r.getBegin());
            r.setKracht(force);
        }
    }
}