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

import java.util.concurrent.CompletableFuture;
import javax.validation.constraints.NotNull;

import dev.orne.http.client.op.StatusDependentOperation;

/**
 * HTTP service client interface with client status.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @param <S> The client status type
 * @since 0.1
 */
public interface StatedHttpServiceClient<S>
extends HttpServiceClient {

    /**
     * Ensures that this client's status has been initialized.
     * 
     * @return The client's status
     */
    public @NotNull CompletableFuture<@NotNull S> ensureInitialized();

    /**
     * Initializes client's status.
     * 
     * @return The new client status
     */
    public @NotNull CompletableFuture<@NotNull S> initializeStatus();

    /**
     * Executes the specified status aware operation for this HTTP service
     * with this client status.
     * 
     * @param <P> The operation's parameter type
     * @param <R> The operation execution's result type
     * @param operation The operation to execute
     * @param params The operation parameter
     * @return The operation execution's result
     */
    public <P, R> @NotNull CompletableFuture<R> execute(
            final @NotNull StatusDependentOperation<P, R, ? super S> operation,
            final P params);

    /**
     * Returns the client's current state.
     * <p>
     * If the state has not been initialized {@code null} is returned.
     * To ensure a non null result use {@code ensureInitialized().get()}.
     * 
     * @return The client's current state
     * @see #ensureInitialized()
     */
    S getStatus();

    /**
     * Resets the client's state.
     */
    void resetStatus();
}
