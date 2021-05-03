package dev.orne.http.client;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

/**
 * Abstract status independent operation for {@code HttpServiceClient} based on
 * HTTP PUT requests with URL encoding entity.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <P> El execution parameters type
 * @param <E> El HTTP response entity type
 * @param <R> El execution result type
 * @since 0.1
 */
public abstract class AbstractStatusIndependentUrlEncodedPutOperation<P, E, R>
extends AbstractStatusIndependentPutOperation<P, E, R> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected UrlEncodedFormEntity createEntity(
            final P params)
    throws HttpClientException {
        final List<NameValuePair> requestParams = createEntityParams(params);
        final UrlEncodedFormEntity entity;
        if (requestParams == null || requestParams.isEmpty()) {
            entity = null;
        } else {
            entity = new UrlEncodedFormEntity(requestParams, 
                    getEntityCharset(params));
        }
        return entity;
    }

    /**
     * Returns the {@code Charset} to use in request entity.
     * 
     * @param params The operation execution parameters
     * @return The {@code Charset} to use in request entity
     * @throws HttpClientException If an exception occurs resolving the
     * entity's {@code Charset}
     */
    protected @NotNull Charset getEntityCharset(
            final P params)
    throws HttpClientException {
        return StandardCharsets.UTF_8;
    }

    /**
     * Creates the URL encoded HTTP request entity parameters.
     * 
     * @param params The operation execution parameters
     * @return The generated HTTP entity parameters
     * @throws HttpClientException If an exception occurs generating the
     * entity parameters
     */
    protected abstract List<NameValuePair> createEntityParams(
            P params)
    throws HttpClientException;
}
