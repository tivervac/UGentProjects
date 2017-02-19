package be.ugent.vopro1.converter.json.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.lang.funky.behavior.Return;

import java.io.IOException;

/**
 * Serializer for {@link Return}
 */
public class ReturnSerializer extends JsonSerializer<Return> {
    @Override
    public void serialize(Return value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        NameReference executor = (NameReference) value.executingEntityReference();
        NameReference target = (NameReference) value.targetReference();

        gen.writeStringField("executor", executor.name());
        gen.writeObjectField("target", target.name());

        gen.writeEndObject();
    }
}
