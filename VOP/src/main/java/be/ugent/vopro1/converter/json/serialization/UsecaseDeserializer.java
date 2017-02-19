package be.ugent.vopro1.converter.json.serialization;

import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.converter.json.JsonConverterFactory;
import be.ugent.vopro1.model.UsecaseEntity;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import org.aikodi.lang.funky.behavior.Block;
import org.aikodi.lang.funky.behavior.description.Description;
import org.aikodi.lang.funky.usecase.AlternateFlow;
import org.aikodi.lang.funky.usecase.ExceptionalFlow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Deserializer for {@link UsecaseEntity}
 */
public class UsecaseDeserializer extends JsonDeserializer<UsecaseEntity> {

    private Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public UsecaseEntity deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        JsonConverter conv = JsonConverterFactory.getInstance();

        try {
            String actorString = node.get("actors").toString();
            List<String> actors = conv.convertToList(actorString, String.class);

            String alternateString = node.get("alternativeFlows").toString();
            List<AlternateFlow> alternate = conv.convertToList(alternateString, AlternateFlow.class);

            String conceptString = node.get("concepts").toString();
            List<String> concepts = conv.convertToList(conceptString, String.class);

            String exceptionalString = node.get("exceptionalFlows").toString();
            List<ExceptionalFlow> exceptional = conv.convertToList(exceptionalString, ExceptionalFlow.class);

            String name = node.get("name").asText();

            String normalString = node.get("normalFlow").toString();
            Block normal = conv.convert(Block.class, normalString);

            String objectiveString = node.get("objective").toString();
            Description objective = conv.convert(Description.class, objectiveString);

            String precondString = node.get("preconditions").toString();
            List<Description> precond = conv.convertToList(precondString, Description.class);

            String postcondString = node.get("postconditions").toString();
            List<Description> postcond = conv.convertToList(postcondString, Description.class);

            UsecaseEntity uc = new UsecaseEntity(name);

            uc.setActors(actors);
            alternate.forEach(uc::addAlternateFlow);

            uc.setConcepts(concepts);
            exceptional.forEach(uc::addExceptionalFlow);

            uc.setNormalFlow(normal);
            uc.setObjective(objective);
            uc.setPreconditions(precond);
            uc.setPostconditions(postcond);

            return uc;
        } catch (NullPointerException ex){
            String message = "Error deserializing Usecase";

            logger.debug(message, ex);
            throw new JsonMappingException(message, ex);
        }
    }
}

