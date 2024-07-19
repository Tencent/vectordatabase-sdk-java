package com.tencent.tcvectordb.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.param.collection.*;
import com.tencent.tcvectordb.utils.JsonUtils;

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
                + o.toString());
    }
}
