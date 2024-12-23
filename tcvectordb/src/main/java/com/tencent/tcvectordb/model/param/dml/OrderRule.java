package com.tencent.tcvectordb.model.param.dml;

import com.tencent.tcvectordb.model.param.enums.OrderEnum;

public class OrderRule {
    private String fieldName;
    private String direction;  // asc, desc default asc
    private OrderRule(Builder builder) {
        this.fieldName = builder.fieldName;
        this.direction = builder.direction;
    }

    public  static Builder newBuilder() {
        return new OrderRule.Builder();
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getDirection() {
        return direction;
    }

    //  建造者模式
    public static class Builder {
        private String fieldName;
        private String direction = "asc";

        public Builder() {
        }

        public Builder withFieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        public Builder withDirection(String direction) {
            this.direction = direction;
            return this;
        }

        public Builder withDirection(OrderEnum direction) {
            this.direction = direction.getOrder();
            return this;
        }

        public OrderRule build() {
            return new OrderRule(this);
        }
    }
}
