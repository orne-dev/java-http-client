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

import java.net.URL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
        this.statusInitOperation = statusInitOperation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public <P, R> R execute(
            @Nonnull
            final StatusDependentOperation<P, R, S> operation,
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
    public S ensureInitialized()
    throws HttpClientException {
        synchronized (this) {
            S result = getStatus();
            if (result == null) {
                result = initializeStatus();
            }
            return result;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public S initializeStatus()
    throws HttpClientException {
        synchronized (this) {
            getLogger().info("Initializing client status");
            final S newState = this.statusInitOperation.execute(
                    null,
                    this);
            this.status = newState;
            return newState;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public S getStatus() {
        synchronized (this) {
            return this.status;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetState() {
        setStatus(null);
    }

    /**
     * Sets the client's status. Setting to {@code null} forces client
     * status reset on next status dependent call.
     * 
     * @param status The client's status
     */
    public void setStatus(
            @Nullable
            final S status) {
        synchronized (this) {
            this.status = status;
        }
    }
}
