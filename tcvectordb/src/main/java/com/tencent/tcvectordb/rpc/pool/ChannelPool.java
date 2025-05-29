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
import java.util.concurrent.TimeUnit;

public class ChannelPool {
    private final GenericObjectPool<ManagedChannel> pool;

    public ChannelPool(ConnectParam param, int maxReceiveMessageSize, String authorization)   {
        GenericObjectPoolConfig<ManagedChannel> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(param.getMaxIdleConnections()); // 最大连接数
        config.setMaxIdle(param.getMaxIdleConnections());   // 最大空闲连接
        config.setMaxWait(Duration.ofSeconds(param.getConnectTimeout()));

        this.pool = new GenericObjectPool<>(new ChannelFactory(getAddress(param.getUrl()), maxReceiveMessageSize, authorization), config);

        for (int i = 0; i < pool.getMaxIdle(); i++) {
            try {
                pool.addObject(); // 添加一个初始对象到池中，直到达到maxIdle设置的数量
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
//            printPoolStats();
            return pool.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void returnChannel(ManagedChannel channel) {
        pool.returnObject(channel);
    }

    /**
     * 关闭连接池，会关闭池中所有 ManagedChannel
     */
    public void close() {
        if (pool != null) {
            pool.close();
        }
    }

    public void printPoolStats() {
        System.out.println("Active: " + pool.getNumActive());
        System.out.println("Idle: " + pool.getNumIdle());
        System.out.println("Total created: " + pool.getCreatedCount());
        System.out.println("Total borrowed: " + pool.getBorrowedCount());
        System.out.println("Total returned: " + pool.getReturnedCount());
    }

    private static class ChannelFactory extends BasePooledObjectFactory<ManagedChannel> {
        private final String url;
        private final int maxReceiveMessageSize;
        private final String authorization;

        public ChannelFactory(String url, int maxReceiveMessageSize,  String authorization)  {
            this.url = url;
            this.maxReceiveMessageSize = maxReceiveMessageSize;
            this.authorization = authorization;
        }

        @Override
        public ManagedChannel create() {
            return OkHttpChannelBuilder.forTarget(url).
                    intercept(new AuthorityInterceptor(this.authorization)).
                    flowControlWindow(maxReceiveMessageSize).
                    maxInboundMessageSize(maxReceiveMessageSize).
                    enableRetry().
                    usePlaintext().build();
        }

        @Override
        public PooledObject<ManagedChannel> wrap(ManagedChannel channel) {
            return new DefaultPooledObject<>(channel);
        }

        @Override
        public void destroyObject(PooledObject<ManagedChannel> p) {
            p.getObject().shutdown();
        }
    }
}
