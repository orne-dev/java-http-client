package dev.orne.http.client;

import java.net.URI;

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

import java.net.URL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Base HTTP service client with client status.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <S> The client status type
 * @since 0.1
 */
public class BaseStatedHttpServiceClient<S>
extends BaseHttpServiceClient
implements StatedHttpServiceClient<S> {

    /** The status initialization operation. */
    @Nonnull
    private final StatusInitOperation<S> statusInitOperation;
    /** The client's status. */
    @Nullable
    private S status;

    /**
     * Creates a new instance.
     * 
     * @param baseURL The HTTP service's base URL
     * @param statusInitOperation The status initialization operation
     */
    public BaseStatedHttpServiceClient(
            @Nonnull
            final URL baseURL,
            @Nonnull
            final StatusInitOperation<S> statusInitOperation) {
        super(baseURL);
        if (statusInitOperation == null) {
            throw new IllegalArgumentException("Parameter 'statusInitOperation' is required.");
        }
        this.statusInitOperation = statusInitOperation;
    }

    /**
     * Creates a new instance.
     * 
     * @param host The HTTP service's host
     * @param baseURI The HTTP service's base URI
     * @param cookieStore The HTTP client's cookie store
     * @param client The HTTP client
     * @param statusInitOperation The status initialization operation
     */
    protected BaseStatedHttpServiceClient(
            @Nonnull
            final HttpHost host,
            @Nonnull
            final URI baseURI,
            @Nonnull
            final CookieStore cookieStore,
            @Nonnull
            final CloseableHttpClient client,
            @Nonnull
            final StatusInitOperation<S> statusInitOperation) {
        super(host, baseURI, cookieStore, client);
        this.statusInitOperation = statusInitOperation;
    }

    /**
     * Returns the status initialization operation.
     * 
     * @return The status initialization operation
     */
    protected StatusInitOperation<S> getStatusInitOperation() {
        return this.statusInitOperation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public <P, R> R execute(
            @Nonnull
            final StatusDependentOperation<P, R, ? super S> operation,
            @Nullable
            final P params)
    throws HttpClientException {
        ensureInitialized();
        return operation.execute(params, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public synchronized S ensureInitialized()
    throws HttpClientException {
        S result = getStatus();
        if (result == null) {
            result = initializeStatus();
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public synchronized S initializeStatus()
    throws HttpClientException {
        getLogger().debug("Initializing client status...");
        final S newState = this.statusInitOperation.execute(
                null,
                this);
        this.status = newState;
        getLogger().debug("Client status initialized.");
        return newState;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public synchronized S getStatus() {
        return this.status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void resetStatus() {
        setStatus(null);
    }

    /**
     * Sets the client's status. Setting to {@code null} forces client
     * status reset on next status dependent call.
     * 
     * @param status The client's status
     */
    public synchronized void setStatus(
            @Nullable
            final S status) {
        this.status = status;
    }
}
