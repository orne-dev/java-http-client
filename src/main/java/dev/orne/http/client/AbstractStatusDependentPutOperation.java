/**
 * 
 */
package dev.orne.http.client;

import java.net.URISyntaxException;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;

/**
 * Abstract status dependent operation for {@code StatedHttpServiceClient}
 * based on HTTP PUT requests.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <P> El execution parameters type
 * @param <E> El HTTP response entity type
 * @param <R> El execution result type
 * @param <S> The client status type
 * @since 0.1
 */
public abstract class AbstractStatusDependentPutOperation<P, E, R, S>
extends AbstractStatusDependentOperation<P, E, R, S> {

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    protected HttpPut createRequest(
            @Nullable
            final P params,
            @Nonnull
            final StatedHttpServiceClient<S> client)
    throws HttpClientException {
        final URIBuilder uriBuilder = new URIBuilder(
                getRequestURI(params, client));
        final S status = client.ensureInitialized();
        uriBuilder.addParameters(createParams(params, status));
        final HttpPut request;
        try {
            request = new HttpPut(uriBuilder.build());
        } catch (final URISyntaxException use) {
            throw new HttpClientException(use);
        }
        final List<Header> requestHeaders = createHeaders(params, status);
        for (final Header header : requestHeaders) {
            request.addHeader(header);
        }
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
    @Nullable
    protected abstract HttpEntity createEntity(
            @Nullable
            P params,
            @Nonnull
            S status)
    throws HttpClientException;
}
