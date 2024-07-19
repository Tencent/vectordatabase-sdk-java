package tcvdb.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.commons.lang3.tuple.Pair;
import tcvdb.exception.VectorDBException;
import tcvdb.model.Collection;
import tcvdb.serializer.CollectionDeserialize;
import tcvdb.serializer.CollectionSerialize;

import java.text.SimpleDateFormat;

import java.util.*;

public class JsonUtils {
    private JsonUtils() {
    }

    private static final String DATE_FORMAT_STR_ISO8601_CH = "yyyy-MM-dd HH:mm:ss";
    private static final ObjectMapper MAPPER = new ObjectMapper();


    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.setDateFormat(new SimpleDateFormat(DATE_FORMAT_STR_ISO8601_CH));

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Collection.class, new CollectionDeserialize());
        module.addSerializer(Collection.class, new CollectionSerialize());
        MAPPER.registerModule(module);
        MAPPER.configOverride(Pair.class)
                .setFormat(JsonFormat.Value.forShape(JsonFormat.Shape.ARRAY));
        MAPPER.registerModule(new PairListModule());
    }

    /**
     * deserialize string to object, it will ignore key when object doesn't have field
     *
     * @param jsonStr {@link String} of json format
     * @param clz     {@link Class} of <T>
     * @param <T>     <T>
     * @return return {@link T}
     */
    public static <T> T parseObject(String jsonStr, Class<T> clz) {
        try {
            return MAPPER.readValue(jsonStr, clz);
        } catch (JsonProcessingException e) {
            throw new VectorDBException(String.format("can't parse content=%s to %s", jsonStr, clz.getName()), e);
        }
    }

    /**
     * @param jsonStr json {@link String}
     * @param clz     {@link TypeReference}
     * @param <T>     result type
     * @return {@link T}
     * @deprecated recommend use {@link JsonUtils#parseObject(String, TypeReference)}, and this method will
     * remove in future
     */
    @Deprecated
    public static <T> T collectionDeserializer(String jsonStr, TypeReference<T> clz) {
        return parseObject(jsonStr, clz);
    }


    /**
     * deserialize json string to generic type
     *
     * @param jsonStr json string
     * @param clz     class
     * @param <T>     result type
     * @return generic type
     */
    public static <T> T parseObject(String jsonStr, TypeReference<T> clz) {
        try {
            return MAPPER.readValue(jsonStr, clz);
        } catch (JsonProcessingException e) {
            throw new VectorDBException(
                    String.format("can't parse content=%s to %s", jsonStr, clz.getType().toString()), e);
        }
    }

    /**
     * parse json string to JsonNode
     *
     * @param jsonStr json string
     * @return {@link JsonNode}
     */
    public static JsonNode parseToJsonNode(String jsonStr) {
        try {
            return MAPPER.readTree(jsonStr);
        } catch (JsonProcessingException e) {
            throw new VectorDBException(String.format("can't parse content=%s to JsonNode", jsonStr), e);
        }
    }

    /**
     * @param obj value for serialize to json string
     * @param <T> <T>
     * @return return type is {@link String}
     */
    public static <T> String toJsonString(T obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new VectorDBException(
                    String.format("can't serialize value=%s, type=%s", obj, obj.getClass().getName()), e);
        }
    }

    /**
     * convert java object to JsonNode
     *
     * @param obj java object
     * @return {@link JsonNode}
     */
    public static JsonNode toJsonNode(Object obj) {
        return MAPPER.valueToTree(obj);
    }

    /**
     * convert json node to java object
     *
     * @param jsonNode {@link JsonNode}
     * @param clz      class
     * @param <T>      result type
     * @return {@link T}
     */
    public static <T> T jsonNodeToObject(JsonNode jsonNode, Class<T> clz) {
        try {
            return MAPPER.treeToValue(jsonNode, clz);
        } catch (JsonProcessingException e) {
            throw new VectorDBException(
                    String.format("can't deserialize jsonNode content=%s to %s", jsonNode.toString(), clz.getName()),
                    e);
        }
    }

    public static List<Pair<Integer, Double>> parseList(JsonNode ele, Class<List> listClass, Class<Pair> pairClass) {
        try {
            TypeFactory typeFactory = MAPPER.getTypeFactory();
            CollectionType pairListType = typeFactory.constructCollectionType(listClass, pairClass);
            return MAPPER.readValue(ele.toString(), pairListType);
        }catch (JsonProcessingException e) {
            throw new VectorDBException(
                    String.format("can't deserialize jsonNode content=%s to %s", ele.toString(), "pair list"),
                    e);
        }
    }

    public static class PairListModule extends com.fasterxml.jackson.databind.module.SimpleModule {
        public PairListModule() {
            addDeserializer(TypeFactory.defaultInstance().constructCollectionType(List.class, Pair.class).getTypeHandler(), new PairListDeserializer());
        }
    }

    public static class PairListDeserializer extends com.fasterxml.jackson.databind.JsonDeserializer<List<Pair<Integer, Double>>> {
        @Override
        public List<Pair<Integer, Double>> deserialize(com.fasterxml.jackson.core.JsonParser jsonParser, com.fasterxml.jackson.databind.DeserializationContext deserializationContext) throws java.io.IOException {
            String[][] array = jsonParser.readValueAs(String[][].class);
            List<Pair<Integer, Double>> pairList = new java.util.ArrayList<>();
            for (String[] pairArray : array) {
                pairList.add(Pair.of(Integer.parseInt(pairArray[0]), Double.parseDouble(pairArray[1])));
            }
            return pairList;
        }
    }
}
