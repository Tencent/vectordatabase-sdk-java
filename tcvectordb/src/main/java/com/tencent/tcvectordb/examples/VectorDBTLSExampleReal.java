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

package com.tencent.tcvectordb.examples;

import com.tencent.tcvectordb.client.RPCVectorDBClient;
import com.tencent.tcvectordb.client.VectorDBClient;
import com.tencent.tcvectordb.model.Collection;
import com.tencent.tcvectordb.model.DocField;
import com.tencent.tcvectordb.model.Document;
import com.tencent.tcvectordb.model.param.collection.CreateCollectionParam;
import com.tencent.tcvectordb.model.param.collection.FilterIndex;
import com.tencent.tcvectordb.model.param.collection.HNSWParams;
import com.tencent.tcvectordb.model.param.collection.IndexType;
import com.tencent.tcvectordb.model.param.collection.FieldType;
import com.tencent.tcvectordb.model.param.collection.MetricType;
import com.tencent.tcvectordb.model.param.collection.VectorIndex;
import com.tencent.tcvectordb.model.param.database.ConnectParam;
import com.tencent.tcvectordb.model.param.database.TLSConfig;
import com.tencent.tcvectordb.model.param.dml.InsertParam;
import com.tencent.tcvectordb.model.param.dml.QueryParam;
import com.tencent.tcvectordb.model.param.dml.SearchByVectorParam;
import com.tencent.tcvectordb.model.param.dml.HNSWSearchParams;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;

import java.util.Arrays;
import java.util.List;

/**
 * VectorDB TLS/SSL Connection Example
 * <p>
 * This example demonstrates TLS/SSL connections to VectorDB server,
 * similar to the Go SDK's tls_demo/main.go.
 * </p>
 *
 * <h2>Usage Examples:</h2>
 * <p><b>1. Using CA certificate file path:</b></p>
 * <pre>{@code
 * TLSConfig tlsConfig = TLSConfig.newBuilder()
 *     .withCACert("/path/to/ca-cert.pem")
 *     .build();
 *
 * ConnectParam connectParam = ConnectParam.newBuilder()
 *     .withUrl("https://your-vectordb-server.com:443")
 *     .withUsername("root")
 *     .withKey("your-api-key")
 *     .withTLSConfig(tlsConfig)
 *     .build();
 *
 * // HTTP client
 * VectorDBClient httpClient = new VectorDBClient(connectParam, ReadConsistencyEnum.EVENTUAL_CONSISTENCY);
 *
 * // RPC client
 * VectorDBClient rpcClient = new RPCVectorDBClient(connectParam, ReadConsistencyEnum.EVENTUAL_CONSISTENCY);
 * }</pre>
 *
 * <p><b>2. Skip certificate verification (for testing only):</b></p>
 * <pre>{@code
 * TLSConfig tlsConfig = TLSConfig.newBuilder()
 *     .withInsecureSkipVerify(true)
 *     .build();
 *
 * ConnectParam connectParam = ConnectParam.newBuilder()
 *     .withUrl("https://your-vectordb-server.com:443")
 *     .withUsername("root")
 *     .withKey("your-api-key")
 *     .withTLSConfig(tlsConfig)
 *     .build();
 * }</pre>
 *
 * <h2>Test Scenarios:</h2>
 * <ul>
 *   <li>HTTP Client with CA certificate file</li>
 *   <li>RPC Client with CA certificate file</li>
 *   <li>HTTP/RPC Client with InsecureSkipVerify (testing only)</li>
 *   <li>Basic database operations: create database, collection, insert, query, search</li>
 * </ul>
 */
public class VectorDBTLSExampleReal {

    // ============== TLS 连接配置 ==============
    // VectorDB 实例 URL (HTTPS)
    private static final String VDB_URL = "xx";
    private static final String VDB_KEY = "xx";
    // CA 证书文件路径 (使用绝对路径确保在任何位置运行都能找到)
    private static final String CA_CERT_PATH = "xx";

    // 测试数据库和集合名称
    private static final String TEST_DB_NAME = "java_sdk_tls_test";
    private static final String TEST_COLL_NAME = "tls_test_collection";

