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
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.utils.URIBuilder;

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
     * Creates a new instance.
     * 
     * @param operationURI The relative URI of the operation
     */
    public AbstractStatusIndependentDeleteOperation(
            @Nonnull
            final URI operationURI) {
        super(operationURI);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    protected HttpDelete createRequest(
            @Nullable
            final P params,
            @Nonnull
            final HttpServiceClient client)
    throws HttpClientException {
        final URIBuilder uriBuilder = new URIBuilder(
                getRequestURI(params, client));
        uriBuilder.addParameters(createParams(params));
        final HttpDelete request;
        try {
            request = new HttpDelete(uriBuilder.build());
        } catch (final URISyntaxException use) {
            throw new HttpClientException(use);
        }
        final List<Header> requestHeaders = createHeaders(params);
        for (final Header header : requestHeaders) {
            request.addHeader(header);
        }
        return request;
    }
}
