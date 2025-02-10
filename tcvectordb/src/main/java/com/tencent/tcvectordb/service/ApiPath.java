/*
 * Copyright (C) 2023 Tencent Cloud.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the vectordb-sdk-java), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
    public final static String DOC_HYBRID_SEARCH = "document/hybridSearch";
    public final static String DOC_DELETE = "document/delete";
    public final static String DOC_UPDATE = "document/update";
    public final static String DOC_COUNT = "document/count";
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
    public final static String AI_COLLECTION_UPLOADER_URL = "ai/collection/uploadUrl";
    public final static String AI_DOCUMENT_IMAGE_URL = "ai/document/getImageUrl";
    public final static String AI_GET_FILE = "ai/documentSet/get";
    public final static String AI_DOCUMENT_GET_CHUNKS = "ai/documentSet/getChunks";
    public final static String AI_ALIAS_SET = "ai/alias/set";
    public final static String AI_ALIAS_DELETE = "ai/alias/delete";
    public final static String AI_REBUILD_INDEX = "ai/index/rebuild";
    public final static String ADD_INDEX = "index/add";
    public final static String MODIFY_VECTOR_INDEX = "index/modifyVectorIndex";

    // user 相关api
    public final static String USER_CREATE = "user/create";
    public final static String USER_GRANT = "user/grant";
    public final static String USER_REVOKE = "user/revoke";
    public final static String USER_DESCRIBE = "user/describe";
    public final static String USER_LIST = "user/list";
    public final static String USER_DROP = "user/drop";
    public final static String USER_CHANGE_PASSWORD = "user/changePassword";


}
