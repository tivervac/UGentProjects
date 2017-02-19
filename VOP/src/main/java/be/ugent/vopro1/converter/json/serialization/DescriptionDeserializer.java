package be.ugent.vopro1.converter.json.serialization;

import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.converter.json.JsonConverterFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.aikodi.lang.funky.behavior.description.Description;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Deserializer for {@link Description}
 */
public class DescriptionDeserializer extends JsonDeserializer<Description> {

    Map<String, Class<? extends Description>> accepted = new HashMap<>();
    private Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public Description deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        JsonConverter conv = JsonConverterFactory.getInstance();
        String textualType = "textual";

        try {
            String type;
            try {
                type = node.get("type").asText(textualType);
            } catch (NullPointerException ex) {
                type = textualType;
            }

            String dataString = node.get("data").toString();

            if (!accepted.containsKey(type)) {
                String message = "Type " + type + " is not allowed.";

                logger.trace(message);
                throw new IllegalArgumentException(message);
            }

            return conv.convert(accepted.get(type), dataString);
        } catch (NullPointerException ex){
            String message = "Error deserializing Description";

            logger.debug(message, ex);
            throw new JsonMappingException(message, ex);
        }
    }

    /**
     * Adds an accepted type for the deserialization
     * @param c Class to use
     * @param type Type string
     */
    public void addAcceptedType(Class<? extends Description> c, String type) {
        if (type == null || c == null){
            throw new IllegalArgumentException();
        }

        accepted.put(type, c);
    }
}
