package be.ugent.vopro1.converter.json.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.aikodi.lang.funky.behavior.description.TextualDescription;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Deserializer for {@link TextualDescription}
 */
public class TextualDescriptionDeserializer extends JsonDeserializer<TextualDescription> {

    private Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public TextualDescription deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        try {
            String textString = node.get("text").asText();

            return new TextualDescription(textString);
        } catch (NullPointerException ex){
            String message = "Error deserializing Textual Description";

            logger.debug(message, ex);
            throw new JsonMappingException(message, ex);
        }
    }
}

