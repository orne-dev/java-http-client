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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.orne.http.client.cookie.CookieStore;
import dev.orne.http.client.engine.HttpClientEngine;
import dev.orne.http.client.op.StatusIndependentOperation;

/**
 * Base HTTP service client.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public class BaseHttpServiceClient
implements HttpServiceClient {

    /** The HTTP client engine. */
    private final @NotNull HttpClientEngine engine;
    /** The HTTP service's base URI. */
    private final URI baseURI;
    /** The logger for this instance's actual class. */
    private Logger logger;

    /**
     * Creates a new instance.
     * 
     * @param engine The HTTP client engine.
     */
    public BaseHttpServiceClient(
            final @NotNull HttpClientEngine engine) {
        this(engine, (URI) null);
    }

    /**
     * Creates a new instance.
     * <p>
     * The base URL, if provided, must be absolute, as is used to resolve
     * relative URIs of operations.
     * 
     * @param engine The HTTP client engine.
     * @param baseURL The HTTP service's base URL.
     * @throws URISyntaxException If the provided base URL is not a valid URI.
     */
    public BaseHttpServiceClient(
            final @NotNull HttpClientEngine engine,
            final URL baseURL)
    throws URISyntaxException {
        this(engine, baseURL == null ? null : baseURL.toURI());
    }

    /**
     * Creates a new instance.
     * <p>
     * The base URI, if provided, must be absolute, as is used to resolve
     * relative URIs of operations.
     * 
     * @param engine The HTTP client engine.
     * @param baseURI The HTTP service's base URI.
     */
    public BaseHttpServiceClient(
            final @NotNull HttpClientEngine engine,
            final URI baseURI) {
        super();
        this.engine = Validate.notNull(engine, "HTTP client engine is required.");
        this.baseURI = baseURI;
        if (this.baseURI != null) {
            Validate.isTrue(baseURI.isAbsolute(), "Base URI must be absolute.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull HttpClientEngine getEngine() {
        return this.engine;
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
        return this.engine.getCookieStore();
    }

    /**
     * Executes the specified status unaware operation for this HTTP service.
     * 
     * @param <P> The operation's parameter type
     * @param <R> The operation execution's result type
     * @param operation The operation to execute
     * @param params The operation parameter
     * @return The operation execution's result
     */
    public <P, R> @NotNull CompletableFuture<R> execute(
            final @NotNull StatusIndependentOperation<P, R> operation,
            final P params) {
        return operation.execute(params, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close()
    throws IOException {
        this.engine.close();
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
