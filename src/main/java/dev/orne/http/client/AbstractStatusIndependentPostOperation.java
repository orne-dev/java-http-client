/**
 * 
 */
package dev.orne.http.client;

import java.net.URI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
    @Nonnull
    protected HttpPost createRequest(
            @Nonnull
            final URI requestURI,
            @Nullable
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
    @Nullable
    protected abstract HttpEntity createEntity(
            @Nullable
            P params)
    throws HttpClientException;
}
