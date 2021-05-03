package dev.orne.http.client;

import java.net.URI;

import javax.validation.constraints.NotNull;

import org.apache.http.client.methods.HttpDelete;

/**
 * Abstract status dependent operation for {@code StatedHttpServiceClient}
 * based on HTTP DELETE requests.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <P> El execution parameters type
 * @param <E> El HTTP response entity type
 * @param <R> El execution result type
 * @param <S> The client status type
 * @since 0.1
 */
public abstract class AbstractStatusDependentDeleteOperation<P, E, R, S>
extends AbstractStatusDependentOperation<P, E, R, S> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull HttpDelete createRequest(
            final @NotNull URI requestURI,
            final P params,
            final @NotNull S status)
    throws HttpClientException {
        return new HttpDelete(requestURI);
    }
}
