package com.tencent.tcvectordb.examples;

import com.tencent.tcvectordb.client.RPCVectorDBClient;
import com.tencent.tcvectordb.client.VectorDBClient;
import com.tencent.tcvectordb.model.param.database.ConnectParam;
import com.tencent.tcvectordb.model.param.entity.BaseRes;
import com.tencent.tcvectordb.model.param.enums.OrderEnum;
import com.tencent.tcvectordb.model.param.enums.ReadConsistencyEnum;
import com.tencent.tcvectordb.model.param.user.*;
import com.tencent.tcvectordb.utils.JsonUtils;

import java.util.Arrays;

public class VectorDBUserPermissionExample {

    public static String coll_test = "java-sdk-user-coll-test";
    public static String db_test = "java-sdk-test-user-permission";

    public static String user_test = "java_sdk_test_user";
    public static void main(String[] args) {
        String vdbURL = "";
        String vdbKey = "";
        System.out.println("\tvdb_url: " + vdbURL);
        System.out.println("\tvdb_key: " + vdbKey);
        VectorDBClient client = new VectorDBClient(ConnectParam.newBuilder()
                .withUrl(vdbURL)
                .withUsername("root")
                .withKey(vdbKey)
                .withTimeout(30)
                .build(),  ReadConsistencyEnum.EVENTUAL_CONSISTENCY);

        client.createDatabaseIfNotExists(db_test);
        CommonService.anySafe(() -> client.dropUser(user_test));

        System.out.println("-----------create user: " + user_test);
        BaseRes res = client.createUser(user_test, "0dd8e8b3d674");
        System.out.println("create user res: code:" + res.getCode() + ", msg: " + res.getMsg());
        System.out.println("-----------describe user: " + user_test);
        UserDescribeRes userDescribeRes = client.describeUser(user_test);
        System.out.println("describe user res: " + userDescribeRes.toString());

        System.out.println("-----------change user: " + user_test);
        res = client.changeUserPassword(user_test, "dd8e8b3d6740");
        System.out.println("change user password res: " + res.getCode() + " " + res.getMsg());
        System.out.println("-----------describe user: " + user_test);
        userDescribeRes = client.describeUser(user_test);
        System.out.println("describe user res: " + userDescribeRes.toString());

        System.out.println("------------list user: ");
        UserListRes userListRes = client.listUser();
        System.out.println("list user res: " + JsonUtils.toJsonString(userListRes.getUsers()));

        System.out.println("------------grant user permission: " + user_test);
        res = client.grantToUser(UserGrantParam.newBuilder()
                .withUser(user_test)
                .withPrivileges(Arrays.asList(
                        PrivilegeParam.newBuilder().withResource("java-sdk-test-user-permission.*").withActions(Arrays.asList("read")).build(),
                        PrivilegeParam.newBuilder().withResource("java-sdk-test-user-permission.*").withActions(Arrays.asList("readWrite")).build())
                ).build());
        System.out.println("grant user permission res: code:" + res.getCode() + ", msg: " + res.getMsg());
        userDescribeRes = client.describeUser(user_test);
        System.out.println("describe user res: " + userDescribeRes.toString());

        System.out.println("--------------revoke user permission: " + user_test);
        res = client.revokeFromUser(UserRevokeParam.newBuilder()
                .withUser(user_test)
                .withPrivilege(
                        PrivilegeParam.newBuilder().withResource("java-sdk-test-user-permission.*").withActions(Arrays.asList("read")).build()
                ).build());
        System.out.println("revoke user permission res: code:" + res.getCode() + ", msg: " + res.getMsg());

        userDescribeRes = client.describeUser(user_test);
        System.out.println("describe user res: " + userDescribeRes.toString());

        System.out.println("--------------drop user: " + user_test);
        res = client.dropUser(user_test);
        System.out.println("drop user res: code:" + res.getCode() + ", msg: " + res.getMsg());

    }

}
