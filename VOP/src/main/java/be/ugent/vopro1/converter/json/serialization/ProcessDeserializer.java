package be.ugent.vopro1.converter.json.serialization;

import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.converter.json.JsonConverterFactory;
import be.ugent.vopro1.model.ProcessEntity;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Deserializer for {@link ProcessEntity}
 */
public class ProcessDeserializer extends JsonDeserializer<ProcessEntity> {

    private Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public ProcessEntity deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        JsonConverter conv = JsonConverterFactory.getInstance();

        try {
            String name = node.get("name").asText();
            int priority = node.get("priority").asInt(0);

            String useCaseString = node.get("useCases").toString();
            List<String> useCases = conv.convertToList(useCaseString, String.class);

            ProcessEntity entity = new ProcessEntity(name);
            entity.setPriority(priority);
            entity.setUseCases(useCases);
            return entity;
        } catch (NullPointerException ex) {
            String message = "Error deserializing Process";

            logger.debug(message, ex);
            throw new JsonMappingException(message, ex);
        }
    }
}
