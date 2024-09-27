package com.tencent.tcvectordb.model.param.collection;


/**
 * init TTLConfig when create a collection.
 * Param:
 *     enable: true or false. Whether to enable TTL, true enable, false disable.
 *     timeField(String): the field name of time.Specify the field name for storing timestamps,
 *                        which must be a filter index of type uint64. if not set, the document will not be expired.
 * Example:
 *    TTLConfig ttlConfig = TTLConfig.newBuilder().WithEnable(true).WithTimeField("time").build();
 */
public class TTLConfig {
    private boolean enable;
    private String timeField;

    public TTLConfig(Builder builder) {
        this.enable = builder.enable;
        this.timeField = builder.timeField;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getTimeField() {
        return timeField;
    }

    public void setTimeField(String timeField) {
        this.timeField = timeField;
    }

    //  建造者模式
    public static Builder newBuilder() {
        return new Builder();
    }
    public static class Builder {
        private boolean enable;
        private String timeField;

        private Builder() {
        }

        public Builder WithEnable(boolean enable)  {
            this.enable = enable;
            return this;
        }

        public Builder WithTimeField(String timeField) {
            this.timeField = timeField;
            return this;
        }

        public TTLConfig build() {
            return new TTLConfig(this);
        }
    }

}
