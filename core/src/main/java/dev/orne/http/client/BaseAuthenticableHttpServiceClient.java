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
import java.util.concurrent.CompletionStage;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * <p>
     * The base URI must be absolute, as is used to resolve
     * relative URIs of operations.
     * <p>
     * The HTTP engine to use is loaded through SPI.
     * 
     * @param baseURI The HTTP service's base URI.
     * @param statusInitOperation The status initialization operation.
     * @param authenticationOperation The authentication operation.
     * @see HttpClientEngine#fromSpi()
     */
    public BaseAuthenticableHttpServiceClient(
            final @NotNull URI baseURI,
            final @NotNull StatusInitOperation<S> statusInitOperation,
            final @NotNull AuthenticationOperation<C, ?, S> authenticationOperation) {
        this(HttpClientEngine.fromSpi(), baseURI, statusInitOperation, authenticationOperation);
    }

    /**
     * Creates a new instance.
     * <p>
     * The base URL, must be absolute, as is used to resolve
     * relative URIs of operations.
     * <p>
     * The HTTP engine to use is loaded through SPI.
     * 
     * @param baseURL The HTTP service's base URL.
     * @param statusInitOperation The status initialization operation.
     * @param authenticationOperation The authentication operation.
     * @throws URISyntaxException If the provided base URL is not a valid URI.
     */
    public BaseAuthenticableHttpServiceClient(
            final @NotNull URL baseURL,
            final @NotNull StatusInitOperation<S> statusInitOperation,
            final @NotNull AuthenticationOperation<C, ?, S> authenticationOperation)
    throws URISyntaxException {
        this(HttpClientEngine.fromSpi(), baseURL, statusInitOperation, authenticationOperation);
    }

    /**
     * Creates a new instance.
     * 
     * @param engine The HTTP client engine.
     * @param baseURI The HTTP service's base URI
     * @param statusInitOperation The status initialization operation.
     * @param authenticationOperation The authentication operation.
     */
    public BaseAuthenticableHttpServiceClient(
            final @NotNull HttpClientEngine engine,
            final @NotNull URI baseURI,
            final @NotNull StatusInitOperation<S> statusInitOperation,
            final @NotNull AuthenticationOperation<C, ?, S> authenticationOperation) {
        super(engine, baseURI, statusInitOperation);
        this.authenticationOperation = Validate.notNull(
                authenticationOperation,
                "Authentication operation is required.");
    }

    /**
     * Creates a new instance.
     * <p>
     * The base URL, if provided, must be absolute, as is used to resolve
     * relative URIs of operations.
     * 
     * @param engine The HTTP client engine.
     * @param baseURL The HTTP service's base URL.
     * @param statusInitOperation The status initialization operation.
     * @param authenticationOperation The authentication operation.
     * @throws URISyntaxException If the provided base URL is not a valid URI.
     */
    public BaseAuthenticableHttpServiceClient(
            final @NotNull HttpClientEngine engine,
            final @NotNull URL baseURL,
            final @NotNull StatusInitOperation<S> statusInitOperation,
            final @NotNull AuthenticationOperation<C, ?, S> authenticationOperation)
    throws URISyntaxException {
        super(engine, baseURL, statusInitOperation);
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
        if (this.credentialsStoringEnabled) {
            this.storedCredentials = credentials;
        }
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
        if (!enabled) {
            this.storedCredentials = null;
        }
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
    public synchronized @NotNull CompletionStage<@NotNull S> authenticate() {
        final CompletionStage<S> result;
        if (this.storedCredentials != null) {
            final Logger logger = LoggerFactory.getLogger(getClass());
            logger.debug("Authenticating with stored credentials...");
            result = executeAuthentication(this.storedCredentials);
            result.handle((status, t) -> {
                    if (t == null ) {
                        logger.debug("Authenticated.");
                    } else {
                        final HttpClientException ex = HttpClientException.unwrapFutureException(t);
                        if (ex instanceof CredentialsInvalidException) {
                            logger.debug("Invalid credentials discarded.");
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
    public synchronized @NotNull CompletionStage<@NotNull S> authenticate(
            final @NotNull C credentials) {
        final Logger logger = LoggerFactory.getLogger(getClass());
        logger.debug("Authenticating...");
        return executeAuthentication(credentials)
        .thenApply(status -> {
            if (this.credentialsStoringEnabled) {
                logger.debug("Storing credentials...");
                this.storedCredentials = credentials;
            }
            logger.debug("Authenticated.");
            return status;
        });
    }

    /**
     * Executes the authentication operation with the specified credentials,
     * returning the updated status.
     * 
     * @param credentials The credentials to use in the authentication attempt.
     * @return The updated client status.
     */
    protected @NotNull CompletionStage<@NotNull S> executeAuthentication(
            final @NotNull C credentials) {
        return execute(this.authenticationOperation, credentials)
            .thenApply(nop -> getStatus());
    }

    /**
     * {@inheritDoc}
     */
    public synchronized @NotNull CompletionStage<@NotNull S> ensureAuthenticated() {
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
    public <P, R> @NotNull CompletionStage<@NotNull R> execute(
            final @NotNull StatusDependentOperation<P, R, ? super S> operation,
            final P params) {
        if (operation instanceof AuthenticatedOperation) {
            return executeAuthenticated((AuthenticatedOperation<P, R, ? super S>) operation, params);
        } else {
            return super.execute(operation, params);
        }
    }

    /**
     * Executes the specified authenticated for this HTTP service
     * with this client status.
     * <p>
     * If the operation throws an {@code AuthenticationExpiredException}
     * and the client has the authentication auto renewal enabled the
     * authentication auto renewal policy is called.
     * 
     * @param <P> The operation's parameter type
     * @param <R> The operation execution's result type
     * @param operation The operation to execute
     * @param params The operation parameter
     * @return The operation execution's result
     */
    protected <P, R> @NotNull CompletionStage<@NotNull R> executeAuthenticated(
            final @NotNull AuthenticatedOperation<P, R, ? super S> operation,
            final P params) {
        final CompletableFuture<R> future = new CompletableFuture<>();
        final CompletionStage<R> base = ensureAuthenticated()
                .thenCompose(status -> super.execute(operation, params));
        base.whenComplete((result, t) -> {
            if (t == null) {
                future.complete(result);
            } else {
                final Exception e = HttpClientException.unwrapFutureException(t);
                if (e instanceof AuthenticationExpiredException) {
                    final AuthenticationAutoRenewalPolicy policy =
                            authenticationOperation.getAutoRenewalPolicy();
                    if (policy == null || !isAuthenticationAutoRenewalEnabled()) {
                        future.completeExceptionally(e);
                    } else {
                        policy.apply(
                                this::authenticate,
                                status -> operation.execute(params, status, this),
                                future);
                    }
                } else {
                    future.completeExceptionally(e);
                }
            }
        });
        return future;
    }
}
