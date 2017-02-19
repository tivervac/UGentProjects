package be.ugent.vopro1.converter.json.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.aikodi.lang.funky.virtualmachine.StoryStep;

import java.io.IOException;

/**
 * Serializer for {@link StoryStep}
 */
public class StoryStepSerializer extends JsonSerializer<StoryStep> {
    @Override
    public void serialize(StoryStep value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeStringField("type", "step");

        gen.writeObjectField("data", value.getBehavior());

        gen.writeEndObject();
    }

}