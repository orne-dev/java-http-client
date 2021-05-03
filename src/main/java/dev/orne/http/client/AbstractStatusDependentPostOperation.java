package dev.orne.http.client;

import java.net.URI;

import javax.validation.constraints.NotNull;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;

/**
 * Abstract status dependent operation for {@code StatedHttpServiceClient}
 * based on HTTP POST requests.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <P> El execution parameters type
 * @param <E> El HTTP response entity type
 * @param <R> El execution result type
 * @param <S> The client status type
 * @since 0.1
 */
public abstract class AbstractStatusDependentPostOperation<P, E, R, S>
extends AbstractStatusDependentOperation<P, E, R, S> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull HttpPost createRequest(
            final @NotNull URI requestURI,
            final P params,
            final @NotNull S status)
    throws HttpClientException {
        final HttpPost request = new HttpPost(requestURI);
        request.setEntity(createEntity(params, status));
        return request;
    }

    /**
     * Creates the HTTP request entity.
     * 
     * @param params The operation execution parameters
     * @param status The client status
     * @return The generated HTTP entity
     * @throws HttpClientException If an exception occurs generating the
     * entity
     */
    protected abstract HttpEntity createEntity(
            P params,
            @NotNull S status)
    throws HttpClientException;
}
