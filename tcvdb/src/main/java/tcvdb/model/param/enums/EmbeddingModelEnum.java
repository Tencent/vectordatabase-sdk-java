package tcvdb.model.param.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EmbeddingModelEnum {
    BGE_BASE_ZH("bge-base-zh", 768),
    M3E_BASE("m3e-base", 768),
    TEXT2VEC_LARGE_CHINESE("text2vec-large-chinese", 1024),
    E5_LARGE_V2("e5-large-v2", 1024),
    MULTILINGUAL_E5_BASE("multilingual-e5-base", 768),
    BGE_LARGE_ZH("bge-large-zh", 1024);

    private final String modelName;
    private final int dimension;

    EmbeddingModelEnum(String modelName, int dimension) {
        this.modelName = modelName;
        this.dimension = dimension;
    }

    @JsonValue
    public String getModelName() {
        return modelName;
    }

    public int getDimension() {
        return dimension;
    }

    public static EmbeddingModelEnum find(String modelName) {
        EmbeddingModelEnum[] values = values();
        for (EmbeddingModelEnum value : values) {
            if (value.modelName.equals(modelName)) {
                return value;
            }
        }
        return null;
    }


}






