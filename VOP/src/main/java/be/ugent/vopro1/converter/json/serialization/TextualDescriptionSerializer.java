package be.ugent.vopro1.converter.json.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.aikodi.lang.funky.behavior.description.TextualDescription;

import java.io.IOException;

/**
 * Serializer for {@link TextualDescription}
 */
public class TextualDescriptionSerializer extends JsonSerializer<TextualDescription> {
    @Override
    public void serialize(TextualDescription value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeStringField("type", "textual");

        gen.writeObjectFieldStart("data");
        gen.writeStringField("text", value.text());
        gen.writeEndObject();

        gen.writeEndObject();
    }

}
