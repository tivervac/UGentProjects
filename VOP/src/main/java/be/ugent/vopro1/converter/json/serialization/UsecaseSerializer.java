package be.ugent.vopro1.converter.json.serialization;

import be.ugent.vopro1.model.UsecaseEntity;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.aikodi.lang.funky.usecase.AbstractFlow;

import java.io.IOException;

/**
 * Serializer for {@link UsecaseEntity}
 */
public class UsecaseSerializer extends JsonSerializer<UsecaseEntity> {
    @Override
    public void serialize(UsecaseEntity value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeArrayFieldStart("actors");
        for (String s : value.getActors()){
            gen.writeString(s);
        }
        gen.writeEndArray();

        gen.writeArrayFieldStart("alternativeFlows");
        for (AbstractFlow flow : value.alternateFlows()){
            gen.writeObject(flow);
        }
        gen.writeEndArray();

        gen.writeArrayFieldStart("concepts");
        for (String s : value.getConcepts()){
            gen.writeString(s);
        }
        gen.writeEndArray();

        gen.writeArrayFieldStart("exceptionalFlows");
        for (AbstractFlow flow : value.exceptionalFlows()){
            gen.writeObject(flow);
        }
        gen.writeEndArray();

        gen.writeObjectField("normalFlow", value.normalFlow());

        gen.writeStringField("name", value.name());

        gen.writeObjectField("objective", value.getObjective());

        gen.writeObjectField("preconditions", value.getPreconditions());

        gen.writeObjectField("postconditions", value.getPostconditions());

        gen.writeEndObject();
    }
}
