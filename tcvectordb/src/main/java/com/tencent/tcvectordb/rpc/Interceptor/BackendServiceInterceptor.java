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