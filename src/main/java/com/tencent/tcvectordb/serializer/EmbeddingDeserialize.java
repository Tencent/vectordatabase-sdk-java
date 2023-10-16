package com.tencent.tcvectordb.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.tencent.tcvectordb.model.param.collection.Embedding;
import com.tencent.tcvectordb.model.param.enums.EmbeddingModelEnum;
import com.tencent.tcvectordb.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class EmbeddingDeserialize extends JsonDeserializer<Embedding> {

    @Override
    public Embedding deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JacksonException {
        String paramsStr = p.getCodec().readTree(p).toString();
        if (StringUtils.isBlank(paramsStr)) {
            return null;
        }
        JsonNode jsonNode = JsonUtils.parseToJsonNode(paramsStr);
        Embedding.Builder builder = Embedding.newBuilder();

        if (jsonNode.has(Embedding.FIELD_NAME)) {
            builder.withField(jsonNode.get(Embedding.FIELD_NAME).asText());
        }

        if (jsonNode.has(Embedding.VECTOR_FIELD_NAME)) {
            builder.withVectorField(jsonNode.get(Embedding.VECTOR_FIELD_NAME).asText());
        }

        if (jsonNode.has(Embedding.MODEL_NAME)) {
            String modelName = jsonNode.get(Embedding.MODEL_NAME).asText();
            builder.withModelName(modelName);
            EmbeddingModelEnum embeddingModelEnum = EmbeddingModelEnum.find(modelName);
            if (embeddingModelEnum != null) {
                builder.withModel(embeddingModelEnum);
            }
        }

        if (jsonNode.has(Embedding.STATUS_NAME)) {
            builder.withStatus(jsonNode.get(Embedding.STATUS_NAME).asText());
        }
        return builder.build();
    }
}
