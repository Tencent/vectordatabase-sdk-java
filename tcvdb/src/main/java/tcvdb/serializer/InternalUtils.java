package tcvdb.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import tcvdb.exception.VectorDBException;
import tcvdb.model.param.collection.ParamsSerializer;

import java.text.SimpleDateFormat;

class InternalUtils {
    private static final String DATE_FORMAT_STR_ISO8601_CH = "yyyy-MM-dd HH:mm:ss";
    private static final ObjectMapper MAPPER = new ObjectMapper();


    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.setDateFormat(new SimpleDateFormat(DATE_FORMAT_STR_ISO8601_CH));

        SimpleModule module = new SimpleModule();
        module.addDeserializer(ParamsSerializer.class, new ParamsDeserialize());
        MAPPER.registerModule(module);
    }


    static <T> T jsonNodeToObject(JsonNode value, Class<T> clz) {
        try {
            return MAPPER.treeToValue(value, clz);
        } catch (JsonProcessingException e) {
            throw new VectorDBException(String.format("JsonNode can't to %s type", clz.getName()), e);
        }
    }

    static JsonNode toJsonNode(Object o) {
        return MAPPER.valueToTree(o);
    }

}
