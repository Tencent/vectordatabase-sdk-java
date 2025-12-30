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
 * A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tencent.tcvectordb.service.impl.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.tencent.tcvectordb.model.*;
import com.tencent.tcvectordb.model.param.collection.CreateCollectionParam;
import com.tencent.tcvectordb.model.param.collectionView.CreateCollectionViewParam;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.enums.DataBaseTypeEnum;
import com.tencent.tcvectordb.service.ApiPath;
import com.tencent.tcvectordb.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * HTTP service implementation for collection operations.
 */
public class CollectionHttpService extends BaseHttpService {

    /**
     * Create a new collection with specified parameters.
     */
    public void createCollection(CreateCollectionParam param) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.COL_CREATE);
        this.post(url, param.toString(), false);
    }

    /**
     * Create a new AI collection view.
     */
    public void createCollectionView(CreateCollectionViewParam params) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_COL_CREATE);
        this.post(url, params.toString(), true);
    }

    /**
     * List all collections in the specified database.
     */
    public List<Collection> listCollections(String databaseName) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.COL_LIST);
        JsonNode jsonNode = this.post(url, String.format("{\"database\":\"%s\"}", databaseName), false);
        JsonNode closJson = jsonNode.get("collections");
        if (closJson == null) {
            return new ArrayList<>();
        }
        return JsonUtils.collectionDeserializer(closJson.toString(), new TypeReference<List<Collection>>() {
        });
    }

    /**
     * Get detailed information about a specific collection.
     */
    public Collection describeCollection(String databaseName, String collectionName) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.COL_DESCRIBE);
        String body = String.format("{\"database\":\"%s\",\"collection\":\"%s\"}",
                databaseName, collectionName);
        JsonNode jsonNode = this.post(url, body, false);
        JsonNode dbsJson = jsonNode.get("collection");
        if (dbsJson == null) {
            return null;
        }
        return JsonUtils.collectionDeserializer(dbsJson.toString(), new TypeReference<Collection>() {
        });
    }

    /**
     * Remove all documents from a collection without dropping the collection.
     */
    public AffectRes truncateCollection(String databaseName, String collectionName, DataBaseTypeEnum dbType) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.COL_FLUSH);
        String body = String.format("{\"database\":\"%s\",\"collection\":\"%s\"}",
                databaseName, collectionName);
        JsonNode jsonNode = this.post(url, body, false);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    /**
     * Remove all documents from a collection view without dropping it.
     */
    public AffectRes truncateCollectionView(String databaseName, String collectionName, DataBaseTypeEnum dbType) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_COL_FLUSH);
        String body = String.format("{\"database\":\"%s\",\"collectionView\":\"%s\"}",
                databaseName, collectionName);
        JsonNode jsonNode = this.post(url, body, true);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    /**
     * Drop an existing collection permanently.
     */
    public void dropCollection(String databaseName, String collectionName) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.COL_DROP);
        String body = String.format("{\"database\":\"%s\",\"collection\":\"%s\"}",
                databaseName, collectionName);
        this.post(url, body, false);
    }

    /**
     * Set an alias name for a collection.
     */
    public AffectRes setAlias(String databaseName, String collectionName, String aliasName) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.SET_COL_ALIAS);
        String body = String.format("{\"database\":\"%s\",\"collection\":\"%s\",\"alias\":\"%s\"}",
                databaseName, collectionName, aliasName);
        JsonNode jsonNode = this.post(url, body, false);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    /**
     * Delete an existing collection alias.
     */
    public AffectRes deleteAlias(String databaseName, String aliasName) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.DELETE_COL_ALIAS);
        String body = String.format("{\"database\":\"%s\",\"alias\":\"%s\"}",
                databaseName, aliasName);
        JsonNode jsonNode = this.post(url, body, false);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    /**
     * Set an alias name for an AI collection.
     */
    public AffectRes setAIAlias(String databaseName, String collectionName, String aliasName) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_ALIAS_SET);
        String body = String.format("{\"database\":\"%s\",\"collectionView\":\"%s\",\"alias\":\"%s\"}",
                databaseName, collectionName, aliasName);
        JsonNode jsonNode = this.post(url, body, true);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    /**
     * Delete an existing AI collection alias.
     */
    public AffectRes deleteAIAlias(String databaseName, String aliasName) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_ALIAS_DELETE);
        String body = String.format("{\"database\":\"%s\",\"alias\":\"%s\"}",
                databaseName, aliasName);
        JsonNode jsonNode = this.post(url, body, true);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    /**
     * List all collection views in the specified database.
     */
    public List<CollectionView> listCollectionView(String databaseName) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_COL_LIST);
        JsonNode jsonNode = this.post(url, String.format("{\"database\":\"%s\"}", databaseName), true);
        JsonNode closJson = jsonNode.get("collectionViews");
        if (closJson == null) {
            return new ArrayList<>();
        }
        return JsonUtils.collectionDeserializer(closJson.toString(), new TypeReference<List<CollectionView>>() {
        });
    }

    /**
     * Get detailed information about a specific collection view.
     */
    public CollectionView describeCollectionView(String databaseName, String collectionName) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_COL_DESCRIBE);
        String body = String.format("{\"database\":\"%s\",\"collectionView\":\"%s\"}",
                databaseName, collectionName);
        JsonNode jsonNode = this.post(url, body, true);
        JsonNode dbsJson = jsonNode.get("collectionView");
        if (dbsJson == null) {
            return null;
        }
        return JsonUtils.collectionDeserializer(dbsJson.toString(), new TypeReference<CollectionView>() {
        });
    }

    /**
     * Drop an existing collection view permanently.
     */
    public AffectRes dropCollectionView(String databaseName, String collectionName) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_COL_DROP);
        String body = String.format("{\"database\":\"%s\",\"collectionView\":\"%s\"}",
                databaseName, collectionName);
        JsonNode jsonNode = this.post(url, body, true);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }
}