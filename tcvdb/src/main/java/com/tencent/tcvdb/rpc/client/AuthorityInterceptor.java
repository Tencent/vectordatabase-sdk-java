package com.tencent.tcvdb.rpc.client;

import io.grpc.*;

public class AuthorityInterceptor implements ClientInterceptor {
    private final Metadata.Key<String> authorityKey = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);
    private  String authorityValue;
    public AuthorityInterceptor(String authorityValue) {
        this.authorityValue = authorityValue;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        ClientCall<ReqT, RespT> call = next.newCall(method, callOptions);
        call = new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(call) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                headers.put(authorityKey, authorityValue);
                super.start(responseListener, headers);
            }
        };
        return call;
    }
}
