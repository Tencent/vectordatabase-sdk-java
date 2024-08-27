package com.tencent.tcvectordb.service;

/**
 * Vector DB server API path
 */
public class ApiPath {
    public final static String DB_CREATE = "database/create";
    public final static String DB_DROP = "database/drop";
    public final static String DB_DESCRIBE = "database/describe";
    public final static String DB_LIST = "database/list";
    public final static String COL_CREATE = "collection/create";
    public final static String COL_DROP = "collection/drop";
    public final static String COL_FLUSH = "collection/truncate";
    public final static String COL_LIST = "collection/list";
    public final static String SET_COL_ALIAS = "alias/set";
    public final static String DELETE_COL_ALIAS = "alias/delete";
    public final static String COL_DESCRIBE = "collection/describe";
    public final static String DOC_UPSERT = "document/upsert";
    public final static String DOC_QUERY = "document/query";
    public final static String DOC_SEARCH = "document/search";
    public final static String DOC_DELETE = "document/delete";
    public final static String DOC_UPDATE = "document/update";
    public final static String REBUILD_INDEX = "index/rebuild";
    public final static String AI_DB_CREATE = "ai/database/create";
    public final static String AI_DB_DROP = "ai/database/drop";
    public final static String AI_COL_CREATE = "ai/collectionView/create";
    public final static String AI_COL_DROP = "ai/collectionView/drop";
    public final static String AI_COL_LIST = "ai/collectionView/list";
    public final static String AI_COL_DESCRIBE = "ai/collectionView/describe";
    public final static String AI_COL_FLUSH = "ai/collectionView/truncate";
    public final static String AI_DOCUMENT_QUERY = "ai/documentSet/query";
    public final static String AI_DOCUMENT_SEARCH = "ai/documentSet/search";
    public final static String AI_DOCUMENT_DELETE = "ai/documentSet/delete";
    public final static String AI_DOCUMENT_UPDATE = "ai/documentSet/update";
    public final static String AI_DOCUMENT_UPLOADER_URL = "ai/documentSet/uploadUrl";
    public final static String AI_GET_FILE = "ai/documentSet/get";
    public final static String AI_DOCUMENT_GET_CHUNKS = "ai/documentSet/getChunks";
    public final static String AI_ALIAS_SET = "ai/alias/set";
    public final static String AI_ALIAS_DELETE = "ai/alias/delete";
    public final static String AI_REBUILD_INDEX = "ai/index/rebuild";
}
