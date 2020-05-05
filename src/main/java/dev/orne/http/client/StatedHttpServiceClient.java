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
public class StatedHttpServiceClient<S>
extends BaseHttpServiceClient {

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
    public StatedHttpServiceClient(
            @Nonnull
            final URL baseURL,
            @Nonnull
            final StatusInitOperation<S> statusInitOperation) {
        super(baseURL);
        this.statusInitOperation = statusInitOperation;
    }

    /**
     * Executes the specified status aware operation for this HTTP service
     * with this client status.
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
            final StatusDependentOperation<P, R, S> operation,
            @Nullable
            final P params)
    throws HttpClientException {
        return operation.execute(params, ensureInitialized(), getClient());
    }

    /**
     * Ensures that this client's status has been initialized.
     * 
     * @return The client's status, never {@code null}
     * @throws HttpClientException If an error occurs initializing the status
     */
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
     * Initializes client's status.
     * 
     * @return The new client status
     * @throws HttpClientException If an error occurs initializing the status
     */
    @Nonnull
    public S initializeStatus()
    throws HttpClientException {
        synchronized (this) {
            final S newState = this.statusInitOperation.execute(
                    null,
                    getClient());
            this.status = newState;
            return newState;
        }
    }

    /**
     * Returns the client's status.
     * 
     * @return The client's status
     */
    @Nullable
    public S getStatus() {
        synchronized (this) {
            return this.status;
        }
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
