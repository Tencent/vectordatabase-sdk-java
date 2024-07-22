package com.tencent.tcvdb.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.tencent.tcvdb.exception.VectorDBException;
import com.tencent.tcvdb.model.Collection;
import com.tencent.tcvdb.model.param.collection.Partition;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;

public class CollectionDeserialize extends JsonDeserializer<Collection> {

    private Field shardNumField = null;

    @Override
    public Collection deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JacksonException {

        // init shardNumField when first time
        ifNeedInit();

        Collection collection = InternalUtils.jsonNodeToObject(p.getCodec().readTree(p), Collection.class);
        Partition partition = collection.getPartition();

        if (Objects.nonNull(partition) &&
                !Objects.equals(0, partition.getPartitionNum()) &&
                !Objects.equals(partition.getPartitionNum(), collection.getShardNum())) {

            try {
                this.shardNumField.set(collection, partition.getPartitionNum());
            } catch (IllegalAccessException e) {
                throw new VectorDBException(
                        String.format("set shardNum to %s type instance failed", Collection.class.getName()), e);
            }

        }
        return collection;
    }

    private void ifNeedInit() {
        if (Objects.isNull(shardNumField)) {
            try {
                Field shardNum = Collection.class.getDeclaredField(Constant.COLLECTION_SHARD_NUM_KEY);
                shardNum.setAccessible(true);
                this.shardNumField = shardNum;
            } catch (NoSuchFieldException e) {
                throw new VectorDBException(String.format("unable to find %s field in Collection class",
                        Constant.COLLECTION_SHARD_NUM_KEY), e);
            }
        }
    }
}

