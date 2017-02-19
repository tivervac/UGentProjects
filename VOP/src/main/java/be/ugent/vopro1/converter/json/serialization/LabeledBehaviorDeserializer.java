package be.ugent.vopro1.converter.json.serialization;

import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.converter.json.JsonConverterFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.aikodi.lang.funky.behavior.Behavior;
import org.aikodi.lang.funky.behavior.LabeledBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Deserializer for {@link LabeledBehavior}
 */
public class LabeledBehaviorDeserializer extends JsonDeserializer<LabeledBehavior> {

    Map<String, Class<? extends Behavior>> accepted = new HashMap<>();
    private Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public LabeledBehavior deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        try {
            String childString = node.findValue("behavior").toString();

            // Label is kind of optional
            String label;
            JsonNode labelNode = node.get("name");
            if (labelNode == null) {
                label = "" + childString.hashCode(); // we javascript now boys
            } else {
                label = node.get("name").asText();
            }

            String type = node.get("type").asText();

            Behavior child = getBehavior(childString, type);

            return new LabeledBehavior(label, child);
        } catch (NullPointerException ex){
            String message = "Error deserializing Labeled Behavior";

            logger.debug(message);
            throw new JsonMappingException(message, ex);
        }
    }

    /**
     * Adds an accepted type for the deserialization
     * @param c Class to use
     * @param type Type string
     */
    public void addAcceptedType(Class<? extends Behavior> c, String type) {
        if (type == null || c == null){
            throw new IllegalArgumentException();
        }

        accepted.put(type, c);
    }

    private Behavior getBehavior(String s, String type) {
        JsonConverter conv = JsonConverterFactory.getInstance();

        if (!accepted.containsKey(type)){
            String message = "Type " + type + " is not allowed.";

            logger.trace(message);
            throw new IllegalArgumentException(message);
        }
        try {
            return conv.convert(accepted.get(type), s);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
