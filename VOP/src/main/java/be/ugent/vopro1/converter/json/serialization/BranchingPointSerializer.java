package be.ugent.vopro1.converter.json.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.lang.funky.behavior.Behavior;
import org.aikodi.lang.funky.behavior.BranchingPoint;

import java.io.IOException;

/**
 * Serializer for {@link BranchingPoint}
 */
public class BranchingPointSerializer extends JsonSerializer<BranchingPoint> {
    @Override
    public void serialize(BranchingPoint value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        NameReference target = (NameReference) value.targetReference();
        gen.writeStringField("targetReference", target.name());

        // Normal behavior can be null if the branch is at the end of the flow
        Behavior normalBehavior = value.normalBehavior();
        if(normalBehavior != null) {
            gen.writeObjectField("normalBehavior", normalBehavior);
        }

        gen.writeEndObject();
    }
}
