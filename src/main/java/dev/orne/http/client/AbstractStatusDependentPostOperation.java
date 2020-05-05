/**
 * 
 */
package dev.orne.http.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;

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
     * Creates a new instance.
     * 
     * @param operationURI The relative URI of the operation
     */
    public AbstractStatusDependentPostOperation(
            @Nonnull
            final URI operationURI) {
        super(operationURI);
    }

    /**
     * Creates a new HTTP POST request.
     * 
     * @param params The operation execution parameters
     * @return The generated HTTP POST request
     * @throws HttpClientException If an exception occurs generating the
     * request
     */
    @Override
    @Nonnull
    protected HttpPost createRequest(
            @Nullable
            final P params,
            @Nonnull
            final StatedHttpServiceClient<S> client)
    throws HttpClientException {
        final URIBuilder uriBuilder = new URIBuilder(
                getRequestURI(params, client));
        final S status = client.ensureInitialized();
        uriBuilder.addParameters(createParams(params, status));
        final HttpPost request;
        try {
            request = new HttpPost(uriBuilder.build());
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
