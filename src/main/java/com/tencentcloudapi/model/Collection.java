package com.tencentcloudapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencentcloudapi.exception.ParamException;
import com.tencentcloudapi.model.param.collection.IndexField;
import com.tencentcloudapi.model.param.dml.*;
import com.tencentcloudapi.service.Stub;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * VectorDB Document
 * User: wlleiiwang
 * Date: 2023/7/24
 */
public class Collection {

    @JsonIgnore
    private Stub stub;
    private String database;
    private String collection;
    private int replicaNum = 0;
    private int shardNum = 0;
    private String description;
    private List<IndexField> indexes;
    private String createTime;

    public Collection(Stub stub, String database, String collection) {
        this.stub = stub;
        this.database = database;
        this.collection = collection;
    }

    private Collection(CreateBuilder builder) {
        this.database = builder.database;
        this.collection = builder.collection;
        this.replicaNum = builder.replicaNum;
        this.shardNum = builder.shardNum;
        this.description = builder.description;
        this.indexes = builder.indexes;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void upsert(List<Document> documents) {
        this.stub.upsertDocument(documents);
    }

    public List<Document> query(QueryParam param) {
        return this.stub.queryDocument(param);
    }

    public List<List<Document>> search(SearchParam param) {
        return this.stub.searchDocument(param);
    }

    public List<Document> searchById(SearchByIdParam param) {
        return this.stub.searchDocumentById(param);
    }

    public void delete(List<String> documentIds) {
        this.stub.deleteDocument(documentIds);
    }

    public static class CreateBuilder {
        private String database;
        private String collection;
        private int replicaNum;
        private int shardNum;
        private String description;
        private List<IndexField> indexes;

        private CreateBuilder() {
            this.indexes = new ArrayList<>();
        }

        public Collection.CreateBuilder withDatabase(String database) {
            this.database = database;
            return this;
        }

        public Collection.CreateBuilder withCollection(String collection) {
            this.collection = collection;
            return this;
        }

        public Collection.CreateBuilder withReplicaNum(int replicaNum) {
            this.replicaNum = replicaNum;
            return this;
        }

        public Collection.CreateBuilder withShardNum(int shardNum) {
            this.shardNum = shardNum;
            return this;
        }

        public Collection.CreateBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Collection.CreateBuilder addField(IndexField field) {
            this.indexes.add(field);
            return this;
        }

        public Collection build() throws ParamException {
            if (StringUtils.isEmpty(this.database)) {
                throw new ParamException("ConnectParam error: database is null");
            }
            if (StringUtils.isEmpty(this.collection)) {
                throw new ParamException("ConnectParam error: collection is null");
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
            return new Collection(this);
        }
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new ParamException(String.format(
                    "Create collection param error: %s", e.getMessage()));
        }
    }
}
