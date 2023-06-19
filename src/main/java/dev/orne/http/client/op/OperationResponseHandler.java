package dev.orne.http.client.op;

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

import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.engine.HttpResponseHandler;

/**
 * Interface for response handlers of HTTP service operations.
 * <p>
 * The response handler must:
 * <ol>
 * <li>Check the HTTP response's status code.</li>
 * <li>Process the HTTP response's body, if any.</li>
 * <li>Create a result of the declared type.</li>
 * </ol>
 * <p>
 * Throw exceptions will be wrapped on {@code CompletionException}
 * when rethrown by the {@code CompletableFuture} returned by the operation.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @param <R> The type of the operation result.
 * @since 0.1
 */
public interface OperationResponseHandler<R>
extends HttpResponseHandler {

    /**
     * Returns the result of the operation.
     * <p>
     * If an error occurred during HTTP request or HTTP response processing
     * an exception must be throw.
     * 
     * @return The result of the operation.
     * @throws HttpClientException The error occurred during HTTP request or
     * response processing.
     */
    R getResult()
    throws HttpClientException;
}
