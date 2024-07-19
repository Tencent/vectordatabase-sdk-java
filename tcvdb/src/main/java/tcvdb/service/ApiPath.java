package tcvdb.service;

/**
 * Vector DB server API path
 */
public class ApiPath {
    public final static String DB_CREATE = "/v2/database/create";
    public final static String DB_DROP = "/v2/database/drop";
    public final static String DB_LIST = "/v2/database/list";
    public final static String COL_CREATE = "/v2/collection/create";
    public final static String COL_DROP = "/v2/collection/drop";
    public final static String COL_FLUSH = "/v2/collection/truncate";
    public final static String COL_LIST = "/v2/collection/list";
    public final static String SET_COL_ALIAS = "/v2/alias/set";
    public final static String DELETE_COL_ALIAS = "/v2/alias/delete";
    public final static String COL_DESCRIBE = "/v2/collection/describe";
    public final static String DOC_UPSERT = "/v2/document/upsert";
    public final static String DOC_QUERY = "/v2/document/query";
    public final static String DOC_SEARCH = "/v2/document/search";
    public final static String DOC_DELETE = "/v2/document/delete";
    public final static String DOC_UPDATE = "/v2/document/update";
    public final static String REBUILD_INDEX = "/v2/index/rebuild";
}
