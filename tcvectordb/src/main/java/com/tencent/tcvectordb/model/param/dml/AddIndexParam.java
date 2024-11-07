package com.tencent.tcvectordb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.model.param.collection.IndexField;

import java.util.List;

/**
 * AddIndexParam
 * <ol>
 *  <li>The indexes parameter is used to pass the definition of new scalar indexes, including the following sub-parameters:
 *     <ol>
 *     <li>
 *     fieldName: Required. User-defined field name, following the naming rules of 1-128 characters.
 *                  It can only use alphanumeric characters, underscores _, hyphens -,
 *                  and must start with an alphabetic character.
 *     </li>
 *     <li>
 *     fieldType: Required. Specifies the field type, supporting uint64, string, and array.
 *     </li>
 *     <li>
 *     indexType: Required. Currently, only supports passing filter as the index type.
 *     </li>
 *     </ol>
 *  </li>
 *  <li>buildExistedData parameter: Specifies whether the field has existing data. By default, it is set to True.
 *                             If it is a batch request, hasData remains the same behavior.
 *     <ol>
 *     The buildExistedData parameter can have two values and their meanings are as follows:
 *          <li>buildExistedData=True: Default behavior, which requires scanning historical data and building the index.</li>
 *          <li>buildExistedData=False: User-specified, indicating that the field is a new field and does not require
 *                                  scanning historical data. It allows for quick index creation.</li>
 *     </ol>
 *  </li>
 * </ol>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddIndexParam {
    private boolean buildExistedData;
    private List<IndexField> indexes;

    public AddIndexParam() {
    }

    public AddIndexParam(Builder builder) {
        this.buildExistedData = builder.buildExistedData;
        this.indexes = builder.indexes;
    }

    public boolean getBuildExistedData() {
        return buildExistedData;
    }

    public List<IndexField> getIndexes() {
        return indexes;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private boolean buildExistedData = true;
        private List<IndexField> indexes;

        private Builder() {
        }

        public Builder withBuildExistedData(boolean buildExistedData) {
            this.buildExistedData = buildExistedData;
            return this;
        }

        public Builder withIndexes(List<IndexField> indexes) {
            this.indexes = indexes;
            return this;
        }

        public AddIndexParam build() {
            return new AddIndexParam(this);
        }
    }
}
