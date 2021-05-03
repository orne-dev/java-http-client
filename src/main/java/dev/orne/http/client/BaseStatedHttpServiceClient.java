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

import java.net.URI;
import java.net.URL;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
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
    private final @NotNull StatusInitOperation<S> statusInitOperation;
    /** The client's status. */
    private S status;

    /**
     * Creates a new instance.
     * 
     * @param baseURL The HTTP service's base URL
     * @param statusInitOperation The status initialization operation
     */
    public BaseStatedHttpServiceClient(
            final @NotNull URL baseURL,
            final @NotNull StatusInitOperation<S> statusInitOperation) {
        super(baseURL);
        this.statusInitOperation = Validate.notNull(
                statusInitOperation,
                "Status initialization operation is required");
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
            final @NotNull HttpHost host,
            final @NotNull URI baseURI,
            final @NotNull CookieStore cookieStore,
            final @NotNull CloseableHttpClient client,
            final @NotNull StatusInitOperation<S> statusInitOperation) {
        super(host, baseURI, cookieStore, client);
        this.statusInitOperation = Validate.notNull(
                statusInitOperation,
                "Status initialization operation is required");
    }

    /**
     * Returns the status initialization operation.
     * 
     * @return The status initialization operation
     */
    protected @NotNull StatusInitOperation<S> getStatusInitOperation() {
        return this.statusInitOperation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P, R> R execute(
            final @NotNull StatusDependentOperation<P, R, ? super S> operation,
            final P params)
    throws HttpClientException {
        ensureInitialized();
        return operation.execute(params, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized @NotNull S ensureInitialized()
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
    public synchronized @NotNull S initializeStatus()
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
            final S status) {
        this.status = status;
    }
}
