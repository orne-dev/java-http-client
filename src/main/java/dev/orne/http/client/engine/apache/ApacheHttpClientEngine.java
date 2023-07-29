package dev.orne.http.client.engine.apache;

/*-
 * #%L
 * Orne HTTP Client
 * %%
 * Copyright (C) 2023 Orne Developments
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpHead;
import org.apache.hc.client5.http.classic.methods.HttpOptions;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpTrace;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.io.CloseMode;

import dev.orne.http.Methods;
import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.engine.HttpClientEngine;
import dev.orne.http.client.engine.HttpRequestCustomizer;
import dev.orne.http.client.engine.HttpResponseHandler;

/**
 * Implementation of {@code HtppClientEngine} based on
 * Apache HTTP Client 5.x.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public class ApacheHttpClientEngine
implements HttpClientEngine {

    /** The HTTP client's cookie store. */
    private final @NotNull ApacheCookieStore cookieStore;
    /** The HTTP client. */
    private final @NotNull CloseableHttpClient client;
    /** The asynchronous executor service. */
    private final @NotNull ExecutorService executor;

    /**
     * Creates a new instance with the default configuration.
     * <p>
     * This includes:
     * <ul>
     * <li>A clean {@code BasicCookieStore}.</li>
     * <li>A default {@code HttpClient}.</li>
     * <li>A default cached thread pool based {@code ExecutorService.}</li>
     * </ul>
     * <p>
     * Custom configuration of {@code HttpClient} can be added overriding
     * {@code configureRequestConfig(Build)} method.
     * 
     * @see #configureRequestConfig(org.apache.http.client.config.RequestConfig.Builder)
     */
    public ApacheHttpClientEngine() {
        super();
        this.cookieStore = new ApacheCookieStore();
        final RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        configureRequestConfig(requestConfigBuilder);
        final RequestConfig requestConfig = requestConfigBuilder.build();
        this.client = HttpClients.custom()
                .setDefaultCookieStore(this.cookieStore.getDelegate())
                .setDefaultRequestConfig(requestConfig)
                .build();
        this.executor = Executors.newCachedThreadPool();
    }

    /**
     * Creates a new instance with the specified components.
     * 
     * @param cookieStore The HTTP client's cookie store.
     * @param client The HTTP client.
     * @param executor The asynchronous executor service.
     */
    public ApacheHttpClientEngine(
            final @NotNull CookieStore cookieStore,
            final @NotNull CloseableHttpClient client,
            final @NotNull ExecutorService executor) {
        super();
        this.cookieStore = new ApacheCookieStore(cookieStore);
        this.client = Validate.notNull(client, "HTTP client is required");
        this.executor = Validate.notNull(executor, "Executor service is required");
    }

    /**
     * Configures the requests build by the HTTP client.
     * The implementations should override this method to configure their
     * requests if needed.
     * <p>
     * For example:
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
    public @NotNull ApacheCookieStore getCookieStore() {
        return this.cookieStore;
    }

    /**
     * Returns the HTTP client.
     * 
     * @return The HTTP client.
     */
    protected @NotNull CloseableHttpClient getClient() {
        return this.client;
    }

    /**
     * Returns the asynchronous executor service.
     * 
     * @return The asynchronous executor service.
     */
    protected @NotNull ExecutorService getExecutor() {
        return this.executor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CompletableFuture<Void> executeHttpRequest(
            final @NotNull URI uri,
            final @NotNull String method,
            final @NotNull HttpRequestCustomizer requestCustomizer,
            final @NotNull HttpResponseHandler responseHandler)
    throws HttpClientException {
        Validate.notNull(uri);
        Validate.isTrue(uri.isAbsolute(), "The request URI must be absolute");
        final HttpHost host = new HttpHost(uri.getScheme(), uri.getHost(), uri.getPort());
        final URI path = URI.create(uri.getPath());
        final ClassicHttpRequest request = createRequest(method, path);
        requestCustomizer.customizeRequest(new ApacheHttpRequest(request));
        final HttpContext context = getHttpContext();
        final CompletableFuture<Void> responseFuture = new CompletableFuture<>();
        final CompletableFuture<Void> result = CompletableFuture.runAsync(
                () -> {
                    try {
                        this.client.execute(
                            host,
                            request,
                            context,
                            response -> {
                                responseHandler.handle(new ApacheHttpResponse(response));
                                responseFuture.complete(null);
                                return null;
                            });
                    } catch (final IOException e) {
                        responseFuture.completeExceptionally(e);
                    }
                },
                this.executor);
        return result.thenCompose(nop -> responseFuture);
    }

    /**
     * Creates a new HTTP request to the specified path and with the specified
     * HTTP method.
     * 
     * @param method The HTTP method.
     * @param path The target URI.
     * @return The HTTP request.
     * @throws HttpClientException If the specified method is not supported.
     */
    protected @NotNull ClassicHttpRequest createRequest(
            final @NotNull String method,
            final @NotNull URI path)
    throws HttpClientException {
        final ClassicHttpRequest result;
        switch (method) {
            case Methods.GET:
                result = new HttpGet(path);
                break;
            case Methods.HEAD:
                result = new HttpHead(path);
                break;
            case Methods.POST:
                result = new HttpPost(path);
                break;
            case Methods.PUT:
                result = new HttpPut(path);
                break;
            case Methods.DELETE:
                result = new HttpDelete(path);
                break;
            case Methods.OPTIONS:
                result = new HttpOptions(path);
                break;
            case Methods.TRACE:
                result = new HttpTrace(path);
                break;
            case Methods.PATCH:
                result = new HttpPatch(path);
                break;
            default:
                throw new HttpClientException(
                        String.format("Unsupported HTTP method: %s", method));
        }
        return result;
    }

    /**
     * Returns the HTTP context to use in the request.
     * Default implementation returns {@code null}.
     * 
     * @return The HTTP context, or {@code null} to use the default one
     * @throws HttpClientException If an exception occurs creating the
     * HTTP context
     */
    protected HttpContext getHttpContext()
    throws HttpClientException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close()
    throws IOException {
        this.client.close(CloseMode.GRACEFUL);
    }
}
