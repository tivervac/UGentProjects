package be.ugent.vopro1.converter.json.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.aikodi.lang.funky.behavior.description.ReferenceDescription;

import java.io.IOException;

/**
 * Serializer for {@link ReferenceDescription}
 */
public class ReferenceDescriptionSerializer extends JsonSerializer<ReferenceDescription> {
    @Override
    public void serialize(ReferenceDescription value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        String referenceType = "reference";

        gen.writeStringField("type", referenceType);

        gen.writeObjectFieldStart("data");
        gen.writeObjectField("left", value.getDescriptionLeft());
        gen.writeStringField(referenceType, value.getReference().name());
        gen.writeObjectField("right", value.getDescriptionRight());
        gen.writeEndObject();

        gen.writeEndObject();
    }

}
