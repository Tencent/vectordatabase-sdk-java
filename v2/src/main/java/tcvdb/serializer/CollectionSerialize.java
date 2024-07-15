package tcvdb.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import tcvdb.model.Collection;

import java.io.IOException;

public class CollectionSerialize extends JsonSerializer<Collection> {

    @Override
    public void serialize(Collection value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        if (value == null) {
            return;
        }

        JsonNode jsonNode = InternalUtils.toJsonNode(value);

        if (jsonNode.has(Constant.COLLECTION_PARTITION_KEY)) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            objectNode.remove(Constant.COLLECTION_SHARD_NUM_KEY);
        }
        gen.writeTree(jsonNode);

    }
}
