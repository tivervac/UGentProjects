package be.ugent.vopro1.converter.json.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.aikodi.lang.funky.behavior.Loop;

import java.io.IOException;

/**
 * Serializer for {@link Loop}
 */
public class LoopSerializer extends JsonSerializer<Loop> {
    @Override
    public void serialize(Loop value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeObjectField("data", value.body());
        gen.writeObjectField("condition", value.condition().description());

        gen.writeEndObject();
    }
}
