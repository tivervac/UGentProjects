package be.ugent.vopro1.converter.json.serialization;

import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.converter.json.JsonConverterFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.aikodi.chameleon.core.declaration.CommonDeclaration;
import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.lang.funky.behavior.description.Description;
import org.aikodi.lang.funky.behavior.description.ReferenceDescription;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Deserializer for {@link ReferenceDescription}
 */
public class ReferenceDescriptionDeserializer extends JsonDeserializer<ReferenceDescription> {

    private Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public ReferenceDescription deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        JsonConverter conv = JsonConverterFactory.getInstance();

        try {
            String leftString = node.get("left").toString();
            String referenceString = node.get("reference").asText();
            String rightString = node.get("right").toString();

            Description left = conv.convert(Description.class, leftString);
            NameReference reference = new NameReference<>(referenceString, CommonDeclaration.class);
            Description right = conv.convert(Description.class, rightString);

            return new ReferenceDescription(left, reference, right);
        } catch (NullPointerException ex){
            String message = "Error deserializing Reference Description";

            logger.debug(message, ex);
            throw new JsonMappingException(message, ex);
        }
    }
}

