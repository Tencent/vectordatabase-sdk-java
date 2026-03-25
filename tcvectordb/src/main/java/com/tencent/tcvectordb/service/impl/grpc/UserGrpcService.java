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
package com.tencent.tcvectordb.service.impl.grpc;

import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.param.entity.BaseRes;
import com.tencent.tcvectordb.model.param.user.*;
import com.tencent.tcvectordb.rpc.proto.Olama;
import com.tencent.tcvectordb.rpc.proto.SearchEngineGrpc;
import com.tencent.tcvectordb.service.ApiPath;
import io.grpc.ManagedChannel;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * gRPC service implementation for user management operations.
 */
public class UserGrpcService extends BaseGrpcService {

    /**
     * Create a new user account with specified permissions.
     */
    public BaseRes createUser(UserCreateParam userCreateParam) {
        Olama.UserAccountRequest.Builder builder = Olama.UserAccountRequest
                .newBuilder();
        if (userCreateParam.getUser()!=null){
            builder.setUser(userCreateParam.getUser());
        }
        if (userCreateParam.getPassword()!=null){
            builder.setPassword(userCreateParam.getPassword()).build();
        }
        logQuery(ApiPath.USER_CREATE, builder.build());
        ManagedChannel channel = channelPool.getChannel();
        Olama.UserAccountResponse response = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS).userCreate(builder.build());

        logResponse(ApiPath.USER_CREATE, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: create user account error, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return new BaseRes(response.getCode(), response.getMsg(), "");
    }

    /**
     * Grant permissions to an existing user.
     */
    public BaseRes grantToUser(UserGrantParam param) {
        Olama.UserPrivilegesRequest.Builder builder = Olama.UserPrivilegesRequest.newBuilder();
        if (param.getUser()!=null){
            builder.setUser(param.getUser());
        }
        if (param.getPrivileges()!=null){
            builder.addAllPrivileges(param.getPrivileges().stream().map(privilege ->
                    Olama.Privilege.newBuilder()
                            .setResource(privilege.getResource())
                            .addAllActions(privilege.getActions()).build()).collect(Collectors.toList()));
        }
        logQuery(ApiPath.USER_GRANT, builder);
        ManagedChannel channel = channelPool.getChannel();
        Olama.UserPrivilegesResponse response = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS).userGrant(builder.build());

        logResponse(ApiPath.USER_GRANT, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: grant user account error, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return new BaseRes(response.getCode(), response.getMsg(), "");
    }

    /**
     * Revoke permissions from an existing user.
     */
    public BaseRes revokeFromUser(UserRevokeParam param) {
        Olama.UserPrivilegesRequest.Builder builder = Olama.UserPrivilegesRequest.newBuilder();
        if (param.getUser()!=null){
            builder.setUser(param.getUser());
        }
        if (param.getPrivileges()!=null){
            builder.addAllPrivileges(param.getPrivileges().stream().map(privilege ->
                    Olama.Privilege.newBuilder()
                            .setResource(privilege.getResource())
                            .addAllActions(privilege.getActions()).build()).collect(Collectors.toList()));
        }
        logQuery(ApiPath.USER_REVOKE, builder);
        ManagedChannel channel = channelPool.getChannel();
        Olama.UserPrivilegesResponse response  = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS).userRevoke(builder.build());

        logResponse(ApiPath.USER_REVOKE, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: revoke user error, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return new BaseRes(response.getCode(), response.getMsg(), "");
    }

    /**
     * Get detailed information about a specific user.
     */
    public UserDescribeRes describeUser(UserDescribeParam userDescribeParam) {
        Olama.UserDescribeRequest.Builder builder = Olama.UserDescribeRequest.newBuilder();
        if (userDescribeParam.getUser()!=null){
            builder.setUser(userDescribeParam.getUser());
        }
        logQuery(ApiPath.USER_DESCRIBE, builder.build());
        ManagedChannel channel = channelPool.getChannel();
        Olama.UserDescribeResponse response = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS).userDescribe(builder.build());

        logResponse(ApiPath.USER_DESCRIBE, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: revoke user error, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        UserDescribeRes res = new UserDescribeRes(response.getCode(), response.getMsg(), "");
        if (response.hasUser()){
            res.setUser(response.getUser().getName());
            res.setPrivileges(response.getUser().getPrivilegesList().stream().map(privilege ->
                    PrivilegeParam.newBuilder().withResource(privilege.getResource()).withActions(privilege.getActionsList()).build())
                    .collect(Collectors.toList()));
        }
        return res;
    }

    /**
     * List all users in the system.
     */
    public UserListRes listUser() {
        Olama.UserListRequest request = Olama.UserListRequest.newBuilder().build();
        logQuery(ApiPath.USER_LIST, request);
        ManagedChannel channel = channelPool.getChannel();
        Olama.UserListResponse response = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS).userList(request);

        logResponse(ApiPath.USER_LIST, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: list user error, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        UserListRes res = new UserListRes(response.getCode(), response.getMsg(), "");
        if (response.getUsersList()!=null && !response.getUsersList().isEmpty()){
            res.setUsers(response.getUsersList().stream().map(user -> {
                UserInfo userInfo = new UserInfo();
                userInfo.setUser(user.getName());
                userInfo.setPrivileges(user.getPrivilegesList().stream().map(privilege ->
                                PrivilegeParam.newBuilder().withResource(privilege.getResource()).withActions(privilege.getActionsList()).build())
                        .collect(Collectors.toList()));
                return userInfo;
            }).collect(Collectors.toList()));
        }
        return res;
    }

    /**
     * Drop an existing user account.
     */
    public BaseRes dropUser(UserDropParam userDropParam) {
        Olama.UserAccountRequest.Builder builder = Olama.UserAccountRequest.newBuilder();
        if (userDropParam.getUser() != null) {
            builder.setUser(userDropParam.getUser());
        }

        logQuery(ApiPath.USER_DROP, builder.build());
        ManagedChannel channel = channelPool.getChannel();
        Olama.UserAccountResponse response = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS).userDrop(builder.build());

        logResponse(ApiPath.USER_DROP, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: drop user error, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return new BaseRes(response.getCode(), response.getMsg(), "");
    }

    /**
     * Change password for an existing user.
     */
    public BaseRes changeUserPassword(UserChangePasswordParam param) {
        Olama.UserAccountRequest.Builder builder = Olama.UserAccountRequest
                .newBuilder();
        if (param.getUser()!=null){
            builder.setUser(param.getUser());
        }
        if (param.getPassword()!=null){
            builder.setPassword(param.getPassword()).build();
        }
        logQuery(ApiPath.USER_CHANGE_PASSWORD, builder.build());
        ManagedChannel channel = channelPool.getChannel();
        Olama.UserAccountResponse response = SearchEngineGrpc.newBlockingStub(channel).withDeadlineAfter(this.timeout, TimeUnit.SECONDS).userChangePassword(builder.build());

        logResponse(ApiPath.USER_CHANGE_PASSWORD, response);
        if (response.getCode()!=0){
            throw new VectorDBException(String.format(
                    "VectorDBServer error: change user password error, body code=%s, message=%s",
                    response.getCode(), response.getMsg()));
        }
        return new BaseRes(response.getCode(), response.getMsg(), "");
    }
}
