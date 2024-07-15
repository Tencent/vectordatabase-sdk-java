package tcvdb.model.param.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AffectRes extends BaseRes {

    protected long affectedCount;

    public AffectRes() {
        super();
    }

    public long getAffectedCount() {
        return affectedCount;
    }


    @Override
    public String toString() {
        return "AffectRes{" +
                "affectedCount=" + affectedCount +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
