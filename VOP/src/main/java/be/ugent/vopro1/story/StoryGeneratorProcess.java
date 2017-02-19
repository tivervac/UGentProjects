package be.ugent.vopro1.story;

import org.aikodi.lang.funky.behavior.AbstractStep;
import org.aikodi.lang.funky.behavior.Behavior;
import org.aikodi.lang.funky.conditions.Condition;
import org.aikodi.lang.funky.virtualmachine.Story;
import org.aikodi.lang.funky.virtualmachine.StoryCondition;
import org.aikodi.lang.funky.virtualmachine.StoryStep;
import org.aikodi.lang.funky.virtualmachine.VirtualProcess;

import java.util.HashMap;
import java.util.Map;

/**
 * VirtualProcess implementation for Story generation
 */
public class StoryGeneratorProcess implements VirtualProcess {
    private StoryGenerator host;
    private StoryConfig config;
    private Map<Behavior, Integer> visited;
    private Story story;

    /**
     * Creates a new StoryGeneratorProcess
     * @param host StoryGenerator hosting this process
     * @param config Config to use
     */
    public StoryGeneratorProcess(StoryGenerator host, StoryConfig config) {
        this.host = host;
        this.config = config;
        visited = new HashMap<>();
        story = new Story();
    }

    @Override
    public void advance(Behavior behavior) {
        if (behavior == null){
            host.addStory(story);
            return;
        }

        VirtualProcess fork = clone();
        behavior.execute(fork);
    }

    @Override
    public boolean advance(Behavior behavior, Condition condition, Behavior source) {
        if (behavior == null){
            host.addStory(story);
            return false;
        }

        if (!visited.keySet().contains(source)){
            visited.put(source, config.getConfig(source.getClass()));
        }

        if (visited.get(source) > 0){
            visited.put(source, visited.get(source) - 1 );
            VirtualProcess fork = clone();
            fork.handleCondition(condition);
            behavior.execute(fork);
        } else {
            return false;
        }

        return true;
    }

    @Override
    public void handleCondition(Condition c) {
        story.addEntry(new StoryCondition(c));
    }

    @Override
    public void handleStep(AbstractStep s) {
        story.addEntry(new StoryStep(s));
    }

    @Override
    public void terminate() {
        host.addStory(story);
    }

    protected Story getStory(){
        return story;
    }

    protected void setStory(Story s){
        this.story = new Story(s);
    }

    protected Map<Behavior, Integer> getVisited(){
        return new HashMap<>(visited);
    }

    protected void setVisited(Map<Behavior, Integer> visited){
        this.visited = visited;
    }

    protected VirtualProcess clone(){
        StoryGeneratorProcess fork = new StoryGeneratorProcess(host, config);
        fork.setStory(this.getStory());
        fork.setVisited(this.getVisited());
        return fork;
    }
}
