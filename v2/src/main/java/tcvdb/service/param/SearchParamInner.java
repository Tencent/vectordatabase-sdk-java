package tcvdb.service.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import tcvdb.model.param.dml.SearchParam;
import tcvdb.model.param.enums.ReadConsistencyEnum;
import tcvdb.utils.JsonUtils;

/**
 * Inner Search Param
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchParamInner {
    private String database;
    private String collection;
    private SearchParam search;
    private ReadConsistencyEnum readConsistency = ReadConsistencyEnum.EVENTUAL_CONSISTENCY;

    public SearchParamInner(String database, String collection, SearchParam search, ReadConsistencyEnum readConsistency) {
        this.database = database;
        this.collection = collection;
        this.search = search;
        this.readConsistency = readConsistency;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

    public SearchParam getSearch() {
        return search;
    }

    public ReadConsistencyEnum getReadConsistency() {
        return readConsistency;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
