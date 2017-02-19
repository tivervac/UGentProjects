package be.ugent.vopro1.converter.json.serialization;

import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.converter.json.JsonConverterFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.lang.funky.behavior.BranchingPoint;
import org.aikodi.lang.funky.behavior.LabeledBehavior;
import org.aikodi.lang.funky.usecase.AbstractFlow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Deserializer for {@link BranchingPoint}
 */
public class BranchingPointDeserializer extends JsonDeserializer<BranchingPoint> {

    public static final String NORMAL_BEHAVIOR = "normalBehavior";
    private JsonConverter converter;
    private Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Creates a new BranchingPointDeserializer
     */
    public BranchingPointDeserializer() {
        converter = JsonConverterFactory.getInstance();
    }

    @Override
    public BranchingPoint deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        try {
            String targetReference = node.get("targetReference").asText();

            // Normal behavior can be null if the branch is at the end of the flow
            LabeledBehavior flowStep = null;
            JsonNode normalNode = node.get(NORMAL_BEHAVIOR);
            if (normalNode != null) {
                flowStep = converter.convert(LabeledBehavior.class, node.get(NORMAL_BEHAVIOR).toString());
            }

            return new BranchingPoint<>(
                    new NameReference<>(targetReference, AbstractFlow.class),
                    flowStep);
        } catch (NullPointerException ex){
            String message = "Error deserializing Branching Point";

            logger.debug(message, ex);
            throw new JsonMappingException(message, ex);
        }
    }
}

