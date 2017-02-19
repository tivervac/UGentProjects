package be.ugent.vopro1.converter.json.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.aikodi.lang.funky.usecase.AbstractFlow;

import java.io.IOException;

/**
 * Serializer for {@link AbstractFlow}
 */
public class AbstractFlowSerializer extends JsonSerializer<AbstractFlow> {
    @Override
    public void serialize(AbstractFlow value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeObjectField("condition", value.condition().description());

        gen.writeObjectField("behavior", value.behavior());

        gen.writeStringField("name", value.name());

        gen.writeEndObject();
    }
}
