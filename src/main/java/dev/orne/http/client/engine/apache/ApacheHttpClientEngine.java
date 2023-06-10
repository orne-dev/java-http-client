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
import java.util.concurrent.Future;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;

import dev.orne.http.Methods;
import dev.orne.http.client.CookieStore;
import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.engine.HttpClientEngine;
import dev.orne.http.client.engine.HttpRequestBodySupplier;
import dev.orne.http.client.engine.HttpRequestHeadersSupplier;
import dev.orne.http.client.engine.HttpResponseHandler;

/**
 * Implementation of {@code HtppClientEngine} based on
 * Apache HTTP Client 4.x.
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
            final @NotNull org.apache.http.client.CookieStore cookieStore,
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
    public @NotNull CookieStore getCookieStore() {
        return this.cookieStore;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Future<Void> executeHttpRequest(
            final @NotNull URI uri,
            final @NotNull String method,
            final HttpRequestHeadersSupplier headers,
            final HttpRequestBodySupplier body,
            final @NotNull HttpResponseHandler handler)
    throws HttpClientException {
        Validate.notNull(uri);
        Validate.isTrue(uri.isAbsolute(), "The request URI must be absolute");
        final HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        final URI path = URI.create(uri.getPath());
        final HttpRequest request = createRequest(method, path);
        if (headers != null) {
            headers.setHeaders((String header, String... values) -> {
                for (final String value : values) {
                    request.addHeader(header, value);
                }
            });
        }
        if (request instanceof HttpEntityEnclosingRequest && body != null) {
            body.setBody((content, length) -> {
                final HttpEntity entity = new InputStreamEntity(content, length);
                ((HttpEntityEnclosingRequest) request).setEntity(entity);
            });
        }
        final CompletableFuture<Void> result = new CompletableFuture<>();
        this.executor.submit(() -> {
            try (final CloseableHttpResponse response = this.client.execute(
                    host,
                    request,
                    getHttpContext())) {
                handler.handle(
                        response.getStatusLine().getStatusCode(),
                        response.getStatusLine().getReasonPhrase(),
                        header -> Stream.of(response.getHeaders(header))
                                    .map(Header::getElements)
                                    .flatMap(Stream::of)
                                    .map(HeaderElement::getValue)
                                    .toArray(String[]::new),
                        () -> {
                            try {
                                return response.getEntity().getContent();
                            } catch (UnsupportedOperationException | IOException e) {
                                throw new HttpClientException("", e);
                            }
                        });
                result.complete(null);
            } catch (final HttpClientException | IOException e) {
                result.completeExceptionally(e);
            }
        });
        return result;
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
    protected @NotNull HttpRequest createRequest(
            final @NotNull String method,
            final @NotNull URI path)
    throws HttpClientException {
        final HttpRequest result;
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
}
