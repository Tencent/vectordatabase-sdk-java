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

import com.fasterxml.jackson.databind.JsonNode;
import com.tencent.tcvectordb.model.param.entity.BaseRes;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.model.param.user.*;
import com.tencent.tcvectordb.service.ApiPath;
import com.tencent.tcvectordb.utils.JsonUtils;

/**
 * HTTP service implementation for user management operations.
 */
public class UserHttpService extends BaseHttpService {

    /**
     * Create a new user account with specified permissions.
     */
    public BaseRes createUser(UserCreateParam userCreateParam) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.USER_CREATE);
        JsonNode jsonNode = this.post(url, userCreateParam.toString(), false);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    /**
     * Grant permissions to an existing user.
     */
    public BaseRes grantToUser(UserGrantParam param) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.USER_GRANT);
        JsonNode jsonNode = this.post(url, param.toString(), false);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    /**
     * Revoke permissions from an existing user.
     */
    public BaseRes revokeFromUser(UserRevokeParam param) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.USER_REVOKE);
        JsonNode jsonNode = this.post(url, param.toString(), false);
        return JsonUtils.parseObject(jsonNode.toString(), AffectRes.class);
    }

    /**
     * Get detailed information about a specific user.
     */
    public UserDescribeRes describeUser(UserDescribeParam userDescribeParam) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.USER_DESCRIBE);
        JsonNode jsonNode = this.post(url, userDescribeParam.toString(), false);
        return JsonUtils.parseObject(jsonNode.toString(), UserDescribeRes.class);
    }

    /**
     * List all users in the system.
     */
    public UserListRes listUser() {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.USER_LIST);
        JsonNode jsonNode = this.get(url, false);
        return JsonUtils.parseObject(jsonNode.toString(), UserListRes.class);
    }

    /**
     * Drop an existing user account.
     */
    public BaseRes dropUser(UserDropParam userDropParam) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.USER_DROP);
        JsonNode jsonNode = this.post(url, userDropParam.toString(), false);
        return JsonUtils.parseObject(jsonNode.toString(), BaseRes.class);
    }

    /**
     * Change password for an existing user.
     */
    public BaseRes changeUserPassword(UserChangePasswordParam build) {
        String url = String.format("%s/%s", this.connectParam.getUrl(), ApiPath.USER_CHANGE_PASSWORD);
        JsonNode jsonNode = this.post(url, build.toString(), false);
        return JsonUtils.parseObject(jsonNode.toString(), BaseRes.class);
    }
}