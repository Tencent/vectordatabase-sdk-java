package com.tencent.tcvectordb.rpc.Interceptor;

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BackendServiceInterceptor implements ClientInterceptor {
    private final Metadata.Key<String> backendKey = Metadata.Key.of("backend-service", Metadata.ASCII_STRING_MARSHALLER);
    private  String backendValue;
    private static final Logger logger = LoggerFactory.getLogger(BackendServiceInterceptor.class.getName());
    public BackendServiceInterceptor(Boolean ai) {
        if (ai){
            this.backendValue = "ai";
        }else {
            this.backendValue = "vdb";
        }
        logger.debug("Backend: {}", backendValue);
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        ClientCall<ReqT, RespT> call = next.newCall(method, callOptions);
        call = new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(call) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                headers.put(backendKey, backendValue);
                super.start(responseListener, headers);
            }
        };
        return call;
    }
}
