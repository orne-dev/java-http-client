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

import javax.validation.constraints.NotNull;

/**
 * Operation for {@code HttpServiceClient} independent of client's
 * status.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <P> El execution parameters type
 * @param <R> El execution result type
 * @since 0.1
 */
public interface StatusIndependentOperation<P, R> {

    /**
     * Executes the operation.
     * 
     * @param params The operation execution parameters
     * @param client The client to execute the operation
     * @return The operation execution result
     * @throws HttpClientException If there is an error executing the operation
     */
    public R execute(
            P params,
            @NotNull HttpServiceClient client)
    throws HttpClientException;
}
