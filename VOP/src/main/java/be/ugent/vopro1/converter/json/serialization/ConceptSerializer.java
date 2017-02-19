package be.ugent.vopro1.converter.json.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.lang.funky.concept.Concept;

import java.io.IOException;

/**
 * Serializer for {@link Concept}
 */
public class ConceptSerializer extends JsonSerializer<Concept> {
    @Override
    public void serialize(Concept value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("name", value.name());
        gen.writeObjectField("definition", value.getDefinition());
        gen.writeArrayFieldStart("attributes");
        for (NameReference reference : value.getAttributeReferences()){
            gen.writeString(reference.name());
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
}
