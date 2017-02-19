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
import org.aikodi.lang.funky.usecase.ExceptionalFlow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Deserializer for {@link ExceptionalFlow}
 */
public class ExceptionalFlowDeserializer extends JsonDeserializer<ExceptionalFlow> {

    private Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public ExceptionalFlow deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        JsonConverter conv = JsonConverterFactory.getInstance();

        try {
            String name = node.get("name").asText();
            String conditionString = node.get("condition").toString();
            String behaviorString = node.get("behavior").toString();


            Description cond = conv.convert(Description.class, conditionString);
            Block b = conv.convert(Block.class, behaviorString);

            return new ExceptionalFlow(name, new TextualCondition(cond), b);
        } catch (NullPointerException ex){
            String message = "Error deserializing Exceptional FLow";

            logger.debug(message, ex);
            throw new JsonMappingException(message, ex);
        }
    }
}

