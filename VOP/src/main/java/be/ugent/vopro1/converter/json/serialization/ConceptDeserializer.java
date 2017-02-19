package be.ugent.vopro1.converter.json.serialization;

import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.converter.json.JsonConverterFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.aikodi.lang.funky.behavior.description.Description;
import org.aikodi.lang.funky.concept.Concept;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Deserializer for {@link org.aikodi.lang.funky.executors.Actor}
 */
public class ConceptDeserializer extends JsonDeserializer<Concept> {

    private Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public Concept deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        JsonConverter conv = JsonConverterFactory.getInstance();

        try {
            String name = node.get("name").asText();

            String attributesSting = node.get("attributes").toString();
            List<String> attributes = conv.convertToList(attributesSting, String.class);

            String definitionString = node.get("definition").toString();
            Description definition = conv.convert(Description.class, definitionString);

            Concept result = new Concept(name);
            result.setAttributes(attributes);
            result.setDefinition(definition);
            return result;
        } catch (NullPointerException ex) {
            String message = "Error deserializing Concept";

            logger.debug(message, ex);
            throw new JsonMappingException(message, ex);
        }

    }
}
