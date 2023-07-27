package com.tencentcloudapi.model.param.collection;

import com.tencentcloudapi.exception.ParamException;
import com.tencentcloudapi.model.Collection;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Create Collection Param
 * User: wlleiiwang
 * Date: 2023/7/26
 */
public class CreateCollectionParam extends Collection {

    private CreateCollectionParam(Builder builder) {
        this.collection = builder.name;
        this.replicaNum = builder.replicaNum;
        this.shardNum = builder.shardNum;
        this.description = builder.description;
        this.indexes = builder.indexes;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private int replicaNum;
        private int shardNum;
        private String description;
        private final List<IndexField> indexes;

        private Builder() {
            this.indexes = new ArrayList<>();
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withReplicaNum(int replicaNum) {
            this.replicaNum = replicaNum;
            return this;
        }

        public Builder withShardNum(int shardNum) {
            this.shardNum = shardNum;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder addField(IndexField field) {
            this.indexes.add(field);
            return this;
        }

        public CreateCollectionParam build() throws ParamException {
            if (StringUtils.isEmpty(this.name)) {
                throw new ParamException("ConnectParam error: name is null");
            }
            if (this.replicaNum == 0) {
                throw new ParamException("ConnectParam error: replicaNum is 0");
            }
            if (this.shardNum == 0) {
                throw new ParamException("ConnectParam error: shardNum is 0");
            }
            if (this.indexes.isEmpty()) {
                throw new ParamException("ConnectParam error: indexes is empty");
            }
            return new CreateCollectionParam(this);
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
