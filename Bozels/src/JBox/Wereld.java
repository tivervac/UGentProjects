package jbox;

import actions.HerstartAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import modellen.SpelModel;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import reader.XMLParser;

/**
 *
 * @author Titouan Vervack
 */
public class Wereld implements Runnable {

    private World wereld;
    private JPanel panel;
    private SpelModel model;

    public Wereld(JPanel panel, SpelModel model) {
        wereld = new World(new Vec2(0f, model.getFz()), true);

        this.model = model;
        this.panel = panel;

        //Maak grond
        BodyDef grondBD = new BodyDef();
        grondBD.position.set(new Vec2(0f, 0f));
        grondBD.angle = 0f;
        grondBD.type = BodyType.STATIC;

        Body grond = wereld.createBody(grondBD);
        PolygonShape grondShape = new PolygonShape();
        grondShape.setAsBox(1024f, 0.5f);

        FixtureDef grondFD = new FixtureDef();
        grondFD.shape = grondShape;

        //Maak rechtermuur
        BodyDef rechtermuurBD = new BodyDef();
        rechtermuurBD.position.set(new Vec2(147.5f, 0f));
        rechtermuurBD.angle = 0f;
        rechtermuurBD.type = BodyType.STATIC;

        Body rechtermuur = wereld.createBody(rechtermuurBD);
        PolygonShape rechtermuurShape = new PolygonShape();
        rechtermuurShape.setAsBox(0.5f, 450f);

        FixtureDef rechtermuurFD = new FixtureDef();
        rechtermuurFD.shape = rechtermuurShape;

        //Maak linkermuur
        BodyDef linkermuurBD = new BodyDef();
        linkermuurBD.position.set(new Vec2(0f, 0f));
        linkermuurBD.angle = 0f;
        linkermuurBD.type = BodyType.STATIC;

        Body linkermuur = wereld.createBody(linkermuurBD);
        PolygonShape linkermuurShape = new PolygonShape();
        linkermuurShape.setAsBox(0.5f, 4500f);

        FixtureDef linkermuurFD = new FixtureDef();
        linkermuurFD.shape = linkermuurShape;

        //Maak plafond
        BodyDef plafondBD = new BodyDef();
        plafondBD.position.set(new Vec2(0f, 100f));
        plafondBD.angle = 0f;
        plafondBD.type = BodyType.STATIC;

        Body plafond = wereld.createBody(plafondBD);
        PolygonShape plafondShape = new PolygonShape();
        plafondShape.setAsBox(1024f, 0.5f);

        FixtureDef plafondFD = new FixtureDef();
        plafondFD.shape = plafondShape;

        rechtermuur.createFixture(rechtermuurFD);
        linkermuur.createFixture(linkermuurFD);
        plafond.createFixture(plafondFD);
        grond.createFixture(grondFD);

        //Sla de wereld op in het model en zet een contactlistener
        model.setWorld(wereld);
        wereld.setContactListener(new BreekListener(model));
    }

    @Override
    public void run() {
        //Kijk of de thread mag lopen
        while (model.getRun()) {
            //Verwijder te verwijderen objecten
            while (model.getDestroy().size() > 0) {
                Body b = (Body) model.getDestroy().remove(0);
                if (model.getDoelen().contains(b)) {
                    model.getDoelen().remove(b);
                }
                wereld.destroyBody(b);
            }
            //Kijk of je gewonnen hebt
            if (model.getDoelen().isEmpty()) {
                gewonnen();
                return;
            }
            //Kijk of je verloren hebt
            if (model.getBozels().isEmpty()) {
                verloren();
                return;
            }
            //Step als er niet gepauseerd is
            if (model.getPaused()) {
                try {
                    wereld.step(model.getTijdsstap(), model.getSnelheid(), 3);
                } catch (Exception exp) {
                }
            }
            panel.repaint();
            try {
                Thread.sleep((long) (model.getTijdsstap() * 1000));
            } catch (InterruptedException ex) {
            }
        }
    }

    private void verloren() {
        JOptionPane.showMessageDialog(model.getWindow(), "U heeft verloren!", "Verloren!", JOptionPane.ERROR_MESSAGE);
        clear();
    }

    private void gewonnen() {
        JOptionPane.showMessageDialog(model.getWindow(), "U heeft gewonnen!", "Gewonnen!", JOptionPane.WARNING_MESSAGE);
        clear();
    }

    private void clear() {
        //Maak alles leeg en zet het spel klaar voor een nieuw level
        model.setRun(false);
        model.setFirst(true);
        model.fireStateChanged();
        model.clearLists();
        //Zonder sleep is er niet genoeg tijd om te herstarten
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(HerstartAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Laad het level opnieuw in
        new XMLParser(model.getCurrentFile(), model, 0);
    }
}