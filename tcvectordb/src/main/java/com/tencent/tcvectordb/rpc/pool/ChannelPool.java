package com.tencent.tcvectordb.rpc.pool;

import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.param.database.ConnectParam;
import com.tencent.tcvectordb.model.param.database.TLSConfig;
import com.tencent.tcvectordb.rpc.Interceptor.AuthorityInterceptor;
import com.tencent.tcvectordb.utils.TLSUtils;
import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Connection pool for gRPC channels.
 * <p>
 * This pool manages multiple gRPC channels for load balancing and provides
 * TLS/SSL support for secure connections.
 * </p>
 */
public class ChannelPool {
    private static final Logger logger = LoggerFactory.getLogger(ChannelPool.class);

    private List<ManagedChannel> pool;
    private AtomicLong channelSelectorCounter = new AtomicLong(0);

    public ChannelPool(ConnectParam param, int maxReceiveMessageSize, String authorization) {
        this.pool = new ArrayList<>();

        String url = param.getUrl();
        String address = getAddress(url);
        TLSConfig tlsConfig = param.getTLSConfig();
        boolean isHttps = TLSUtils.isHttps(url);

        for (int i = 0; i < (param.getConnectionPoolSize()); i++) {
            try {
                OkHttpChannelBuilder builder = OkHttpChannelBuilder.forTarget(address)
                        .intercept(new AuthorityInterceptor(authorization))
                        .flowControlWindow(maxReceiveMessageSize)
                        .maxInboundMessageSize(maxReceiveMessageSize)
                        .enableRetry();

                // Configure TLS based on URL protocol and TLS config
                configureTLS(builder, url, tlsConfig, isHttps);

                pool.add(builder.build());
            } catch (Exception e) {
                throw new VectorDBException("create channel pool error", e);
            }
        }
    }

    /**
     * Configures TLS/SSL settings for the gRPC channel.
     *
     * @param builder   OkHttpChannelBuilder
     * @param url       connection URL
     * @param tlsConfig TLS configuration
     * @param isHttps   whether the URL uses HTTPS
     */
    private void configureTLS(OkHttpChannelBuilder builder, String url, TLSConfig tlsConfig, boolean isHttps) {
        if (!isHttps) {
            // HTTP connection - use plaintext
            builder.usePlaintext();
            logger.debug("gRPC using plaintext (HTTP)");
            return;
        }

        // HTTPS connection - configure TLS
        if (tlsConfig == null) {
            // No TLS config - use default TLS settings
            builder.useTransportSecurity();
            logger.debug("gRPC using default TLS settings");
            return;
        }

        logger.debug("Configuring gRPC TLS: {}", tlsConfig);

        try {
            if (tlsConfig.isInsecureSkipVerify()) {
                // Skip certificate verification (INSECURE - for testing only)
                logger.warn("gRPC TLS certificate verification is disabled. This is insecure!");
                SSLSocketFactory sslSocketFactory = TLSUtils.createSSLSocketFactory(tlsConfig);
                X509TrustManager trustManager = TLSUtils.createTrustManager(tlsConfig);
                
                if (sslSocketFactory != null && trustManager != null) {
                    builder.sslSocketFactory(sslSocketFactory);
                    // Note: OkHttpChannelBuilder doesn't support setting a custom TrustManager directly
                    // The insecure SSLSocketFactory already trusts all certificates
                }
            } else if (tlsConfig.getCACert() != null && !tlsConfig.getCACert().isEmpty()) {
                // Use custom CA certificate
                SSLSocketFactory sslSocketFactory = TLSUtils.createSSLSocketFactory(tlsConfig);
                if (sslSocketFactory != null) {
                    builder.sslSocketFactory(sslSocketFactory);
                }
            } else {
                // Use default TLS
                builder.useTransportSecurity();
            }

            // Configure SNI override for IP address connections
            if (TLSUtils.isIPAddress(url)) {
                String serverName = TLSUtils.getServerNameForSNI(url, tlsConfig);
                if (serverName != null) {
                    builder.overrideAuthority(serverName);
                    logger.debug("gRPC override authority for SNI: {}", serverName);
                }
            }
        } catch (Exception e) {
            throw new VectorDBException("Failed to configure gRPC TLS: " + e.getMessage(), e);
        }
    }

    private String getAddress(String url) {
        URL _url = null;
        try {
            _url = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        
        String host = _url.getHost();
        int port = _url.getPort();
        
        if (port <= 0) {
            // Use default port based on protocol
            if (url.toLowerCase().startsWith("https://")) {
                port = 443;
            } else {
                port = 80;
            }
        }
        
        return host + ":" + port;
    }

    public ManagedChannel getChannel() {
        try {
            long count = channelSelectorCounter.incrementAndGet();
            if (count < 0) {
                count = 0;
                channelSelectorCounter.set(0);
            }
            Long index = count % pool.size();
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
