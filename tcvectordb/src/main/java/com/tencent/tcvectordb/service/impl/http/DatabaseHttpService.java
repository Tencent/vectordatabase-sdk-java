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
package com.tencent.tcvectordb.service.impl.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.tencent.tcvectordb.model.*;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.entity.DataBaseTypeRes;
import com.tencent.tcvectordb.model.param.entity.DataBaseType;
import com.tencent.tcvectordb.service.ApiPath;
import com.tencent.tcvectordb.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP service implementation for database operations.
 */
public class DatabaseHttpService extends BaseHttpService {

    /**
     * Create a new database instance.
     */
    public void createDatabase(Database database) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.DB_CREATE);
        this.post(url, database.toString(), false);
    }

    /**
     * Drop an existing database.
     */
    public void dropDatabase(Database database) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.DB_DROP);
        this.post(url, database.toString(), false);
    }

    /**
     * Create a new AI database instance.
     */
    public AffectRes createAIDatabase(AIDatabase aiDatabase) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_DB_CREATE);
        JsonNode jsonNode = this.post(url, aiDatabase.toString(), false);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    /**
     * Describe database information and type.
     */
    public DataBaseTypeRes describeDatabase(Database database) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.DB_DESCRIBE);
        String body = String.format("{\"database\":\"%s\"}",
                database.getDatabaseName());
        JsonNode jsonNode = this.post(url, body, false);
        return JsonUtils.parseObject(jsonNode.toString(), DataBaseTypeRes.class);
    }

    /**
     * Drop an existing AI database.
     */
    public AffectRes dropAIDatabase(AIDatabase aiDatabase) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.AI_DB_DROP);
        JsonNode jsonNode = this.post(url, aiDatabase.toString(), false);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    /**
     * List all available database names.
     */
    public List<String> listDatabases() {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.DB_LIST);
        JsonNode jsonNode = this.get(url, false);
        JsonNode dbsJson = jsonNode.get("databases");
        if (dbsJson == null) {
            return new ArrayList<>();
        }
        return JsonUtils.collectionDeserializer(dbsJson.toString(), new TypeReference<List<String>>(){});
    }

    /**
     * List all databases with their type information.
     */
    public Map<String, DataBaseType> listDatabaseInfos() {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.DB_LIST);
        JsonNode jsonNode = this.get(url, false);
        JsonNode dbsJson = jsonNode.get("info");
        if (dbsJson == null) {
            return new HashMap<>();
        }
        return JsonUtils.collectionDeserializer(dbsJson.toString(), new TypeReference<Map<String, DataBaseType>>() {
        });
    }
}