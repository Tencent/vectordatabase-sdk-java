package com.tencent.tcvectordb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.model.param.collection.IndexField;
import com.tencent.tcvectordb.model.param.collection.VectorIndex;

import java.util.ArrayList;
import java.util.List;

/**
 * ModifyVectorIndexParam
 * Currently, this interface is only for dense vectors, i.e. vector
 * Supports re-specifying vector index parameters, HNSW supports re-specifying M and efConstruction, IVF supports re-specifying nlist (IVF_PQ supports re-specifying M and nlist)
 * Supports re-specifying similarity calculation method
 * The new configuration after the vector index is modified is defined by the field vectorIndexes
 * After adjusting the parameters, this interface will trigger a rebuild, and the rebuild rules are specified by the field rebuildRules
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModifyVectorIndexParam {
    private RebuildIndexParam rebuildRules;
    private List<VectorIndex> vectorIndexes;

    public ModifyVectorIndexParam() {
    }

    public ModifyVectorIndexParam(Builder builder) {
        this.rebuildRules = builder.rebuildRules;
        this.vectorIndexes = builder.vectorIndexes;
    }

    public RebuildIndexParam getRebuildRules() {
        return rebuildRules;
    }

    public List<VectorIndex> getVectorIndexes() {
        return vectorIndexes;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private RebuildIndexParam rebuildRules;
        private List<VectorIndex> vectorIndexes;

        private Builder() {
            vectorIndexes = new ArrayList<>();
        }

        public Builder withRebuildRules(RebuildIndexParam rebuildRules) {
            this.rebuildRules = rebuildRules;
            return this;
        }

        public Builder withVectorIndex(VectorIndex vectorIndex) {
            this.vectorIndexes.add(vectorIndex);
            return this;
        }

        public ModifyVectorIndexParam build() {
            return new ModifyVectorIndexParam(this);
        }
    }
}
