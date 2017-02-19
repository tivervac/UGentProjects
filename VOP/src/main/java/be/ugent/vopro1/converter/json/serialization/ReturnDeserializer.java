package be.ugent.vopro1.converter.json.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.lang.funky.behavior.LabeledBehavior;
import org.aikodi.lang.funky.behavior.Return;
import org.aikodi.lang.funky.executors.ExecutingEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Deserializer for {@link Return}
 */
public class ReturnDeserializer extends JsonDeserializer<Return> {

    private Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public Return deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        try {
            String target = node.get("target").asText();
            String executor = node.get("executor").asText();

            return new Return(new NameReference<>(target, LabeledBehavior.class),
                    new NameReference<>(executor, ExecutingEntity.class));
        } catch (NullPointerException ex){
            String message = "Error deserializing Return";

            logger.debug(message, ex);
            throw new JsonMappingException(message, ex);
        }
    }
}
