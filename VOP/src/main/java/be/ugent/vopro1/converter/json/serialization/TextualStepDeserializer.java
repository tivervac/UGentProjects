package be.ugent.vopro1.converter.json.serialization;

import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.converter.json.JsonConverterFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.lang.funky.behavior.TextualStep;
import org.aikodi.lang.funky.behavior.description.Description;
import org.aikodi.lang.funky.executors.ExecutingEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

/**
 * Deserializer for {@link TextualStep}
 */
public class TextualStepDeserializer extends JsonDeserializer<TextualStep> {

    private Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public TextualStep deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        JsonConverter conv = JsonConverterFactory.getInstance();

        try {
            Optional<String> descriptionText = Optional.ofNullable(node.get("description").toString());
            Optional<String> executor = Optional.ofNullable(node.get("executor").asText());

            Description description = conv.convert(Description.class, descriptionText.orElse(""));

            return new TextualStep(description,
                    new NameReference<>(executor.orElse(""), ExecutingEntity.class));
        } catch (NullPointerException ex){
            String message = "Error deserializing Textual Step";

            logger.debug(message, ex);
            throw new JsonMappingException(message, ex);
        }
    }
}
