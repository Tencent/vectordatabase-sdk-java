package tcvdb.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import tcvdb.model.param.dml.DeleteParam;
import tcvdb.utils.JsonUtils;

/**
 * Inner Delete Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteParamInner {
    private String database;
    private String collection;
    private DeleteParam query;

    public DeleteParamInner(String database, String collection, DeleteParam query) {
        this.database = database;
        this.collection = collection;
        this.query = query;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

    public DeleteParam getQuery() {
        return query;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
