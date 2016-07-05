package jbox;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

/**
 *
 * @author Titouan Vervack
 */
//Hou de posities en de fixture bij nodig om de lijnen te tekenen
public class Lijntjes {
    
    private Vec2 begin;
    private Vec2 kracht;
    private Fixture fD;

    public Lijntjes(Fixture fD, Vec2 begin) {
        this.fD = fD;
        this.begin = begin;
    }

    public void setKracht(Vec2 kracht) {
        this.kracht = kracht.mul(0.001f);
        this.kracht = kracht.add(begin);
    }

    public Fixture getFD() {
        return fD;
    }

    public Vec2 getKracht() {
        return kracht;
    }

    public Vec2 getBegin() {
        return begin;
    }
}