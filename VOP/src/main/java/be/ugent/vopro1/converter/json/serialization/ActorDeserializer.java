package be.ugent.vopro1.converter.json.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.aikodi.lang.funky.executors.Actor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Deserializer for {@link org.aikodi.lang.funky.executors.Actor}
 */
public class ActorDeserializer extends JsonDeserializer<Actor> {

    private Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public Actor deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        try {
            String name = node.get("name").asText();

            return new Actor(name);
        } catch (NullPointerException ex) {
            String message = "Error deserializing actor";

            logger.debug(message, ex);
            throw new JsonMappingException(message, ex);
        }

    }
}

