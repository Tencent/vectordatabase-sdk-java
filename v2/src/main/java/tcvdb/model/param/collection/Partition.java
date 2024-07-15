package tcvdb.model.param.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import tcvdb.model.param.enums.PartitionTypeEnum;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Partition {
    /**
     * partition number, range [1, 100]
     */
    private Integer partitionNum;
    /**
     * partition key, must be a scalar field, you can specify "id" field when no suitable scalar field.
     */
    private String partitionBy;
    /**
     * partition type, only supports 'hash' now
     */
    private PartitionTypeEnum partitionType;


    /**
     * default constructor for json deserialize, please use newBuilder() to create a new instance,
     */
    public Partition() {
    }

    private Partition(Builder builder) {
        this.partitionNum = builder.partitionNum;
        this.partitionBy = builder.partitionBy;
        this.partitionType = builder.partitionType;
    }


    public Integer getPartitionNum() {
        return partitionNum;
    }

    public String getPartitionBy() {
        return partitionBy;
    }

    public PartitionTypeEnum getPartitionType() {
        return partitionType;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private Integer partitionNum;
        private String partitionBy;
        private PartitionTypeEnum partitionType;

        private Builder() {
        }


        public Builder withPartitionNum(Integer partitionNum) {
            this.partitionNum = partitionNum;
            return this;
        }

        public Builder withPartitionBy(String partitionBy) {
            this.partitionBy = partitionBy;
            return this;
        }

        public Builder withPartitionType(PartitionTypeEnum partitionType) {
            this.partitionType = partitionType;
            return this;
        }

        public Partition build() {
            return new Partition(this);
        }
    }
}
