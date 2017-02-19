package be.ugent.vopro1.converter.json.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.lang.funky.behavior.TextualStep;
import org.aikodi.lang.funky.executors.ExecutingEntity;

import java.io.IOException;

/**
 * Serializer for {@link TextualStep}
 */
public class TextualStepSerializer extends JsonSerializer<TextualStep> {
    @Override
    public void serialize(TextualStep value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        NameReference<ExecutingEntity> executor = (NameReference) value.executingEntityReference();
        gen.writeStringField("executor", executor.name());

        gen.writeObjectField("description", value.description());

        gen.writeEndObject();
    }
}
