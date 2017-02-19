package be.ugent.vopro1.converter.json.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.aikodi.lang.funky.behavior.Behavior;
import org.aikodi.lang.funky.behavior.LabeledBehavior;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Serializer for {@link LabeledBehavior}
 */
public class LabeledBehaviorSerializer extends JsonSerializer<LabeledBehavior> {
    Map<Class<? extends Behavior>, String> accepted;

    /**
     * Creates a new LabeledBehaviorSerializer
     */
    public LabeledBehaviorSerializer(){
        accepted = new HashMap<>();
    }

    @Override
    public void serialize(LabeledBehavior value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        Behavior behavior = value.behavior();

        gen.writeStringField("name", value.signature().name());
        gen.writeStringField("type", getNameTag(value));
        gen.writeObjectField("behavior", behavior);

        gen.writeEndObject();
    }

    private String getNameTag(LabeledBehavior value) {
        String result = accepted.get(value.behavior().getClass());
        if (result == null){
            throw new IllegalArgumentException("Class " + value.behavior().getClass() + " is not allowed.");
        }

        return result;
    }

    /**
     * Adds an accepted type for the deserialization
     * @param c Class to use
     * @param type Type string
     */
    public void addAcceptedType(Class<? extends Behavior> c, String type) {
        if (type == null || c == null){
            throw new IllegalArgumentException();
        }

        accepted.put(c, type);
    }
}
