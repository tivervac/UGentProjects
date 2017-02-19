package be.ugent.vopro1.story;

import org.aikodi.lang.funky.usecase.RegularUseCase;
import org.aikodi.lang.funky.virtualmachine.Story;

import java.util.ArrayList;
import java.util.List;

/**
 * StoryGenerator that generates stories
 */
public class StoryGenerator {
    private List<Story> stories;

    /**
     * Creates a new StoryGenerator
     */
    public StoryGenerator() {
        stories = new ArrayList<>();
    }

    /**
     * Called by the processes
     * Adds a story which will be returned
     * @param story finished story
     */
    public void addStory(Story story){
        stories.add(story);
    }

    /**
     * Generates all the stories from a usecase
     * @param uc usecase object to generate from
     * @param config sets the number of times a node type gets executed
     * @return list of stories from the usecase
     */
    public List<Story> getStories(RegularUseCase uc, StoryConfig config){
        stories = new ArrayList<>();
        new StoryGeneratorProcess(this, config).advance(uc.normalFlow());
        return stories;
    }
}
