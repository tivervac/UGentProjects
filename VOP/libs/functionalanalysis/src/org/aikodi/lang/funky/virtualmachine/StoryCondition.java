package org.aikodi.lang.funky.virtualmachine;

import org.aikodi.lang.funky.conditions.Condition;

public class StoryCondition implements StoryEntry {

    private Condition condition;

    public StoryCondition(Condition c){
        setCondition(c);
    }

    public Condition getCondition(){
        return condition;
    }

    public void setCondition(Condition c){
        if (c == null){
            throw new IllegalArgumentException("Condition cannot be null");
        }
        this.condition = c;
    }

    @Override
    public String text() {
        return condition.description().text();
    }
}
