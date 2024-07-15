package tcvdb.model.param.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataBaseTypeRes extends BaseRes {
    @JsonProperty(value = "database")
    private DataBaseType databaseType;

    public DataBaseTypeRes() {
        super();
    }

    public DataBaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DataBaseType database) {
        this.databaseType = database;
    }

    @Override
    public String toString() {
        return "DataBaseTypeRes{" +
                "databaseType=" + databaseType +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", warning='" + warning + '\'' +
                ", count=" + count +
                ", requestId='" + requestId + '\'' +
                '}';
    }
}

