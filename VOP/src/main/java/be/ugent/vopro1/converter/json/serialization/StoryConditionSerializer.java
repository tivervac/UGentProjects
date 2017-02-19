package be.ugent.vopro1.converter.json.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.aikodi.lang.funky.virtualmachine.StoryCondition;

import java.io.IOException;

/**
 * Serializer for {@link StoryCondition}
 */
public class StoryConditionSerializer extends JsonSerializer<StoryCondition> {
    @Override
    public void serialize(StoryCondition value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeStringField("type", "condition");

        gen.writeObjectField("data", value.getCondition().description());

        gen.writeEndObject();
    }

}