package com.tencent.tcvdb;

import com.tencent.tcvdb.client.VectorDBClient;
import com.tencent.tcvdb.model.Collection;
import com.tencent.tcvdb.model.Database;
import com.tencent.tcvdb.model.param.collection.CreateCollectionParam;
import com.tencent.tcvdb.model.param.database.ConnectParam;
import com.tencent.tcvdb.model.param.entity.AffectRes;
import com.tencent.tcvdb.model.param.enums.ReadConsistencyEnum;

import java.util.List;

public class CommonService {
    private CommonService() {
    }


    /**
     * init connect parameter
     *
     * @return {@link ConnectParam}
     */
    private static ConnectParam initConnectParam() {
        String vdbURL = "http://9.135.180.240:9500";
        String vdbKey = "OFxd1WfoOejNYK20ZEAMdRnRsJI3HErLrRgOyS6i";
        System.out.println("\tvdb_url: " + vdbURL);
        System.out.println("\tvdb_key: " + vdbKey);
        return ConnectParam.newBuilder()
                .withUrl(vdbURL)
                .withUsername("root")
                .withKey(vdbKey)
                .withTimeout(30)
                .build();
    }

    /**
     * init vector database client
     *
     * @return {@link VectorDBClient}
     */
    public static VectorDBClient initClient() {
        return new VectorDBClient(initConnectParam(), ReadConsistencyEnum.EVENTUAL_CONSISTENCY);
    }

    /**
     * safe run task
     *
     * @param run
     */
    public static void anySafe(Runnable run) {
        try {
            run.run();
        } catch (Exception ignore) {
        }
    }

    /**
     * @param client                vector database client
     * @param createCollectionParam parameter of create collection
     * @param databaseName          database name
     * @param collAlias             collection alias
     */
    public static void createDatabaseAndCollection(VectorDBClient client,
                                                    CreateCollectionParam createCollectionParam,
                                                    String databaseName,
                                                    String collAlias) {
        // 1. 创建数据库
        System.out.println("---------------------- createDatabase ----------------------");
        Database db = client.createDatabase(databaseName);

        // 2. 列出所有数据库
        System.out.println("---------------------- listCollections ----------------------");
        List<String> database = client.listDatabase();
        for (String s : database) {
            System.out.println("\tres: " + s);
        }
//        Database db = client.database(DBNAME);

        // 3. 创建 collection
        System.out.println("---------------------- createCollection ----------------------");
        db.createCollection(createCollectionParam);

        // 4. 列出所有 collection
//        Database db = client.database(DBNAME);
        System.out.println("---------------------- listCollections ----------------------");
        List<Collection> cols = db.listCollections();
        for (Collection col : cols) {
            System.out.println("\tres: " + col.toString());
        }

        // 5. 设置 collection 别名
        System.out.println("---------------------- setAlias ----------------------");
        AffectRes affectRes = db.setAlias(createCollectionParam.getCollection(), collAlias);
        System.out.println("\tres: " + affectRes.toString());


        // 6. describe collection
        System.out.println("---------------------- describeCollection ----------------------");
        Collection descCollRes = db.describeCollection(createCollectionParam.getCollection());
        System.out.println("\tres: " + descCollRes.toString());

        // 7. delete alias
        System.out.println("---------------------- deleteAlias ----------------------");
        AffectRes affectRes1 = db.deleteAlias(collAlias);
        System.out.println("\tres: " + affectRes1);

        // 8. describe collection
        System.out.println("---------------------- describeCollection ----------------------");
        Collection descCollRes1 = db.describeCollection(createCollectionParam.getCollection());
        System.out.println("\tres: " + descCollRes1.toString());

    }
}
