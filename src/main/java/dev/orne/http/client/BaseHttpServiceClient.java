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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

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
    private final HttpHost host;
    /** The HTTP service's base URI. */
    private final URI baseURI;
    /** The HTTP client's cookie store. */
    private final CookieStore cookieStore;
    /** The HTTP client. */
    private final CloseableHttpClient client;

    /**
     * Creates a new instance.
     * 
     * @param baseURL The HTTP service's base URL
     */
    public BaseHttpServiceClient(
            @Nonnull
            final URL baseURL) {
        super();
        if (baseURL == null) {
            throw new IllegalArgumentException("Parameter 'baseURL' is required.");
        }
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
            @Nonnull
            final RequestConfig.Builder builder) {
        // Override if needed
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public HttpHost getHost() {
        return this.host;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public URI getBaseURI() {
        return this.baseURI;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    public CookieStore getCookieStore() {
        return this.cookieStore;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public CloseableHttpClient getClient() {
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
    @Nullable
    public <P, R> R execute(
            @Nonnull
            final StatusIndependientOperation<P, R> operation,
            @Nullable
            final P params)
    throws HttpClientException {
        return operation.execute(params, getClient());
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
}
