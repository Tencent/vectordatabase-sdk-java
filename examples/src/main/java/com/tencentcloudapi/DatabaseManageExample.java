package com.tencentcloudapi;

import com.tencentcloudapi.client.VectorDBClient;
import com.tencentcloudapi.model.param.database.ConnectParam;

import java.util.List;

/**
 * VectorDB Java SDK usage example
 * User: wlleiiwang
 * Date: 2023/7/24
 */
public class DatabaseManageExample {

    public static void main(String[] args) {
        // 创建VectorDB Client
        ConnectParam  connectParam = ConnectParam.newBuilder()
                .withUrl("http://11.141.218.193:8100")
                .withUsername("root")
                .withKey("12jih3DC8GSQXrr0DBLKkCwPkjMzPwpBO93IE6f5")
                .withTimeout(10)
                .build();
        VectorDBClient client = new VectorDBClient(connectParam);
        // List databases
        List<String> dbs = client.listDatabase();
        System.out.println(dbs);
        // create database
        client.createDatabase("db001");
        System.out.println(client.listDatabase());
        // drop database
        client.dropDatabase("db001");
        System.out.println(client.listDatabase());
    }
}
