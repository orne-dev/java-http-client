/**
 * 
 */
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Operation for {@code HttpServiceClient} that authenticates on HTTP service.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <P> El authentication parameters type
 * @param <R> El authentication result type
 * @param <S> The client status type
 * @since 0.1
 */
public interface AuthenticationOperation<P, R, S>
extends StatusDependentOperation<P, R, S> {

    /**
     * {@inheritDoc}
     * @throws HttpAuthenticationFailedException If the authentication attempt
     * failed
     */
    @Override
    @Nullable
    public R execute(
            @Nullable
            P params,
            @Nonnull
            StatedHttpServiceClient<S> client)
    throws HttpClientException;
}
