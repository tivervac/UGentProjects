package modellen;

import actions.PauzeAction;
import bozels.panels.SpelPanel;
import decor.MaterialenFactory;
import decor.Objecten;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import jbox.ExplosionRayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

/**
 *
 * @author Titouan Vervack
 */
public class SpelModel extends Model {

    //Map met engels naam en Objecten
    private HashMap<String, Objecten> map = new MaterialenFactory(this);
    //Map met bodies en engelse naam
    private HashMap<Body, String> bodies = new HashMap<Body, String>();
    //Map met ID een body van een bozel
    private HashMap<Integer, Body> bozels = new HashMap<Integer, Body>();
    //Map die de schade bijhoud van alle objecten
    private HashMap<Body, Float> schade = new HashMap<Body, Float>();
    //Tijddsstap
    private float dt = 1f / 60f;
    //Zwaartekracht
    private float fz = -9.81f;
    private float lanceerkracht = 50000f;
    private int snelheid = 8;
    //Kijkt of de slapende objecten gekleurd moeten worden
    private boolean awake = false;
    //Kijkt of de snelheid moet getekend worden
    private boolean drawSnelheid = false;
    //Kijkt of het zwaartepunt moet getekend worden
    private boolean drawZwaartepunt = false;
    //X-Positie van de eerste bozel
    private float katapultX;
    //Y-Positie van de eerste bozel
    private float katapultY;
    //Lijst die objecten bijhoud die moeten verwijderd worden
    private LinkedList<Body> destroy = new LinkedList<Body>();
    //Kijkt of de thread nog mag draaien
    private boolean run = false;
    //De huidige wereld
    private World world;
    //Kijkt of er moet gepauzeerd worden
    private boolean isPaused = true;
    //Kijkt of de volgende bozel op de plaats van de katapult mag staan
    private boolean first = true;
    //Main window
    private JFrame window;
    //Lijst die de doelen bijhoud
    private ArrayList<Body> doelen = new ArrayList<Body>();
    //Paneel waar alles wordt op getekend
    private SpelPanel panel;
    //Bestand dat het laatst is geopend, nodig voor te herstarten
    private File currentFile;
    //Pauzeknop
    private JToggleButton pauzeKnop;
    //Pauzecheckbox
    private JCheckBoxMenuItem pauzeBox;
    //Gezameljke action voor de pauzeknop en box
    private PauzeAction pauze = new PauzeAction(this);
    //Kijkt of de rays moeten getekend worden
    private boolean drawRays;
    //Onthoud raycast zodat je die kan verwijderen wanneer nodig
    private ExplosionRayCastCallback raycast;

    public SpelModel() {
    }

    public LinkedList getDestroy() {
        return destroy;
    }

    public HashMap getBodies() {
        return bodies;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public void addBody(Body b, String s) {
        bodies.put(b, s);
    }

    public HashMap<Integer, Body> getBozels() {
        return bozels;
    }

    public HashMap<String, Objecten> getMap() {
        return map;
    }

    public void addBozel(Body bozel, int id) {
        bozels.put(id, bozel);
        if (id == 1) {
            katapultX = bozel.getPosition().x;
            katapultY = bozel.getPosition().y;
        }
    }

    public void addSchade(Body b) {
        schade.put(b, 0f);
    }

    public float getTijdsstap() {
        return dt;
    }

    public void setTijdsstap(float dt) {
        this.dt = dt;
    }

    public float getFz() {
        return fz;
    }

    public void setFz(float fz) {
        this.fz = fz;
        world.setGravity(new Vec2(0, fz));
    }

    public int getSnelheid() {
        return snelheid;
    }

    public void setSnelheid(int snelheid) {
        this.snelheid = snelheid;
    }

    public float getLanceerkracht() {
        return lanceerkracht;
    }

    public void setLanceerkracht(float lanceerkracht) {
        this.lanceerkracht = lanceerkracht;
    }

    public boolean getAwake() {
        return awake;
    }

    public void setAwake(boolean awake) {
        this.awake = awake;
    }

    public void setDrawSnelheid(boolean drawSnelheid) {
        this.drawSnelheid = drawSnelheid;
    }

    public boolean getDrawSnelheid() {
        return drawSnelheid;
    }

    public void setDrawZwaartepunt(boolean drawZwaartepunt) {
        this.drawZwaartepunt = drawZwaartepunt;
    }

    public boolean getDrawZwaartepunt() {
        return drawZwaartepunt;
    }

    public float getKatapultX() {
        return katapultX;
    }

    public float getKatapultY() {
        return katapultY;
    }

    public void beschadig(Body b, float force) {
        if (bodies.get(b) != null) {
            Objecten materiaal = map.get(bodies.get(b));
            if (force > materiaal.getKrachtdrempel()) {
                schade.put(b, schade.get(b) + force);
                if (schade.get(b) > materiaal.getSterkte()) {
                    bodies.remove(b);
                    schade.remove(b);
                    destroy.push(b);
                }
            }
        }
    }

    public boolean getRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public void clearLists() {
        bodies.clear();
        bozels.clear();
        doelen.clear();
        schade.clear();
        destroy.clear();
        if (raycast != null) {
            raycast.getRays().clear();
        }
        fireStateChanged();
    }

    public boolean getPaused() {
        return isPaused;
    }

    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public boolean getFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public JFrame getWindow() {
        return window;
    }

    public void setWindow(JFrame window) {
        this.window = window;
    }

    public void addDoel(Body doel) {
        doelen.add(doel);
    }

    public ArrayList getDoelen() {
        return doelen;
    }

    public void setPanel(SpelPanel panel) {
        this.panel = panel;;
    }

    public SpelPanel getPanel() {
        return panel;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }

    public void setPauzeKnop(JToggleButton knop) {
        this.pauzeKnop = knop;
        this.pauzeKnop.setAction(pauze);
    }

    public JToggleButton getPauzeKnop() {
        return pauzeKnop;
    }

    public void setPauzeBox(JCheckBoxMenuItem box) {
        this.pauzeBox = box;
        this.pauzeBox.setAction(pauze);
    }

    public JCheckBoxMenuItem getPauzeBox() {
        return pauzeBox;
    }

    public boolean getDrawRays() {
        return drawRays;
    }

    public void setDrawRays(boolean drawRays) {
        this.drawRays = drawRays;
    }

    public void setRaycast(ExplosionRayCastCallback raycast) {
        this.raycast = raycast;
    }

    public ExplosionRayCastCallback getRaycast() {
        return raycast;
    }
}
