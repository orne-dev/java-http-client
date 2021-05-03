package dev.orne.http.client;

/*-
 * #%L
 * Orne HTTP Client
 * %%
 * Copyright (C) 2020 Orne Developments
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base HTTP service client.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 */
public class BaseHttpServiceClient
implements HttpServiceClient {

    /** The HTTP service's host. */
    private final @NotNull HttpHost host;
    /** The HTTP service's base URI. */
    private final @NotNull URI baseURI;
    /** The HTTP client's cookie store. */
    private final @NotNull CookieStore cookieStore;
    /** The HTTP client. */
    private final @NotNull CloseableHttpClient client;
    /** The logger for this instance's actual class. */
    private Logger logger;

    /**
     * Creates a new instance.
     * 
     * @param baseURL The HTTP service's base URL
     */
    public BaseHttpServiceClient(
            final @NotNull URL baseURL) {
        super();
        Validate.notNull(baseURL, "Base URL is required.");
        this.host = new HttpHost(baseURL.getHost(), baseURL.getPort(), baseURL.getProtocol());
        this.baseURI = URI.create(baseURL.getPath());
        this.cookieStore = new BasicCookieStore();
        final RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        configureRequestConfig(requestConfigBuilder);
        final RequestConfig requestConfig = requestConfigBuilder.build();
        this.client = HttpClients.custom()
                .setDefaultCookieStore(this.cookieStore)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    /**
     * Creates a new instance.
     * 
     * @param host The HTTP service's host
     * @param baseURI The HTTP service's base URI
     * @param cookieStore The HTTP client's cookie store
     * @param client The HTTP client
     */
    protected BaseHttpServiceClient(
            final @NotNull HttpHost host,
            final @NotNull URI baseURI,
            final @NotNull CookieStore cookieStore,
            final @NotNull CloseableHttpClient client) {
        super();
        this.host = Validate.notNull(host, "Host is required");
        this.baseURI = Validate.notNull(baseURI, "Base URI is required");
        this.cookieStore = Validate.notNull(cookieStore, "Cookie store is required");
        this.client = Validate.notNull(client, "Client is required");
    }

    /**
     * <p>Configures the requests build by the HTTP client.
     * The implementations should override this method to configure their
     * requests if needed.</p>
     * 
     * <p>For example:</p>
     * <pre>
     * {@literal @}Override
     * protected void configureRequestConfig(
     *         final RequestConfig.Builder builder) {
     *     builder.setConnectTimeout(30000);
     * }
     * </pre>
     * 
     * @param builder The configuration builder
     */
    protected void configureRequestConfig(
            final @NotNull RequestConfig.Builder builder) {
        // Override if needed
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull HttpHost getHost() {
        return this.host;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull URI getBaseURI() {
        return this.baseURI;
    }

    /**
     * {@inheritDoc}
     */
    public @NotNull CookieStore getCookieStore() {
        return this.cookieStore;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CloseableHttpClient getClient() {
        return this.client;
    }

    /**
     * Executes the specified status unaware operation for this HTTP service.
     * 
     * @param <P> The operation's parameter type
     * @param <R> The operation execution's result type
     * @param operation The operation to execute
     * @param params The operation parameter
     * @return The operation execution's result
     * @throws HttpClientException If an error occurs executing the operation
     */
    public <P, R> R execute(
            final @NotNull StatusIndependentOperation<P, R> operation,
            final P params)
    throws HttpClientException {
        return operation.execute(params, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close()
    throws IOException {
        this.client.close();
        this.cookieStore.clear();
    }

    /**
     * Returns the logger for this instance's actual class.
     * 
     * @return The logger for this instance's actual class
     */
    protected @NotNull Logger getLogger() {
        synchronized (this) {
            if (this.logger == null) {
                this.logger = LoggerFactory.getLogger(getClass());
            }
            return this.logger;
        }
    }
}
