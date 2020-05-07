/**
 * 
 */
package dev.orne.http.client;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

/**
 * Abstract status dependent operation for {@code StatedHttpServiceClient}
 * based on HTTP POST requests with URL encoding entity.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <P> El execution parameters type
 * @param <E> El HTTP response entity type
 * @param <R> El execution result type
 * @param <S> The client status type
 * @since 0.1
 */
public abstract class AbstractStatusDependentUrlEncodedPostOperation<P, E, R, S>
extends AbstractStatusDependentPostOperation<P, E, R, S> {

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    protected HttpEntity createEntity(
            @Nullable
            final P params,
            @Nonnull
            final S state)
    throws HttpClientException {
        final List<NameValuePair> requestParams = createEntityParams(
                params, state);
        return new UrlEncodedFormEntity(requestParams, StandardCharsets.UTF_8);
    }

    /**
     * Creates the URL encoded HTTP request entity parameters.
     * 
     * @param params The operation execution parameters
     * @param status The client status
     * @return The generated HTTP entity parameters
     * @throws HttpClientException If an exception occurs generating the
     * entity parameters
     */
    @Nonnull
    protected abstract List<NameValuePair> createEntityParams(
            @Nullable
            P params,
            @Nonnull
            final S state)
    throws HttpClientException;
}
