package com.tencent.tcvectordb.service.param;

import com.qcloud.cos.endpoint.EndpointBuilder;

import com.qcloud.cos.endpoint.EndpointBuilder;
import com.qcloud.cos.internal.BucketNameUtils;
import com.qcloud.cos.internal.UrlComponentUtils;

public class CosEndpointBuilder implements EndpointBuilder {

    private String cosEndpoint;
    private String cosGetServiceEndpoint = "service.cos.tencentcos.cn";

    public CosEndpointBuilder(String endpoint) {
        super();
        if (endpoint == null) {
            throw new IllegalArgumentException("endpoint must not be null");
        }
        while (endpoint.startsWith(".")) {
            endpoint = endpoint.substring(1);
        }
        UrlComponentUtils.validateEndPointSuffix(endpoint);
        this.cosEndpoint = endpoint.trim();
    }

    @Override
    public String buildGeneralApiEndpoint(String bucketName) {
        BucketNameUtils.validateBucketName(bucketName);
        return String.format("%s.%s", bucketName, this.cosEndpoint);
    }

    @Override
    public String buildGetServiceApiEndpoint() {
        return cosGetServiceEndpoint;
    }
}