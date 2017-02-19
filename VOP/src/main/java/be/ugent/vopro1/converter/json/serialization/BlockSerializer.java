package be.ugent.vopro1.converter.json.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.aikodi.lang.funky.behavior.Behavior;
import org.aikodi.lang.funky.behavior.Block;

import java.io.IOException;

/**
 * Serializer for {@link Block}
 */
public class BlockSerializer extends JsonSerializer<Block> {
    @Override
    public void serialize(Block value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeArrayFieldStart("behaviors");
        for (Behavior ch : value.nearestDescendants(Behavior.class)){
            if (ch.children().size() > 0) {
                gen.writeObject(ch);
            }
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
}
