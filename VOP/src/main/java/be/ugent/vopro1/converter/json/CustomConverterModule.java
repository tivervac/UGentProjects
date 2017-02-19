package be.ugent.vopro1.converter.json;

import be.ugent.vopro1.converter.json.serialization.*;
import be.ugent.vopro1.model.ProcessEntity;
import be.ugent.vopro1.model.UsecaseEntity;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
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
 * Provides a CustomConverterModule for Spring automatic serialisation
 */
public class CustomConverterModule extends SimpleModule {

    SimpleSerializers serializers;
    SimpleDeserializers deserializers;

    /**
     * Creates a new CustomConverterModule that allows Spring
     * to use our custom serializers for presenting results in the API.
     */
    public CustomConverterModule() {
        super("CustomConverterModule", new Version(1, 0, 0, null));
    }

    @Override
    public void setupModule(SetupContext context) {
        serializers = new SimpleSerializers();
        deserializers = new SimpleDeserializers();
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

        registerSerialize(ReferenceDescription.class, new ReferenceDescriptionSerializer());
        registerSerialize(TextualDescription.class, new TextualDescriptionSerializer());
        registerSerialize(TextualStep.class, new TextualStepSerializer());
        registerSerialize(BranchingPoint.class, new BranchingPointSerializer());
        registerSerialize(Return.class, new ReturnSerializer());
        registerSerialize(LabeledBehavior.class, labelSerializer);
        registerSerialize(Block.class, new BlockSerializer());
        registerSerialize(AbstractFlow.class, new AbstractFlowSerializer());
        registerSerialize(UsecaseEntity.class, new UsecaseSerializer());
        registerSerialize(Loop.class, new LoopSerializer());
        registerSerialize(StoryStep.class, new StoryStepSerializer());
        registerSerialize(StoryCondition.class, new StoryConditionSerializer());
        registerSerialize(Actor.class, new ActorSerializer());
        registerSerialize(Concept.class, new ConceptSerializer());
        registerSerialize(ProcessEntity.class, new ProcessSerializer());

        registerDeserialize(TextualStep.class, new TextualStepDeserializer());
        registerDeserialize(BranchingPoint.class, new BranchingPointDeserializer());
        registerDeserialize(Return.class, new ReturnDeserializer());
        registerDeserialize(LabeledBehavior.class, labelDeserializer);
        registerDeserialize(Block.class, new BlockDeserializer());
        registerDeserialize(AlternateFlow.class, new AlternateFlowDeserializer());
        registerDeserialize(ExceptionalFlow.class, new ExceptionalFlowDeserializer());
        registerDeserialize(UsecaseEntity.class, new UsecaseDeserializer());
        registerDeserialize(ReferenceDescription.class, new ReferenceDescriptionDeserializer());
        registerDeserialize(Description.class, descriptionDeserializer);
        registerDeserialize(TextualDescription.class, new TextualDescriptionDeserializer());
        registerDeserialize(Actor.class, new ActorDeserializer());
        registerDeserialize(Concept.class, new ConceptDeserializer());
        registerDeserialize(ProcessEntity.class, new ProcessDeserializer());

        context.addSerializers(serializers);
        context.addDeserializers(deserializers);
    }

    private void registerSerialize(Class c, JsonSerializer serializer){
        serializers.addSerializer(c, serializer);
    }

    private void registerDeserialize(Class c, JsonDeserializer deserializer){
        deserializers.addDeserializer(c, deserializer);
    }
}
