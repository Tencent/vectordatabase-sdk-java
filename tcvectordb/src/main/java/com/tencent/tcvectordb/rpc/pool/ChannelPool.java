package com.tencent.tcvectordb.rpc.pool;

import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.param.database.ConnectParam;
import com.tencent.tcvectordb.rpc.Interceptor.AuthorityInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.okhttp.OkHttpChannelBuilder;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ChannelPool {
    private List<ManagedChannel> pool;
    private AtomicLong activeCount = new AtomicLong(0);

    public ChannelPool(ConnectParam param, int maxReceiveMessageSize, String authorization)   {

        this.pool = new ArrayList<>();

        for (int i = 0; i < (param.getConnectionPoolSize()); i++) {
            try {
                pool.add(OkHttpChannelBuilder.forTarget(getAddress(param.getUrl())).
                        intercept(new AuthorityInterceptor(authorization)).
                        flowControlWindow(maxReceiveMessageSize).
                        maxInboundMessageSize(maxReceiveMessageSize).
                        enableRetry().
                        usePlaintext().build());
            } catch (Exception e) {
                throw new VectorDBException("create channel pool error",  e);
            }
        }

    }

    private String getAddress(String url){
        URL _url = null;
        try {
            _url = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        if (_url.getPort()<=0){
            url = url + ":80";
        }
        return url.replaceFirst("http://", "").replaceFirst("https://", "");
    }

    public ManagedChannel getChannel() {
        try {
             activeCount.incrementAndGet();
             Long index = activeCount.get() % pool.size();
            return pool.get(index.intValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭连接池，会关闭池中所有 ManagedChannel
     */
    public void close() {
        if (pool != null) {
            for (ManagedChannel channel : pool) {
                channel.shutdown();
            }
        }
    }
}
