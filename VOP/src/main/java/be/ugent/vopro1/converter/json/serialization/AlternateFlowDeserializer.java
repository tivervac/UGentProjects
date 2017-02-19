package be.ugent.vopro1.converter.json.serialization;

import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.converter.json.JsonConverterFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.aikodi.lang.funky.behavior.Block;
import org.aikodi.lang.funky.behavior.description.Description;
import org.aikodi.lang.funky.conditions.TextualCondition;
import org.aikodi.lang.funky.usecase.AbstractFlow;
import org.aikodi.lang.funky.usecase.AlternateFlow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Deserializer for {@link AlternateFlow}
 */
public class AlternateFlowDeserializer extends JsonDeserializer<AbstractFlow> {

    private Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public AbstractFlow deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        JsonConverter conv = JsonConverterFactory.getInstance();

        try {
            String name = node.get("name").asText();
            String conditionString = node.get("condition").toString();
            String behaviorString = node.get("behavior").toString();


            Description cond = conv.convert(Description.class, conditionString);
            Block b = conv.convert(Block.class, behaviorString);

            return new AlternateFlow(name, new TextualCondition(cond), b);
        } catch (NullPointerException ex) {
            String message = "Error deserializing Alternate Flow";

            logger.debug(message, ex);
            throw new JsonMappingException(message, ex);
        }

    }
}

