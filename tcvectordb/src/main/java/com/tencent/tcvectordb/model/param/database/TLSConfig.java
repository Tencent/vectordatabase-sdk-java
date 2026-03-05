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

package com.tencent.tcvectordb.model.param.database;

/**
 * TLS configuration for HTTPClient and RPCClient.
 * <p>
 * This class provides configuration options for TLS/SSL connections to VectorDB server.
 * </p>
 *
 * <h3>Usage Examples:</h3>
 *
 * <p><b>1. Using CA certificate file path:</b></p>
 * <pre>{@code
 * TLSConfig tlsConfig = TLSConfig.newBuilder()
 *     .withCACert("/path/to/ca-cert.pem")
 *     .build();
 * }</pre>
 *
 * <p><b>2. Using CA certificate content directly:</b></p>
 * <pre>{@code
 * String caCertContent = "-----BEGIN CERTIFICATE-----\n...certificate content...\n-----END CERTIFICATE-----";
 * TLSConfig tlsConfig = TLSConfig.newBuilder()
 *     .withCACert(caCertContent)
 *     .build();
 * }</pre>
 *
 * <p><b>3. Skip certificate verification (for testing only):</b></p>
 * <pre>{@code
 * TLSConfig tlsConfig = TLSConfig.newBuilder()
 *     .withInsecureSkipVerify(true)
 *     .build();
 * }</pre>
 */
public class TLSConfig {

    /**
     * Default server name for SNI when connecting to IP addresses.
     */
    public static final String DEFAULT_SERVER_NAME = "vdb.tencentcloud.com";

    /**
     * CA certificate content or file path for HTTPS connections.
     * <p>
     * If the value is a valid file path, the certificate will be read from the file.
     * Otherwise, it will be treated as the certificate content directly (PEM format).
     * </p>
     */
    private final String caCert;

    /**
     * Whether to skip TLS certificate verification.
     * <p>
     * WARNING: This should only be used for testing environments.
     * Never use this in production as it makes the connection vulnerable to MITM attacks.
     * </p>
     */
    private final boolean insecureSkipVerify;

    /**
     * Server name for SNI (Server Name Indication).
     * <p>
     * This is automatically set to {@link #DEFAULT_SERVER_NAME} when connecting to an IP address.
     * </p>
     */
    private final String serverName;

    private TLSConfig(Builder builder) {
        this.caCert = builder.caCert;
        this.insecureSkipVerify = builder.insecureSkipVerify;
        this.serverName = builder.serverName;
    }

    /**
     * Gets the CA certificate content or file path.
     *
     * @return CA certificate content or file path, or null if not set
     */
    public String getCACert() {
        return caCert;
    }

    /**
     * Returns whether to skip TLS certificate verification.
     *
     * @return true if certificate verification should be skipped, false otherwise
     */
    public boolean isInsecureSkipVerify() {
        return insecureSkipVerify;
    }

    /**
     * Gets the server name for SNI.
     *
     * @return server name for SNI
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Creates a new builder for TLSConfig.
     *
     * @return a new Builder instance
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Builder class for TLSConfig.
     */
    public static class Builder {
        private String caCert;
        private boolean insecureSkipVerify = false;
        private String serverName = DEFAULT_SERVER_NAME;

        private Builder() {
        }

        /**
         * Sets the CA certificate content or file path.
         * <p>
         * The certificate can be provided in two ways:
         * <ul>
         *   <li>As a file path pointing to a PEM-encoded certificate file</li>
         *   <li>As the certificate content directly in PEM format</li>
         * </ul>
         * </p>
         *
         * @param caCert CA certificate content or file path
         * @return this builder
         */
        public Builder withCACert(String caCert) {
            this.caCert = caCert;
            return this;
        }

        /**
         * Sets whether to skip TLS certificate verification.
         * <p>
         * WARNING: Only use this for testing environments. Never use in production.
         * </p>
         *
         * @param insecureSkipVerify true to skip certificate verification
         * @return this builder
         */
        public Builder withInsecureSkipVerify(boolean insecureSkipVerify) {
            this.insecureSkipVerify = insecureSkipVerify;
            return this;
        }

        /**
         * Sets the server name for SNI.
         * <p>
         * By default, this is set to {@link TLSConfig#DEFAULT_SERVER_NAME}.
         * </p>
         *
         * @param serverName server name for SNI
         * @return this builder
         */
        public Builder withServerName(String serverName) {
            if (serverName != null && !serverName.isEmpty()) {
                this.serverName = serverName;
            }
            return this;
        }

        /**
         * Builds the TLSConfig instance.
         *
         * @return a new TLSConfig instance
         */
        public TLSConfig build() {
            return new TLSConfig(this);
        }
    }

    @Override
    public String toString() {
        return "TLSConfig{" +
                "caCert='" + (caCert != null ? "[SET]" : "[NOT SET]") + '\'' +
                ", insecureSkipVerify=" + insecureSkipVerify +
                ", serverName='" + serverName + '\'' +
                '}';
    }
}
