package decor;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import modellen.SpelModel;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author Titouan Vervack
 */
//Superklasse van alle decorstukken
public abstract class Objecten {

    private Color kleur;
    private float dichtheid;
    private float restitutie;
    private float wrijving;
    private float sterkte;
    private float krachtdrempel;
    private SpelModel model;
    private String material;
    private boolean breekbaar;

    //Laad alle eigenschappen van het materiaal in
    public Objecten(Color kleur, float dichtheid, float restitutie, float wrijving, float krachtdrempel, float sterkte, SpelModel model, String material, boolean breekbaar) {
        this.kleur = kleur;
        this.dichtheid = dichtheid;
        this.restitutie = restitutie;
        this.wrijving = wrijving;
        this.krachtdrempel = krachtdrempel;
        this.sterkte = sterkte;
        this.model = model;
        this.material = material;
        this.breekbaar = breekbaar;
    }

    public void setColor(Color kleur) {
        this.kleur = kleur;
    }

    public Color getColor() {
        return kleur;
    }

    public String getMaterial() {
        return material;
    }

    //Verander de dichtheid voor alle bodies van dit materiaal
    public void setDichtheid(float dichtheid) {
        this.dichtheid = dichtheid;
        HashMap<Body, String> bodies = model.getBodies();
        for (Iterator it = model.getBodies().keySet().iterator(); it.hasNext();) {
            Body blok = (Body) it.next();
            if (bodies.get(blok).equals(getMaterial())) {
                //Maak ze wakker anders wordt de dichtheid niet veranderd
                blok.setAwake(true);
                blok.getFixtureList().setDensity(dichtheid);
                blok.resetMassData();
            }
        }
    }

    public float getDichtheid() {
        return dichtheid;
    }

    //Verander de restitutie voor alle bodies van dit materiaal
    public void setRestitutie(float restitutie) {
        this.restitutie = restitutie;
        HashMap<Body, String> bodies = model.getBodies();
        for (Iterator it = model.getBodies().keySet().iterator(); it.hasNext();) {
            Body blok = (Body) it.next();
            if (bodies.get(blok).equals(getMaterial())) {
                blok.getFixtureList().setRestitution(restitutie);
            }
        }
    }

    public float getRestitutie() {
        return restitutie;
    }

    //Verander de wrijving voor alle bodies van dit materiaal
    public void setWrijving(float wrijving) {
        this.wrijving = wrijving;
        HashMap<Body, String> bodies = model.getBodies();
        for (Iterator it = model.getBodies().keySet().iterator(); it.hasNext();) {
            Body blok = (Body) it.next();
            if (bodies.get(blok).equals(getMaterial())) {
                blok.getFixtureList().setFriction(wrijving);
            }
        }
    }

    public float getWrijving() {
        return wrijving;
    }

    public float getSterkte() {
        return sterkte;
    }

    public void setSterkte(float sterkte) {
        this.sterkte = sterkte;
    }

    public void setKrachtdrempel(float krachtdrempel) {
        this.krachtdrempel = krachtdrempel;
    }

    public float getKrachtdrempel() {
        return krachtdrempel;
    }

    public boolean getBreekbaar() {
        return breekbaar;
    }

    public void setBreekbaar(boolean breekbaar) {
        this.breekbaar = breekbaar;
    }
}