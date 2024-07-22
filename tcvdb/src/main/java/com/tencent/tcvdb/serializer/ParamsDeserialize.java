package com.tencent.tcvdb.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.tencent.tcvdb.model.param.collection.*;
import com.tencent.tcvdb.exception.VectorDBException;
import com.tencent.tcvdb.model.param.collection.*;
import com.tencent.tcvdb.utils.JsonUtils;

import java.io.IOException;

public class ParamsDeserialize extends JsonDeserializer<ParamsSerializer> {

    @Override
    public ParamsSerializer deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JacksonException {
        String paramsStr = p.getCodec().readTree(p).toString();
        Object o = p.currentValue();
        if (o instanceof IndexField) {
            IndexField indexField = (IndexField) o;
            if (indexField.isVectorField()) {
                ParamsSerializer params = null;
                switch (indexField.getIndexType()) {
                    case HNSW:
                        params = JsonUtils.parseObject(paramsStr, HNSWParams.class);
                        break;
                    case IVF_FLAT:
                        params = JsonUtils.parseObject(paramsStr, IVFFLATParams.class);
                        break;
                    case IVF_PQ:
                        params = JsonUtils.parseObject(paramsStr, IVFPQParams.class);
                        break;
                    case IVF_SQ8:
                        params = JsonUtils.parseObject(paramsStr, IVFSQ8Params.class);
                        break;
                }
                return params;
            }

        }
        throw new VectorDBException("deserialize IndexField exception, expect IndexField class, actually value is "
                + o.getClass().getName());
    }
}
