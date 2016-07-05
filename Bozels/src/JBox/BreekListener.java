package jbox;

import modellen.SpelModel;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author Titouan Vervack
 */
public class BreekListener implements ContactListener {

    private SpelModel model;
    private Body body1;
    private Body body2;

    public BreekListener(SpelModel model) {
        this.model = model;
    }

    @Override
    public void beginContact(Contact contact) {
        body1 = contact.getFixtureA().getBody();
        body2 = contact.getFixtureB().getBody();
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        //Voert krachten uit op de objecten
        try {
            //Als er rechtstreeks contact is tussen een doel en bozel verwijder het doel
            if (model.getDoelen().contains(body1) && model.getBozels().containsValue(body2)) {
                if (model.getMap().get((String) model.getBodies().get(body1)).getBreekbaar()) {
                    model.beschadig(body1, Float.MAX_VALUE);
                }
                return;
            }
            if (model.getDoelen().contains(body2) && model.getBozels().containsValue(body1)) {
                if (model.getMap().get((String) model.getBodies().get(body2)).getBreekbaar()) {
                    model.beschadig(body2, Float.MAX_VALUE);
                }
                return;
            }
            
            //Vermenigvuldig met 1000 anders is alles direct kapot
            if (model.getMap().get((String) model.getBodies().get(body1)).getBreekbaar()) {
                model.beschadig(body1, impulse.normalImpulses[0] / (model.getTijdsstap() * 1000));
            }
            if (model.getMap().get((String) model.getBodies().get(body2)).getBreekbaar()) {
                model.beschadig(body2, impulse.normalImpulses[1] / (model.getTijdsstap() * 1000));
            }
        } catch (Exception exp) {
            //Nullpointers
        }
    }
}