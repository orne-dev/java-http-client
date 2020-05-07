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
 * Base HTTP service client able to authenticate against
 * the HTTP service.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <S> The client status type
 * @param <C> The credentials type
 * @since 0.1
 */
public class BaseAuthenticableHttpServiceClient<
        S extends AuthenticableClientStatus,
        C>
extends BaseStatedHttpServiceClient<S>
implements AuthenticableHttpServiceClient<S, C> {

    /** The authentication operation. */
    private final AuthenticationOperation<C, ?, S> authenticationOperation;
    /** If credentials should be stored. */
    private boolean credentialsStoringEnabled;
    /** Stored credentials. */
    @Nullable
    private C storedCredentials;
    /** If expired authentications should be renewed automatically. */
    private boolean authenticationAutoRenewalEnabled;

    /**
     * Creates a new instance.
     * 
     * @param baseURL The HTTP service's base URL
     * @param statusInitOperation The status initialization operation
     * @param authenticationOperation The authentication operation
     */
    public BaseAuthenticableHttpServiceClient(
            @Nonnull
            final URL baseURL,
            @Nonnull
            final StatusInitOperation<S> statusInitOperation,
            @Nonnull
            AuthenticationOperation<C, ?, S> authenticationOperation) {
        super(baseURL, statusInitOperation);
        this.authenticationOperation = authenticationOperation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCredentialsStoringEnabled() {
        return this.credentialsStoringEnabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCredentialsStoringEnabled(
            final boolean enabled) {
        this.credentialsStoringEnabled = enabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAuthenticationAutoRenewalEnabled() {
        return this.authenticationAutoRenewalEnabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAuthenticationAutoRenewalEnabled(
            final boolean enabled) {
        this.authenticationAutoRenewalEnabled = enabled;
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
        if (operation instanceof AuthenticatedOperation) {
            ensureAuthenticated();
        }
        try {
            return super.execute(operation, params);
        } catch (final AuthenticationExpiredException aee) {
            if (this.authenticationAutoRenewalEnabled &&
                    this.storedCredentials != null) {
                getLogger().debug("Session expired...");
                authenticate();
                return super.execute(operation, params);
            }
            throw aee;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean ensureAuthenticated()
    throws HttpClientException {
        synchronized (this) {
            if (!getStatus().isAuthenticated()) {
                authenticate();
            }
            return getStatus().isAuthenticated();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void authenticate(
            @Nonnull
            final C credentials)
    throws HttpClientException {
        synchronized (this) {
            if (this.credentialsStoringEnabled) {
                getLogger().debug("Storing credentials...");
                this.storedCredentials = credentials;
            }
            getLogger().debug("Authenticating...");
            try {
                this.authenticationOperation.execute(
                        credentials,
                        this);
            } catch (final CredentialsInvalidException cie) {
                getLogger().debug("Invalid credentials discarded.");
                this.storedCredentials = null;
                throw cie;
            }
            getLogger().debug("Authenticated.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void authenticate()
    throws HttpClientException {
        synchronized (this) {
            if (this.storedCredentials != null) {
                getLogger().debug("Authenticating with stored credentials...");
                try {
                    this.authenticationOperation.execute(
                            this.storedCredentials,
                            this);
                } catch (final CredentialsInvalidException cie) {
                    getLogger().debug("Invalid credentials discarded.");
                    this.storedCredentials = null;
                    throw cie;
                }
                getLogger().debug("Authenticated.");
            } else {
                throw new CredentialsNotStoredException(
                        "No stored credentials. Call authenticate(credentials) first.");
            }
        }
    }
}
