package be.ugent.vopro1.converter.json;

import be.ugent.vopro1.converter.json.serialization.*;
import be.ugent.vopro1.model.ProcessEntity;
import be.ugent.vopro1.model.UsecaseEntity;
import be.ugent.vopro1.util.ApplicationContextProvider;
import org.aikodi.lang.funky.behavior.*;
import org.aikodi.lang.funky.behavior.description.Description;
import org.aikodi.lang.funky.behavior.description.ReferenceDescription;
import org.aikodi.lang.funky.behavior.description.TextualDescription;
import org.aikodi.lang.funky.concept.Concept;
import org.aikodi.lang.funky.executors.Actor;
import org.aikodi.lang.funky.usecase.AbstractFlow;
import org.aikodi.lang.funky.usecase.AlternateFlow;
import org.aikodi.lang.funky.usecase.ExceptionalFlow;
import org.aikodi.lang.funky.virtualmachine.StoryCondition;
import org.aikodi.lang.funky.virtualmachine.StoryStep;

/**
 * Holds a single instance of a JsonConverter.
 *
 * @see JsonConverter
 * @see JsonConverterImpl
 */
public class JsonConverterFactory {

    private static JsonConverter instance;

    /**
     * Sets a custom Converter, for dependency injection and tests.
     *
     * @param converter Converter to set and return in future
     * {@link #getInstance()} calls
     */
    public static void setInstance(JsonConverter converter) {
        instance = converter;
    }

    /**
     * Retrieves the Converter instance.
     * <p>
     * The default converter is based on the "converter" bean defined in
     * <code>application-context.xml</code>.
     *
     * @return Converter instance
     */
    public static JsonConverter getInstance() {
        if (instance == null) {
            instance = ApplicationContextProvider.getApplicationContext().getBean("converter", JsonConverter.class);
            initialize();
        }

        return instance;
    }

    /**
     * Initializes the loaded instance
     */
    public static void initialize() {
        String branchingPointType = "branchingPoint";
        String textualStepType = "textualStep";
        String returnType = "return";
        String blockType = "block";

        LabeledBehaviorSerializer labelSerializer = new LabeledBehaviorSerializer();
        labelSerializer.addAcceptedType(BranchingPoint.class, branchingPointType);
        labelSerializer.addAcceptedType(TextualStep.class, textualStepType);
        labelSerializer.addAcceptedType(Return.class, returnType);
        labelSerializer.addAcceptedType(Block.class, blockType);

        LabeledBehaviorDeserializer labelDeserializer = new LabeledBehaviorDeserializer();
        labelDeserializer.addAcceptedType(BranchingPoint.class, branchingPointType);
        labelDeserializer.addAcceptedType(TextualStep.class, textualStepType);
        labelDeserializer.addAcceptedType(Return.class, returnType);
        labelDeserializer.addAcceptedType(Block.class, blockType);

        DescriptionDeserializer descriptionDeserializer = new DescriptionDeserializer();
        descriptionDeserializer.addAcceptedType(TextualDescription.class, "textual");
        descriptionDeserializer.addAcceptedType(ReferenceDescription.class, "reference");

        instance.registerEntityType(Concept.class);
        instance.registerEntityType(UsecaseEntity.class);
        instance.registerEntityType(Actor.class);
        instance.registerEntityType(ProcessEntity.class);

        instance.registerSerialize(ReferenceDescription.class, new ReferenceDescriptionSerializer());
        instance.registerSerialize(TextualDescription.class, new TextualDescriptionSerializer());
        instance.registerSerialize(TextualStep.class, new TextualStepSerializer());
        instance.registerSerialize(BranchingPoint.class, new BranchingPointSerializer());
        instance.registerSerialize(Return.class, new ReturnSerializer());
        instance.registerSerialize(LabeledBehavior.class, labelSerializer);
        instance.registerSerialize(Block.class, new BlockSerializer());
        instance.registerSerialize(AbstractFlow.class, new AbstractFlowSerializer());
        instance.registerSerialize(UsecaseEntity.class, new UsecaseSerializer());
        instance.registerSerialize(Loop.class, new LoopSerializer());
        instance.registerSerialize(StoryStep.class, new StoryStepSerializer());
        instance.registerSerialize(StoryCondition.class, new StoryConditionSerializer());
        instance.registerSerialize(Actor.class, new ActorSerializer());
        instance.registerSerialize(Concept.class, new ConceptSerializer());
        instance.registerSerialize(ProcessEntity.class, new ProcessSerializer());

        instance.registerDeserialize(TextualStep.class, new TextualStepDeserializer());
        instance.registerDeserialize(BranchingPoint.class, new BranchingPointDeserializer());
        instance.registerDeserialize(Return.class, new ReturnDeserializer());
        instance.registerDeserialize(LabeledBehavior.class, labelDeserializer);
        instance.registerDeserialize(Block.class, new BlockDeserializer());
        instance.registerDeserialize(AlternateFlow.class, new AlternateFlowDeserializer());
        instance.registerDeserialize(ExceptionalFlow.class, new ExceptionalFlowDeserializer());
        instance.registerDeserialize(UsecaseEntity.class, new UsecaseDeserializer());
        instance.registerDeserialize(ReferenceDescription.class, new ReferenceDescriptionDeserializer());
        instance.registerDeserialize(Description.class, descriptionDeserializer);
        instance.registerDeserialize(TextualDescription.class, new TextualDescriptionDeserializer());
        instance.registerDeserialize(Actor.class, new ActorDeserializer());
        instance.registerDeserialize(Concept.class, new ConceptDeserializer());
        instance.registerDeserialize(ProcessEntity.class, new ProcessDeserializer());
    }
}
