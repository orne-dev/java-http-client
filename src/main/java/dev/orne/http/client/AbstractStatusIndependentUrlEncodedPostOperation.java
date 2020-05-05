/**
 * 
 */
package dev.orne.http.client;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

/**
 * Abstract status independent operation for {@code HttpServiceClient} based on
 * HTTP POST requests with URL encoding entity.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <P> El execution parameters type
 * @param <E> El HTTP response entity type
 * @param <R> El execution result type
 * @since 0.1
 */
public abstract class AbstractStatusIndependentUrlEncodedPostOperation<P, E, R>
extends AbstractStatusIndependentPostOperation<P, E, R> {

    /**
     * Creates a new instance.
     * 
     * @param operationURI The relative URI of the operation
     */
    public AbstractStatusIndependentUrlEncodedPostOperation(
            final URI operationURI) {
        super(operationURI);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    protected HttpEntity createEntity(
            @Nullable
            final P params)
    throws HttpClientException {
        final List<NameValuePair> requestParams = createEntityParams(params);
        return new UrlEncodedFormEntity(requestParams, StandardCharsets.UTF_8);
    }

    /**
     * Creates the URL encoded HTTP request entity parameters.
     * 
     * @param params The operation execution parameters
     * @return The generated HTTP entity parameters
     * @throws HttpClientException If an exception occurs generating the
     * entity parameters
     */
    @Nonnull
    protected abstract List<NameValuePair> createEntityParams(
            @Nullable
            P params)
    throws HttpClientException;
}
