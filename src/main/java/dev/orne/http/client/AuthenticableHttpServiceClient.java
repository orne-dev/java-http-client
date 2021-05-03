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
 * HTTP service client interface able to authenticate against
 * the HTTP service.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <S> The client status type
 * @param <C> The credentials type
 * @since 0.1
 */
public interface AuthenticableHttpServiceClient<S, C>
extends StatedHttpServiceClient<S> {

    /**
     * Returns {@code true} if credentials should be stored on calls to
     * {@link #authenticate(Object)}.
     * 
     * @return If credentials should be stored
     */
    boolean isCredentialsStoringEnabled();

    /**
     * Sets if credentials should be stored on calls to
     * {@link #authenticate(Object)}.
     * 
     * @param enabled If credentials should be stored
     */
    void setCredentialsStoringEnabled(
            boolean enabled);

    /**
     * Returns {@code true} if expired authentications should be renewed
     * automatically. Requires {@link #setCredentialsStoringEnabled(boolean)}
     * and storing credentials calling to {@link #authenticate(Object)}.
     * 
     * @return If expired authentications should be renewed automatically
     */
    boolean isAuthenticationAutoRenewalEnabled();

    /**
     * Sets if expired authentications should be renewed automatically.
     * Requires {@link #setCredentialsStoringEnabled(boolean)} and storing
     * credentials calling to {@link #authenticate(Object)}.
     * 
     * @param enabled  If expired authentications should be renewed
     * automatically
     */
    void setAuthenticationAutoRenewalEnabled(
            boolean enabled);

    /**
     * Ensures that this client has been authenticated against the HTTP service.
     * If it's not authenticated and credentials have been stored by a previous
     * call to {@link #authenticate(Object)} tries to authenticate with stored
     * credentials.
     * 
     * @throws HttpClientException If an error occurs during the authentication
     * process
     */
    void ensureAuthenticated()
    throws HttpClientException;

    /**
     * Authenticates against the HTTP service using the specified credentials.
     * If the storage of credentials is allowed the credentials are stored
     * for future call to {@link #authenticate()}.
     * 
     * @param credentials The credentials to use
     * @throws HttpClientException If an error occurs during the authentication
     * process
     */
    void authenticate(
            @NotNull C credentials)
    throws HttpClientException;

    /**
     * Authenticates against the HTTP service using the stored credentials.
     * If no credentials
     * 
     * @throws AuthenticationRequiredException If no credentials have
     * been store with a previous call to {@link #authenticate(Object)}
     * @throws HttpClientException If an error occurs during the authentication
     * process
     */
    void authenticate()
    throws HttpClientException;
}
