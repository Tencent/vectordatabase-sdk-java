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

package com.tencent.tcvectordb.utils;

import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.param.database.TLSConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;

/**
 * Utility class for TLS/SSL operations.
 * <p>
 * This class provides helper methods to create SSL contexts and configure TLS connections
 * for both HTTP (OkHttp) and gRPC clients.
 * </p>
 */
public class TLSUtils {

    private static final Logger logger = LoggerFactory.getLogger(TLSUtils.class);

    private static final String PEM_BEGIN_MARKER = "-----BEGIN CERTIFICATE-----";
    private static final String PEM_END_MARKER = "-----END CERTIFICATE-----";

    private TLSUtils() {
        // Utility class, prevent instantiation
    }

    /**
     * Creates an SSLSocketFactory based on the TLS configuration.
     *
     * @param tlsConfig TLS configuration
     * @return SSLSocketFactory configured according to the TLS config
     * @throws VectorDBException if SSL configuration fails
     */
    public static SSLSocketFactory createSSLSocketFactory(TLSConfig tlsConfig) {
        if (tlsConfig == null) {
            return null;
        }

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");

            if (tlsConfig.isInsecureSkipVerify()) {
                // Trust all certificates (INSECURE - for testing only)
                logger.warn("TLS certificate verification is disabled. This is insecure and should only be used for testing!");
                TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(X509Certificate[] chain, String authType) {
                                // Trust all clients
                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] chain, String authType) {
                                // Trust all servers
                            }

                            @Override
                            public X509Certificate[] getAcceptedIssuers() {
                                return new X509Certificate[0];
                            }
                        }
                };
                sslContext.init(null, trustAllCerts, new SecureRandom());
            } else if (tlsConfig.getCACert() != null && !tlsConfig.getCACert().isEmpty()) {
                // Use custom CA certificate
                TrustManager[] trustManagers = createTrustManagers(tlsConfig.getCACert());
                sslContext.init(null, trustManagers, new SecureRandom());
            } else {
                // Use default trust managers
                sslContext.init(null, null, new SecureRandom());
            }

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new VectorDBException("Failed to create SSL socket factory: " + e.getMessage(), e);
        }
    }

    /**
     * Creates an X509TrustManager based on the TLS configuration.
     *
     * @param tlsConfig TLS configuration
     * @return X509TrustManager configured according to the TLS config
     * @throws VectorDBException if trust manager creation fails
     */
    public static X509TrustManager createTrustManager(TLSConfig tlsConfig) {
        if (tlsConfig == null) {
            return getDefaultTrustManager();
        }

        if (tlsConfig.isInsecureSkipVerify()) {
            // Trust all certificates (INSECURE - for testing only)
            return new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    // Trust all clients
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    // Trust all servers
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
        }

        if (tlsConfig.getCACert() != null && !tlsConfig.getCACert().isEmpty()) {
            TrustManager[] trustManagers = createTrustManagers(tlsConfig.getCACert());
            for (TrustManager tm : trustManagers) {
                if (tm instanceof X509TrustManager) {
                    return (X509TrustManager) tm;
                }
            }
        }

        return getDefaultTrustManager();
    }

    /**
     * Creates TrustManagers from CA certificate content or file path.
     *
     * @param caCertOption CA certificate content or file path
     * @return array of TrustManagers
     * @throws VectorDBException if trust managers creation fails
     */
    public static TrustManager[] createTrustManagers(String caCertOption) {
        if (caCertOption == null || caCertOption.isEmpty()) {
            return null;
        }

        try {
            String certContent = loadCertificateContent(caCertOption);

            // Validate PEM format
            if (!certContent.contains(PEM_BEGIN_MARKER) || !certContent.contains(PEM_END_MARKER)) {
                throw new VectorDBException("CA certificate must be in PEM format with BEGIN/END CERTIFICATE markers");
            }

            // Create certificate factory and load certificates
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            Collection<? extends Certificate> certificates;

            try (InputStream certInputStream = new ByteArrayInputStream(certContent.getBytes(StandardCharsets.UTF_8))) {
                certificates = certFactory.generateCertificates(certInputStream);
            }

            if (certificates.isEmpty()) {
                throw new VectorDBException("No certificates found in the provided CA certificate");
            }

            // Create KeyStore and add certificates
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            int certIndex = 0;
            for (Certificate cert : certificates) {
                keyStore.setCertificateEntry("ca-cert-" + certIndex++, cert);
            }

            // Create TrustManagerFactory with the KeyStore
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            return trustManagerFactory.getTrustManagers();
        } catch (VectorDBException e) {
            throw e;
        } catch (Exception e) {
            throw new VectorDBException("Failed to create trust managers from CA certificate: " + e.getMessage(), e);
        }
    }

    /**
     * Loads certificate content from file path or returns the content directly if it's already PEM content.
     *
     * @param caCertOption CA certificate content or file path
     * @return certificate content in PEM format
     * @throws VectorDBException if loading fails
     */
    public static String loadCertificateContent(String caCertOption) {
        if (caCertOption == null || caCertOption.isEmpty()) {
            return null;
        }

        String trimmed = caCertOption.trim();

        // Check if it's a file path
        if (isFilePath(trimmed)) {
            try {
                return new String(Files.readAllBytes(Paths.get(trimmed)), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new VectorDBException("Failed to read CA certificate file: " + trimmed, e);
            }
        }

        // It's certificate content directly
        return trimmed;
    }

    /**
     * Checks if the given string is a file path.
     *
     * @param str the string to check
     * @return true if the string is a valid file path that exists
     */
    public static boolean isFilePath(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        // If it starts with PEM markers, it's not a file path
        if (str.contains(PEM_BEGIN_MARKER)) {
            return false;
        }

        // Check if the file exists
        File file = new File(str);
        return file.exists() && file.isFile();
    }

    /**
     * Checks if the given URL is an IP address.
     *
     * @param url the URL to check
     * @return true if the host in the URL is an IP address
     */
    public static boolean isIPAddress(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }

        // Remove protocol prefix
        String host = url;
        if (host.startsWith("http://")) {
            host = host.substring(7);
        } else if (host.startsWith("https://")) {
            host = host.substring(8);
        }

        // Remove path if present
        int slashIndex = host.indexOf('/');
        if (slashIndex > 0) {
            host = host.substring(0, slashIndex);
        }

        // Handle IPv6 with brackets [::1]:port
        if (host.startsWith("[")) {
            int bracketEnd = host.indexOf(']');
            if (bracketEnd > 0) {
                host = host.substring(1, bracketEnd);
            }
            return isIPv6Address(host);
        }
        
        // Check if it looks like IPv6 without brackets (contains multiple colons)
        // IPv6 addresses have at least 2 colons (e.g., ::1, fe80::1, 2001:db8::1)
        int colonCount = 0;
        for (int i = 0; i < host.length(); i++) {
            if (host.charAt(i) == ':') {
                colonCount++;
            }
        }
        
        if (colonCount >= 2) {
            // This is likely an IPv6 address
            return isIPv6Address(host);
        }
        
        // For IPv4 or hostname with optional port (single colon)
        if (colonCount == 1) {
            // Remove port for IPv4
            int colonIndex = host.lastIndexOf(':');
            if (colonIndex > 0) {
                host = host.substring(0, colonIndex);
            }
        }

        // Check IPv4 pattern
        return isIPv4Address(host);
    }

    /**
     * Checks if the given string is a valid IPv4 address.
     */
    private static boolean isIPv4Address(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        String[] parts = str.split("\\.");
        if (parts.length != 4) {
            return false;
        }

        try {
            for (String part : parts) {
                int value = Integer.parseInt(part);
                if (value < 0 || value > 255) {
                    return false;
                }
                // Check for leading zeros (invalid in strict IP addresses)
                if (part.length() > 1 && part.startsWith("0")) {
                    return false;
                }
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the given string is a valid IPv6 address.
     */
    private static boolean isIPv6Address(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        // Simple IPv6 check: contains colon
        if (!str.contains(":")) {
            return false;
        }

        // Remove brackets if present
        String addr = str;
        if (addr.startsWith("[") && addr.endsWith("]")) {
            addr = addr.substring(1, addr.length() - 1);
        }

        // Check for :: (zero compression) - can appear only once
        boolean hasDoubleColon = addr.contains("::");
        int doubleColonCount = 0;
        int idx = 0;
        while ((idx = addr.indexOf("::", idx)) != -1) {
            doubleColonCount++;
            idx += 2;
        }
        if (doubleColonCount > 1) {
            return false; // Only one :: allowed
        }

        // IPv6 should have at most 8 groups separated by colons
        String[] parts = addr.split(":", -1);
        
        // Count non-empty parts
        int nonEmptyCount = 0;
        
        for (String part : parts) {
            if (!part.isEmpty()) {
                nonEmptyCount++;
                // Each part should be 1-4 hex characters
                if (part.length() > 4) {
                    return false;
                }
                try {
                    Integer.parseInt(part, 16);
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }

        // Valid IPv6 has at most 8 groups
        // With ::, we can have fewer non-empty groups (the :: represents missing zeros)
        // Without ::, we need exactly 8 groups
        if (hasDoubleColon) {
            return nonEmptyCount <= 7; // :: takes at least one group's place
        } else {
            return nonEmptyCount == 8;
        }
    }

    /**
     * Gets the hostname from a URL.
     *
     * @param urlString the URL string
     * @return the hostname
     */
    public static String getHostname(String urlString) {
        if (urlString == null || urlString.isEmpty()) {
            return null;
        }

        try {
            URL url = new URL(urlString);
            return url.getHost();
        } catch (MalformedURLException e) {
            // Try to extract hostname manually
            String host = urlString;
            if (host.startsWith("http://")) {
                host = host.substring(7);
            } else if (host.startsWith("https://")) {
                host = host.substring(8);
            }

            int slashIndex = host.indexOf('/');
            if (slashIndex > 0) {
                host = host.substring(0, slashIndex);
            }

            int colonIndex = host.lastIndexOf(':');
            if (colonIndex > 0) {
                host = host.substring(0, colonIndex);
            }

            return host;
        }
    }

    /**
     * Checks if the URL uses HTTPS protocol.
     *
     * @param url the URL to check
     * @return true if the URL uses HTTPS
     */
    public static boolean isHttps(String url) {
        return url != null && url.toLowerCase().startsWith("https://");
    }

    /**
     * Creates a HostnameVerifier that allows all hostnames (INSECURE).
     *
     * @return HostnameVerifier that trusts all hostnames
     */
    public static HostnameVerifier createInsecureHostnameVerifier() {
        return (hostname, session) -> true;
    }

    /**
     * Gets the default X509TrustManager from the system.
     *
     * @return the default X509TrustManager
     */
    private static X509TrustManager getDefaultTrustManager() {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);

            for (TrustManager tm : trustManagerFactory.getTrustManagers()) {
                if (tm instanceof X509TrustManager) {
                    return (X509TrustManager) tm;
                }
            }
        } catch (Exception e) {
            logger.error("Failed to get default trust manager", e);
        }

        // Return a trust manager that uses default behavior
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // Use default behavior
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // Use default behavior
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    /**
     * Determines whether TLS should be enabled based on URL and TLS configuration.
     *
     * @param url       the URL
     * @param tlsConfig the TLS configuration
     * @return true if TLS should be enabled
     */
    public static boolean shouldEnableTLS(String url, TLSConfig tlsConfig) {
        if (!isHttps(url)) {
            return false;
        }
        // For HTTPS, we always need TLS
        return true;
    }

    /**
     * Gets the server name for SNI based on URL and TLS configuration.
     *
     * @param url       the URL
     * @param tlsConfig the TLS configuration
     * @return the server name for SNI
     */
    public static String getServerNameForSNI(String url, TLSConfig tlsConfig) {
        if (isIPAddress(url)) {
            // If URL is an IP address, use the configured server name or default
            if (tlsConfig != null && tlsConfig.getServerName() != null) {
                return tlsConfig.getServerName();
            }
            return TLSConfig.DEFAULT_SERVER_NAME;
        }
        // If URL is a hostname, return null to use default SNI behavior
        return null;
    }
}
