package tcvdb.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import tcvdb.model.param.dml.QueryParam;
import tcvdb.model.param.enums.ReadConsistencyEnum;
import tcvdb.utils.JsonUtils;

/**
 * Inner Query Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryParamInner {
    private String database;
    private String collection;
    private ReadConsistencyEnum readConsistency;
    private QueryParam query;

    public QueryParamInner(String database, String collection, QueryParam query, ReadConsistencyEnum readConsistency) {
        this.database = database;
        this.collection = collection;
        this.readConsistency = readConsistency;
        this.query = query;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

    public ReadConsistencyEnum getReadConsistency() {
        return readConsistency;
    }

    public QueryParam getQuery() {
        return query;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
