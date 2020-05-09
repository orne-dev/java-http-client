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
 * Operation for {@code HttpServiceClient} dependent of clients status
 * that requires the client to be authenticated before execution the
 * operation.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <P> El execution parameters type
 * @param <R> El execution result type
 * @param <S> The authenticable client status type
 * @since 0.1
 */
public interface AuthenticatedOperation<
        P,
        R,
        S extends AuthenticableClientStatus>
extends StatusDependentOperation<P, R, S> {

    /**
     * {@inheritDoc}
     * @throws AuthenticationRequiredException If client is not
     * authenticated
     */
    @Override
    R execute(
            @Nullable
            P params,
            @Nonnull
            StatedHttpServiceClient<? extends S> client)
    throws HttpClientException;
}
