/**
 * 
 */
package dev.orne.http.client;

import java.net.URI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.http.client.methods.HttpDelete;

/**
 * Abstract status independent operation for {@code HttpServiceClient} based on
 * HTTP DELETE requests.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <P> El execution parameters type
 * @param <E> El HTTP response entity type
 * @param <R> El execution result type
 * @since 0.1
 */
public abstract class AbstractStatusIndependentDeleteOperation<P, E, R>
extends AbstractStatusIndependentOperation<P, E, R> {

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    protected HttpDelete createRequest(
            @Nonnull
            final URI requestURI,
            @Nullable
            final P params)
    throws HttpClientException {
        return new HttpDelete(requestURI);
    }
}
