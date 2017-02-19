package be.ugent.vopro1.converter.json.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.aikodi.lang.funky.executors.Actor;

import java.io.IOException;

/**
 * Serializer for {@link org.aikodi.lang.funky.executors.Actor}
 */
public class ActorSerializer extends JsonSerializer<Actor> {
    @Override
    public void serialize(Actor value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("name", value.name());
        gen.writeEndObject();
    }
}
