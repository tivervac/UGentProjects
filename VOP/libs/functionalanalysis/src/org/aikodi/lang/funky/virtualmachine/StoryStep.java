package org.aikodi.lang.funky.virtualmachine;

import org.aikodi.lang.funky.behavior.AbstractStep;
import org.aikodi.lang.funky.behavior.Behavior;

public class StoryStep implements  StoryEntry {

    private AbstractStep behavior;

    public StoryStep(AbstractStep b){
        setBehavior(b);
    }

    @Override
    public String text() {
        return behavior.text();
    }

    public void setBehavior(AbstractStep b){
        if (b == null){
            throw new IllegalArgumentException("Behavior cannot be null");
        }
        this.behavior = b;
    }

    public Behavior getBehavior(){
        return behavior;
    }
}
