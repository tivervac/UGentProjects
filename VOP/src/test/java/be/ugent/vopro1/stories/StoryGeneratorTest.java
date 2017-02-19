package be.ugent.vopro1.stories;

import be.ugent.vopro1.story.StoryConfig;
import be.ugent.vopro1.story.StoryGenerator;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespace.RootNamespace;
import org.aikodi.chameleon.workspace.ProjectException;
import org.aikodi.lang.funky.behavior.BranchingPoint;
import org.aikodi.lang.funky.behavior.WhileLoop;
import org.aikodi.lang.funky.builder.ManualProjectBuilder;
import org.aikodi.lang.funky.usecase.RegularUseCase;
import org.aikodi.lang.funky.virtualmachine.Story;
import org.aikodi.lang.funky.virtualmachine.StoryEntry;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StoryGeneratorTest {

    private RootNamespace root;
    private ManualProjectBuilder builder;

    private ManualProjectBuilder builder() {
        return builder;
    }

    private StoryGenerator generator;

    private RegularUseCase usecase;

    @Before
    public void initialize(){
        try {
            builder = new ManualProjectBuilder("test");
        } catch (ProjectException e) {
            e.printStackTrace();
        }
        root = builder.view().namespace();

        builder().createInRootNamespace().subject("THE system");

        builder()
                .createInNamespace("actors")
                .actor("Bullock");

        builder()
                .createInNamespace("accounting")
                .addDemandImport("actors")
                .usecase("MoreHelloAccountant")
                .primaryActor("Bullock")
                .precondition().text("Sandra is lonely")
                .postcondition().text("Hello has been said.")
                .normalFlow()
                .label("q").step("Shit", "Bullock")
                //.alternate("alternative")
                .doWhile()
                .condition().text("I am not tired")
                .openBlock()
                .step("Say Hello.","Bullock")
                .endBlock()
                .endBlock()
                .alternateFlow("alternative")
                .when().text("The accountant is not present.")
                .execute()
                .step("Say goodbye", "Bullock")
                .returnTo("q", "THE system");

        String user = "Client";
        String system = "THE system";
        String sandra = "actors.Bullock";
        builder()
                .createInRootNamespace()
                .usecase("useless case")
                .primaryActor(user)
                .primaryActor(sandra)
                .precondition().text("The sky hasn't fallen.")
                .postcondition().text("The sky will be falling")
                .normalFlow()
                .step("Hello",user)
                .step("The user hits the screen.",user)
                .openBlock()
                .label("x").step("Nested step",system)
                .alternate("Alternative").label("y")
                .step("More nesting.",user)
                .endBlock()
                .step("After nesting",system)
                .doWhile()
                .condition().text("I am not tired")
                .openBlock()
                .step("Run.",user)
                .endBlock()
                .step("float in space", sandra)
                .endBlock()
                .alternateFlow("Alternative")
                .when().text("The end is near")
                .execute()
                .step("Do something else",user)
                .returnTo("x",system)
                .endBlock();

        try {
            usecase = root.find("useless case", RegularUseCase.class);
        } catch (LookupException e) {
            e.printStackTrace();
        }

        generator = new StoryGenerator();
    }

    @Test
    public void testOutput(){
        StoryConfig config = new StoryConfig();
        config.addConfig(BranchingPoint.class, 2);
        config.addConfig(WhileLoop.class, 2);

        List<List<String>> expected = getExpectedResults();

        List<Integer> expectedHashes = new ArrayList<>();
        for (List<String> entry : expected){
            expectedHashes.add(entry.hashCode());
        }

        List<Story> stories = generator.getStories(usecase, config);

        assertEquals(stories.size(), expected.size());

        for (Story path : stories){
            assertTrue(expectedHashes.contains(getHash(path)));
        }
    }

    private List<List<String>> getExpectedResults(){
        List<List<String>> result = new ArrayList<>();

        List<String> temp = new ArrayList<>();
        temp.add("Hello");
        temp.add("The user hits the screen.");
        temp.add("Nested step");
        temp.add("More nesting.");
        temp.add("After nesting");
        temp.add("I am not tired");
        temp.add("Run.");
        temp.add("float in space");
        result.add(temp);

        temp = new ArrayList<>();
        temp.add("Hello");
        temp.add("The user hits the screen.");
        temp.add("Nested step");
        temp.add("More nesting.");
        temp.add("After nesting");
        temp.add("I am not tired");
        temp.add("Run.");
        temp.add("I am not tired");
        temp.add("Run.");
        temp.add("float in space");
        result.add(temp);

        temp = new ArrayList<>();
        temp.add("Hello");
        temp.add("The user hits the screen.");
        temp.add("Nested step");
        temp.add("The end is near");
        temp.add("Do something else");
        temp.add("Nested step");
        temp.add("More nesting.");
        temp.add("After nesting");
        temp.add("I am not tired");
        temp.add("Run.");
        temp.add("float in space");
        result.add(temp);

        temp = new ArrayList<>();
        temp.add("Hello");
        temp.add("The user hits the screen.");
        temp.add("Nested step");
        temp.add("The end is near");
        temp.add("Do something else");
        temp.add("Nested step");
        temp.add("More nesting.");
        temp.add("After nesting");
        temp.add("I am not tired");
        temp.add("Run.");
        temp.add("I am not tired");
        temp.add("Run.");
        temp.add("float in space");
        result.add(temp);

        temp = new ArrayList<>();
        temp.add("Hello");
        temp.add("The user hits the screen.");
        temp.add("Nested step");
        temp.add("The end is near");
        temp.add("Do something else");
        temp.add("Nested step");
        temp.add("The end is near");
        temp.add("Do something else");
        temp.add("Nested step");
        temp.add("More nesting.");
        temp.add("After nesting");
        temp.add("I am not tired");
        temp.add("Run.");
        temp.add("float in space");
        result.add(temp);

        temp = new ArrayList<>();
        temp.add("Hello");
        temp.add("The user hits the screen.");
        temp.add("Nested step");
        temp.add("The end is near");
        temp.add("Do something else");
        temp.add("Nested step");
        temp.add("The end is near");
        temp.add("Do something else");
        temp.add("Nested step");
        temp.add("More nesting.");
        temp.add("After nesting");
        temp.add("I am not tired");
        temp.add("Run.");
        temp.add("I am not tired");
        temp.add("Run.");
        temp.add("float in space");
        result.add(temp);

        return result;
    }


    private int getHash(Story story){
        long result = 0;

        List<StoryEntry> values = story.getEntries();

        List<String> contents = new ArrayList<>();

        for (StoryEntry entry : values){
            contents.add(entry.text());
        }

        return contents.hashCode();
    }
}
