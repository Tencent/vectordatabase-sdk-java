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
        String vdbURL = "http://21.0.191.80:8100";
        String vdbKey = "Kn4Y5RERjqbqJWNp85CN9PMX5wSNXDNCOOH7UpEE";
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
        // 创建http client
        return new VectorDBClient(initConnectParam(), ReadConsistencyEnum.EVENTUAL_CONSISTENCY);
        // 创建rpc client
//        return new RPCVectorDBClient(initConnectParam(), ReadConsistencyEnum.EVENTUAL_CONSISTENCY);
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
