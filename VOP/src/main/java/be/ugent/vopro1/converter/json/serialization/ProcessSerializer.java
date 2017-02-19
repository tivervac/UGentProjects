package be.ugent.vopro1.converter.json.serialization;

import be.ugent.vopro1.model.ProcessEntity;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Serializer for {@link ProcessEntity}
 */
public class ProcessSerializer extends JsonSerializer<ProcessEntity> {
    @Override
    public void serialize(ProcessEntity value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();

        gen.writeStringField("name", value.name());
        gen.writeNumberField("priority", value.getPriority());

        gen.writeArrayFieldStart("useCases");
        for (String s : value.getUseCases()){
            gen.writeString(s);
        }
        gen.writeEndArray();


        gen.writeEndObject();
    }
}
