package dev.orne.http.client;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

/**
 * HTTP service client interface with client status.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <S> The client status type
 * @since 0.1
 */
public interface StatedHttpServiceClient<S>
extends HttpServiceClient {

    /**
     * Ensures that this client's status has been initialized.
     * 
     * @return The client's status, never {@code null}
     * @throws HttpClientException If an error occurs initializing the status
     */
    @Nonnull
    public S ensureInitialized()
    throws HttpClientException;

    /**
     * Initializes client's status.
     * 
     * @return The new client status
     * @throws HttpClientException If an error occurs initializing the status
     */
    @Nonnull
    public S initializeStatus()
    throws HttpClientException;

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
    throws HttpClientException;

    /**
     * Returns the client's current state.
     * 
     * @return The client's current state
     */
    @Nullable
    S getStatus();

    /**
     * Resets the client's state.
     */
    void resetStatus();
}
