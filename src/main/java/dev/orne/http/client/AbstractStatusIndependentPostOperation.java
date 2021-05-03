package dev.orne.http.client;

import java.net.URI;

import javax.validation.constraints.NotNull;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;

/**
 * Abstract status independent operation for {@code HttpServiceClient} based on
 * HTTP POST requests.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <P> El execution parameters type
 * @param <E> El HTTP response entity type
 * @param <R> El execution result type
 * @since 0.1
 */
public abstract class AbstractStatusIndependentPostOperation<P, E, R>
extends AbstractStatusIndependentOperation<P, E, R> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull HttpPost createRequest(
            final @NotNull URI requestURI,
            final P params)
    throws HttpClientException {
        final HttpPost request = new HttpPost(requestURI);
        request.setEntity(createEntity(params));
        return request;
    }

    /**
     * Creates the HTTP request entity.
     * 
     * @param params The operation execution parameters
     * @return The generated HTTP entity
     * @throws HttpClientException If an exception occurs generating the
     * entity
     */
    protected abstract HttpEntity createEntity(
            P params)
    throws HttpClientException;
}