    // 测试结果统计
    private static int passedTests = 0;
    private static int failedTests = 0;

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║         VectorDB TLS/SSL Real Connection Test Suite              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Configuration:");
        System.out.println("  URL: " + VDB_URL);
        System.out.println("  CA Certificate: " + CA_CERT_PATH);
        System.out.println();

        // 执行测试
        testHTTPClientWithTLS();
        testRPCClientWithTLS();
        testHTTPClientWithInsecureSkipVerify();
        testRPCClientWithInsecureSkipVerify();

        // 打印测试结果
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        Test Summary                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println("  ✓ Passed: " + passedTests);
        System.out.println("  ✗ Failed: " + failedTests);
        System.out.println("  Total:   " + (passedTests + failedTests));
        System.out.println();

        if (failedTests == 0) {
            System.out.println("All tests passed! TLS/SSL connections are working correctly.");
        } else {
            System.out.println("Some tests failed. Please check the error messages above.");
        }
    }

    /**
     * Test HTTP Client with TLS using CA certificate file
     */
    private static void testHTTPClientWithTLS() {
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("Test 1: HTTP Client with TLS (CA Certificate File)");
        System.out.println("═══════════════════════════════════════════════════════════════════");

        VectorDBClient client = null;
        try {
            // 创建 TLS 配置 - 使用 CA 证书文件
            System.out.println("\n[1.1] Creating HTTP client with TLS...");
            TLSConfig tlsConfig = TLSConfig.newBuilder()
                    .withCACert(CA_CERT_PATH)
                    .build();

            ConnectParam connectParam = ConnectParam.newBuilder()
                    .withUrl(VDB_URL)
                    .withUsername("root")
                    .withKey(VDB_KEY)
                    .withTimeout(30)
                    .withTLSConfig(tlsConfig)
                    .build();

            client = new VectorDBClient(connectParam, ReadConsistencyEnum.EVENTUAL_CONSISTENCY);
            System.out.println("  ✓ HTTP client created successfully");
            passedTests++;

            // 列出数据库
            System.out.println("\n[1.2] Listing databases...");
            List<String> databases = client.listDatabase();
            System.out.println("  ✓ Databases: " + databases);
            passedTests++;

            // 执行数据库操作
            performDatabaseOperations(client, "HTTP");

        } catch (Exception e) {
            System.out.println("  ✗ Test failed: " + e.getMessage());
            e.printStackTrace();
            failedTests++;
        } finally {
            if (client != null) {
                try {
                    cleanup(client);
                    client.close();
                } catch (Exception ignore) {}
            }
        }
    }

    /**
     * Test RPC Client with TLS using CA certificate file
     */
    private static void testRPCClientWithTLS() {
        System.out.println("\n═══════════════════════════════════════════════════════════════════");
        System.out.println("Test 2: RPC Client with TLS (CA Certificate File)");
        System.out.println("═══════════════════════════════════════════════════════════════════");

        VectorDBClient client = null;
        try {
            // 创建 TLS 配置 - 使用 CA 证书文件
            System.out.println("\n[2.1] Creating RPC client with TLS...");
            TLSConfig tlsConfig = TLSConfig.newBuilder()
                    .withCACert(CA_CERT_PATH)
                    .build();

            ConnectParam connectParam = ConnectParam.newBuilder()
                    .withUrl(VDB_URL)
                    .withUsername("root")
                    .withKey(VDB_KEY)
                    .withTimeout(30)
                    .withTLSConfig(tlsConfig)
                    .build();

            client = new RPCVectorDBClient(connectParam, ReadConsistencyEnum.EVENTUAL_CONSISTENCY);
            System.out.println("  ✓ RPC client created successfully");
            passedTests++;

            // 列出数据库
            System.out.println("\n[2.2] Listing databases...");
            List<String> databases = client.listDatabase();
            System.out.println("  ✓ Databases: " + databases);
            passedTests++;

            // 执行数据库操作
            performDatabaseOperations(client, "RPC");

        } catch (Exception e) {
            System.out.println("  ✗ Test failed: " + e.getMessage());
            e.printStackTrace();
            failedTests++;
        } finally {
            if (client != null) {
                try {
                    cleanup(client);
                    client.close();
                } catch (Exception ignore) {}
            }
        }
    }

    /**
     * Test HTTP Client with InsecureSkipVerify (for testing only)
     */
    private static void testHTTPClientWithInsecureSkipVerify() {
        System.out.println("\n═══════════════════════════════════════════════════════════════════");
        System.out.println("Test 3: HTTP Client with InsecureSkipVerify (Testing Only)");
        System.out.println("═══════════════════════════════════════════════════════════════════");

        VectorDBClient client = null;
        try {
            // 创建 TLS 配置 - 跳过证书验证（仅用于测试）
            System.out.println("\n[3.1] Creating HTTP client with InsecureSkipVerify...");
            TLSConfig tlsConfig = TLSConfig.newBuilder()
                    .withInsecureSkipVerify(true)
                    .build();

            ConnectParam connectParam = ConnectParam.newBuilder()
                    .withUrl(VDB_URL)
                    .withUsername("root")
                    .withKey(VDB_KEY)
                    .withTimeout(30)
                    .withTLSConfig(tlsConfig)
                    .build();

            client = new VectorDBClient(connectParam, ReadConsistencyEnum.EVENTUAL_CONSISTENCY);
            System.out.println("  ✓ HTTP client created successfully");
            passedTests++;

            // 列出数据库
            System.out.println("\n[3.2] Listing databases...");
            List<String> databases = client.listDatabase();
            System.out.println("  ✓ Databases: " + databases);
            passedTests++;

        } catch (Exception e) {
            System.out.println("  ✗ Test failed: " + e.getMessage());
            e.printStackTrace();
            failedTests++;
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (Exception ignore) {}
            }
        }
    }

    /**
     * Test RPC Client with InsecureSkipVerify (for testing only)
     */
    private static void testRPCClientWithInsecureSkipVerify() {
        System.out.println("\n═══════════════════════════════════════════════════════════════════");
        System.out.println("Test 4: RPC Client with InsecureSkipVerify (Testing Only)");
        System.out.println("═══════════════════════════════════════════════════════════════════");

        VectorDBClient client = null;
        try {
            // 创建 TLS 配置 - 跳过证书验证（仅用于测试）
            System.out.println("\n[4.1] Creating RPC client with InsecureSkipVerify...");
            TLSConfig tlsConfig = TLSConfig.newBuilder()
                    .withInsecureSkipVerify(true)
                    .build();

            ConnectParam connectParam = ConnectParam.newBuilder()
                    .withUrl(VDB_URL)
                    .withUsername("root")
                    .withKey(VDB_KEY)
                    .withTimeout(30)
                    .withTLSConfig(tlsConfig)
                    .build();

            client = new RPCVectorDBClient(connectParam, ReadConsistencyEnum.EVENTUAL_CONSISTENCY);
            System.out.println("  ✓ RPC client created successfully");
            passedTests++;

            // 列出数据库
            System.out.println("\n[4.2] Listing databases...");
            List<String> databases = client.listDatabase();
            System.out.println("  ✓ Databases: " + databases);
            passedTests++;

        } catch (Exception e) {
            System.out.println("  ✗ Test failed: " + e.getMessage());
            e.printStackTrace();
            failedTests++;
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (Exception ignore) {}
            }
        }
    }

    /**
     * Perform comprehensive database operations
     */
    private static void performDatabaseOperations(VectorDBClient client, String clientType) 
            throws InterruptedException {
        System.out.println("\n[" + clientType + "] Performing database operations...");

        // 清理旧数据
        cleanup(client);

        // 创建数据库
        System.out.println("\n  [a] Creating database: " + TEST_DB_NAME);
        client.createDatabaseIfNotExists(TEST_DB_NAME);
        System.out.println("    ✓ Database created");
        passedTests++;

        // 创建 collection
        System.out.println("\n  [b] Creating collection: " + TEST_COLL_NAME);
        CreateCollectionParam collectionParam = CreateCollectionParam.newBuilder()
                .withName(TEST_COLL_NAME)
                .withShardNum(1)
                .withReplicaNum(0)  // 免费实例 replicaNum 必须为 0
                .withDescription("TLS test collection")
                .addField(new FilterIndex("id", FieldType.String, IndexType.PRIMARY_KEY))
                .addField(new VectorIndex("vector", 3, IndexType.HNSW,
                        MetricType.COSINE, new HNSWParams(16, 200)))
                .addField(new FilterIndex("title", FieldType.String, IndexType.FILTER))
                .addField(new FilterIndex("content", FieldType.String, IndexType.FILTER))
                .build();
        client.createCollectionIfNotExists(TEST_DB_NAME, collectionParam);
        System.out.println("    ✓ Collection created");
        passedTests++;

        // 插入数据
        System.out.println("\n  [c] Inserting documents...");
        List<Document> documents = Arrays.asList(
                Document.newBuilder()
                        .withId("doc001")
                        .withVector(Arrays.asList(0.1, 0.2, 0.3))
                        .addDocField(new DocField("title", "TLS Test Document 1"))
                        .addDocField(new DocField("content", "This is a test document for TLS connection"))
                        .build(),
                Document.newBuilder()
                        .withId("doc002")
                        .withVector(Arrays.asList(0.2, 0.3, 0.4))
                        .addDocField(new DocField("title", "TLS Test Document 2"))
                        .addDocField(new DocField("content", "Another test document for TLS connection"))
                        .build(),
                Document.newBuilder()
                        .withId("doc003")
                        .withVector(Arrays.asList(0.3, 0.4, 0.5))
                        .addDocField(new DocField("title", "TLS Test Document 3"))
                        .addDocField(new DocField("content", "Third test document for TLS connection"))
                        .build()
        );
        InsertParam insertParam = InsertParam.newBuilder().withDocuments(documents).build();
        AffectRes insertRes = client.upsert(TEST_DB_NAME, TEST_COLL_NAME, insertParam);
        System.out.println("    ✓ Inserted " + insertRes.getAffectedCount() + " documents");
        passedTests++;

        // 等待数据同步
        Thread.sleep(2000);

        // 查询数据
        System.out.println("\n  [d] Querying documents...");
        QueryParam queryParam = QueryParam.newBuilder()
                .withDocumentIds(Arrays.asList("doc001", "doc002", "doc003"))
                .withRetrieveVector(true)
                .build();
        List<Document> queryResult = client.query(TEST_DB_NAME, TEST_COLL_NAME, queryParam);
        System.out.println("    ✓ Query returned " + queryResult.size() + " documents");
        for (Document doc : queryResult) {
            System.out.println("      - " + doc.getId() + ": " + doc.getObject("title"));
        }
        passedTests++;

        // 向量搜索
        System.out.println("\n  [e] Vector search...");
        SearchByVectorParam searchParam = SearchByVectorParam.newBuilder()
                .addVector(Arrays.asList(0.15, 0.25, 0.35))
                .withParams(new HNSWSearchParams(100))
                .withLimit(3)
                .build();
        List<List<Document>> searchResult = client.search(TEST_DB_NAME, TEST_COLL_NAME, searchParam);
        System.out.println("    ✓ Search returned results:");
        for (List<Document> docs : searchResult) {
            for (Document doc : docs) {
                System.out.println("      - " + doc.getId() + " (score: " + doc.getScore() + ")");
            }
        }
        passedTests++;

        // 描述 collection
        System.out.println("\n  [f] Describing collection...");
        Collection coll = client.describeCollection(TEST_DB_NAME, TEST_COLL_NAME);
        System.out.println("    ✓ Collection: " + coll.getCollection());
        System.out.println("      - Shard: " + coll.getShardNum());
        System.out.println("      - Replica: " + coll.getReplicaNum());
        passedTests++;
    }

    /**
     * Cleanup test database
     */
    private static void cleanup(VectorDBClient client) {
        try {
            client.dropDatabase(TEST_DB_NAME);
        } catch (Exception ignore) {
            // Database may not exist
        }
    }
}
