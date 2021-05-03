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

import java.net.URI;
import java.net.URL;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.CloseableHttpClient;

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
    private final @NotNull AuthenticationOperation<C, ?, S> authenticationOperation;
    /** If credentials should be stored. */
    private boolean credentialsStoringEnabled;
    /** The stored credentials. */
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
            final @NotNull URL baseURL,
            final @NotNull StatusInitOperation<S> statusInitOperation,
            final @NotNull AuthenticationOperation<C, ?, S> authenticationOperation) {
        super(baseURL, statusInitOperation);
        this.authenticationOperation = Validate.notNull(
                authenticationOperation,
                "Authentication operation is required.");
    }

    /**
     * Creates a new instance.
     * 
     * @param host The HTTP service's host
     * @param baseURI The HTTP service's base URI
     * @param cookieStore The HTTP client's cookie store
     * @param client The HTTP client
     * @param statusInitOperation The status initialization operation
     * @param authenticationOperation The authentication operation
     */
    protected BaseAuthenticableHttpServiceClient(
            final @NotNull HttpHost host,
            final @NotNull URI baseURI,
            final @NotNull CookieStore cookieStore,
            final @NotNull CloseableHttpClient client,
            final @NotNull StatusInitOperation<S> statusInitOperation,
            @NotNull AuthenticationOperation<C, ?, S> authenticationOperation) {
        super(host, baseURI, cookieStore, client, statusInitOperation);
        this.authenticationOperation = Validate.notNull(
                authenticationOperation,
                "Authentication operation is required.");
    }

    /**
     * Returns the authentication operation.
     * 
     * @return The authentication operation
     */
    protected @NotNull AuthenticationOperation<C, ?, S> getAuthenticationOperation() {
        return this.authenticationOperation;
    }

    /**
     * Returns {@code true} if any credentials are stored.
     * 
     * @return If stored credentials exists
     */
    protected final synchronized boolean hasStoredCredentials() {
        return this.storedCredentials != null;
    }

    /**
     * Stores the specified credentials for later usage in
     * {@link #authenticate()} calls.
     * 
     * @param credentials The credentials to store
     */
    protected final synchronized void setStoredCredentials(
            final C credentials) {
        this.storedCredentials = credentials;
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
    public synchronized void authenticate()
    throws HttpClientException {
        if (this.storedCredentials != null) {
            getLogger().debug("Authenticating with stored credentials...");
            try {
                super.execute(this.authenticationOperation, this.storedCredentials);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void authenticate(
            final @NotNull C credentials)
    throws HttpClientException {
        getLogger().debug("Authenticating...");
        super.execute(this.authenticationOperation, credentials);
        getLogger().debug("Authenticated.");
        if (this.credentialsStoringEnabled) {
            getLogger().debug("Storing credentials...");
            this.storedCredentials = credentials;
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void ensureAuthenticated()
    throws HttpClientException {
        if (!ensureInitialized().isAuthenticated()) {
            authenticate();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P, R> R execute(
            final @NotNull StatusDependentOperation<P, R, ? super S> operation,
            final P params)
    throws HttpClientException {
        if (operation instanceof AuthenticatedOperation) {
            ensureAuthenticated();
        }
        try {
            return super.execute(operation, params);
        } catch (final AuthenticationExpiredException aee) {
            ensureInitialized().resetAuthentication();
            if (this.authenticationAutoRenewalEnabled &&
                    this.storedCredentials != null) {
                getLogger().debug("Session expired...");
                authenticate();
                return super.execute(operation, params);
            }
            throw aee;
        }
    }
}
