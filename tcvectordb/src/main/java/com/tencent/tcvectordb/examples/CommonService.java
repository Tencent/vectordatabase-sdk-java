package com.tencent.tcvectordb.examples;

import com.tencent.tcvectordb.client.VectorDBClient;
import com.tencent.tcvectordb.model.Collection;
import com.tencent.tcvectordb.model.Database;
import com.tencent.tcvectordb.model.param.collection.CreateCollectionParam;
import com.tencent.tcvectordb.model.param.database.ConnectParam;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvectordb.client.RPCVectorDBClient;

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
        String vdbURL = "";
        String vdbKey = "";
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
        return new RPCVectorDBClient(initConnectParam(), ReadConsistencyEnum.EVENTUAL_CONSISTENCY);
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
    
}
