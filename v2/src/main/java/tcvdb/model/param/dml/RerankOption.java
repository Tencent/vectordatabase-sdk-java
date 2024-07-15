package tcvdb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RerankOption {
    private Boolean enable;
    private Double expectRecallMultiples;

    public RerankOption(boolean enable, double expectRecallMultiples) {
        this.enable = enable;
        this.expectRecallMultiples = expectRecallMultiples;
    }

    public RerankOption(boolean enable) {
        this.enable = enable;
    }

    public Boolean isEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Double getExpectRecallMultiples() {
        return expectRecallMultiples;
    }

    public void setExpectRecallMultiples(double expectRecallMultiples) {
        this.expectRecallMultiples = expectRecallMultiples;
    }
}
