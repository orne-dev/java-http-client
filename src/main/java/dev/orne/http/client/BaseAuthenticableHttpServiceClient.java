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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;

import dev.orne.http.client.engine.HttpClientEngine;
import dev.orne.http.client.op.AuthenticatedOperation;
import dev.orne.http.client.op.AuthenticationOperation;
import dev.orne.http.client.op.StatusDependentOperation;
import dev.orne.http.client.op.StatusInitOperation;

/**
 * Base HTTP service client able to authenticate against
 * the HTTP service.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
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
     * @param engine The HTTP client engine.
     * @param statusInitOperation The status initialization operation
     */
    public BaseAuthenticableHttpServiceClient(
            final @NotNull HttpClientEngine engine,
            final @NotNull StatusInitOperation<S> statusInitOperation,
            final @NotNull AuthenticationOperation<C, ?, S> authenticationOperation) {
        this(engine, (URI) null, statusInitOperation, authenticationOperation);
    }

    /**
     * Creates a new instance.
     * <p>
     * The base URL, if provided, must be absolute, as is used to resolve
     * relative URIs of operations.
     * 
     * @param engine The HTTP client engine.
     * @param baseURL The HTTP service's base URL.
     * @param statusInitOperation The status initialization operation
     * @throws URISyntaxException If the provided base URL is not a valid URI.
     */
    public BaseAuthenticableHttpServiceClient(
            final @NotNull HttpClientEngine engine,
            final URL baseURL,
            final @NotNull StatusInitOperation<S> statusInitOperation,
            final @NotNull AuthenticationOperation<C, ?, S> authenticationOperation)
    throws URISyntaxException {
        super(engine, baseURL, statusInitOperation);
        this.authenticationOperation = Validate.notNull(
                authenticationOperation,
                "Authentication operation is required.");
    }

    /**
     * Creates a new instance.
     * 
     * @param engine The HTTP client engine.
     * @param baseURI The HTTP service's base URI
     * @param statusInitOperation The status initialization operation
     */
    public BaseAuthenticableHttpServiceClient(
            final @NotNull HttpClientEngine engine,
            final URI baseURI,
            final @NotNull StatusInitOperation<S> statusInitOperation,
            final @NotNull AuthenticationOperation<C, ?, S> authenticationOperation) {
        super(engine, baseURI, statusInitOperation);
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
    public final synchronized boolean hasStoredCredentials() {
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
    public synchronized @NotNull CompletableFuture<@NotNull S> authenticate() {
        final CompletableFuture<S> result;
        if (this.storedCredentials != null) {
            getLogger().debug("Authenticating with stored credentials...");
            result = executeAuthentication(this.storedCredentials);
            result.handle((status, t) -> {
                    if (t == null ) {
                        getLogger().debug("Authenticated.");
                    } else {
                        final HttpClientException ex = HttpClientException.unwrapFutureException(t);
                        if (ex instanceof CredentialsInvalidException) {
                            getLogger().debug("Invalid credentials discarded.");
                            this.storedCredentials = null;
                        }
                    }
                    return status;
            });
        } else {
            result = FutureUtils.completableFailure(new CredentialsNotStoredException(
                    "No stored credentials. Call authenticate(credentials) first."));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized @NotNull CompletableFuture<@NotNull S> authenticate(
            final @NotNull C credentials) {
        getLogger().debug("Authenticating...");
        final CompletableFuture<S> result = executeAuthentication(this.storedCredentials);
        return result.thenApply(status -> {
            if (this.credentialsStoringEnabled) {
                getLogger().debug("Storing credentials...");
                this.storedCredentials = credentials;
            }
            getLogger().debug("Authenticated.");
            return status;
        });
    }

    protected @NotNull CompletableFuture<@NotNull S> executeAuthentication(
            final @NotNull C credentials) {
        return ensureInitialized().thenCompose(status ->
            super.execute(this.authenticationOperation, this.storedCredentials)
            .thenApply(nop -> status)
        );
    }

    /**
     * {@inheritDoc}
     */
    public synchronized @NotNull CompletableFuture<@NotNull S> ensureAuthenticated() {
        return ensureInitialized().thenCompose(status -> {
            if (status.isAuthenticated()) {
                return CompletableFuture.completedFuture(status);
            } else {
                return authenticate();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P, R> @NotNull CompletableFuture<@NotNull R> execute(
            final @NotNull StatusDependentOperation<P, R, ? super S> operation,
            final P params) {
        final CompletableFuture<R> future = new CompletableFuture<>();
        final CompletableFuture<R> base;
        if (operation instanceof AuthenticatedOperation) {
            base = ensureAuthenticated().thenCompose(status -> super.execute(operation, params));
        } else {
            base = super.execute(operation, params);
        }
        base.whenComplete((result, t) -> {
            if (t == null) {
                future.complete(result);
            } else if (t instanceof AuthenticationExpiredException) {
                final AuthenticationAutoRenewalPolicy policy =
                        authenticationOperation.getAutoRenewalPolicy();
                if (policy == null) {
                    future.completeExceptionally(t);
                } else {
                    policy.apply(
                            this::authenticate,
                            status -> super.execute(operation, params),
                            future);
                }
            } else {
                future.completeExceptionally(t);
            }
        });
        return future;
    }
}
