package be.ugent.vopro1.story;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for the story generation
 */
public class StoryConfig {
    private Map<Class, Integer> map;

    /**
     * Creates a new StoryConfig object
     */
    public StoryConfig(){
        map = new HashMap<>();
    }

    /**
     * Sets the number of times a type of node gets traversed
     * @param c type of node
     * @param val number of times to traverse
     */
    public void addConfig(Class c, int val){
        if (c == null){
            throw new IllegalArgumentException();
        }

        map.put(c, val);
    }

    /**
     * Gets the number of times a type of node gets traversed
     * @param c type of nodes
     * @return number of times to traverse
     */
    public int getConfig(Class c){
        int result = 0;

        Integer val = map.get(c);
        if (val != null){
            result = val;
        }
        return result;
    }

}
