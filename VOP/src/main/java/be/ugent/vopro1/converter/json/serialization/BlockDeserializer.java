package be.ugent.vopro1.converter.json.serialization;

import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.converter.json.JsonConverterFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.aikodi.lang.funky.behavior.Block;
import org.aikodi.lang.funky.behavior.LabeledBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Serializer for {@link Block}
 */
public class BlockDeserializer extends JsonDeserializer<Block> {

    private Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public Block deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        JsonConverter conv = JsonConverterFactory.getInstance();

        try {
            Block b = new Block();

            String s = node.get("behaviors").toString();

            List<LabeledBehavior> children = conv.convertToList(s, LabeledBehavior.class);

            children.forEach(b::add);

            return b;
        } catch (NullPointerException ex){
            String message = "Error deserializing Block";

            logger.debug(message, ex);
            throw new JsonMappingException(message, ex);
        }
    }
}

