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
import dev.orne.http.client.op.StatusDependentOperation;
import dev.orne.http.client.op.StatusInitOperation;

/**
 * Base HTTP service client with client status.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <S> The client status type
 * @since 0.1
 */
public class BaseStatedHttpServiceClient<S>
extends BaseHttpServiceClient
implements StatedHttpServiceClient<S> {

    /** The status initialization operation. */
    private final @NotNull StatusInitOperation<S> statusInitOperation;
    /** The client's status. */
    private S status;

    /**
     * Creates a new instance.
     * 
     * @param engine The HTTP client engine.
     * @param baseURI The HTTP service's base URI
     * @param statusInitOperation The status initialization operation
     */
    public BaseStatedHttpServiceClient(
            final @NotNull HttpClientEngine engine,
            final @NotNull URI baseURI,
            final @NotNull StatusInitOperation<S> statusInitOperation) {
        super(engine, baseURI);
        this.statusInitOperation = Validate.notNull(
                statusInitOperation,
                "Status initialization operation is required");
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
    public BaseStatedHttpServiceClient(
            final @NotNull HttpClientEngine engine,
            final @NotNull URL baseURL,
            final @NotNull StatusInitOperation<S> statusInitOperation)
    throws URISyntaxException {
        super(engine, baseURL);
        this.statusInitOperation = Validate.notNull(
                statusInitOperation,
                "Status initialization operation is required");
    }

    /**
     * Returns the status initialization operation.
     * 
     * @return The status initialization operation
     */
    protected @NotNull StatusInitOperation<S> getStatusInitOperation() {
        return this.statusInitOperation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P, R> @NotNull CompletionStage<R> execute(
            final @NotNull StatusDependentOperation<P, R, ? super S> operation,
            final P params) {
        return ensureInitialized().thenCompose(opStatus -> operation.execute(params, opStatus, this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized @NotNull CompletionStage<@NotNull S> ensureInitialized() {
        final CompletionStage<@NotNull S> result;
        if (this.status == null) {
            result = initializeStatus();
        } else {
            result = CompletableFuture.completedFuture(this.status);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized @NotNull CompletionStage<@NotNull S> initializeStatus() {
        final Logger logger = LoggerFactory.getLogger(getClass());
        logger.debug("Initializing client status...");
        return this.statusInitOperation.execute(
                null,
                this)
        .thenApply(res -> {
            this.status = res;
            logger.debug("Client status initialized.");
            return res;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized S getStatus() {
        return this.status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void resetStatus() {
        setStatus(null);
    }

    /**
     * Sets the client's status. Setting to {@code null} forces client
     * status reset on next status dependent call.
     * 
     * @param status The client's status
     */
    public synchronized void setStatus(
            final S status) {
        this.status = status;
    }
}
