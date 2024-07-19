package tcvdb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GeneralParams implements Params {

    @JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
    private int ef;
    @JsonProperty(value = "nprobe")
    @JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
    private int NProbe;
    @JsonProperty(value = "radius")
    @JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
    private double Radius;

    private GeneralParams(Builder builder) {
        this.ef = builder.ef;
        this.NProbe = builder.NProbe;
        this.Radius = builder.Radius;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private int ef;
        private int NProbe;
        private double Radius;

        private Builder() {
        }


        public Builder withEf(int ef) {
            this.ef = ef;
            return this;
        }

        public Builder withNProbe(int NProbe) {
            this.NProbe = NProbe;
            return this;
        }

        public Builder withRadius(double Radius) {
            this.Radius = Radius;
            return this;
        }

        public GeneralParams build() {
            return new GeneralParams(this);
        }
    }
}
