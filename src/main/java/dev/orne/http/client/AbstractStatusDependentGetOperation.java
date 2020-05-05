/**
 * 
 */
package dev.orne.http.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

/**
 * Abstract status dependent operation for {@code HttpServiceClient} based on
 * HTTP GET requests.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <P> El execution parameters type
 * @param <E> El HTTP response entity type
 * @param <R> El execution result type
 * @param <S> The client status type
 * @since 0.1
 */
public abstract class AbstractStatusDependentGetOperation<P, E, R, S>
extends AbstractStatusDependentOperation<P, E, R, S> {

    /**
     * Creates a new instance.
     * 
     * @param operationURI The relative URI of the operation
     */
    public AbstractStatusDependentGetOperation(
            final URI operationURI) {
        super(operationURI);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected HttpGet createRequest(
            final P params,
            final StatedHttpServiceClient<S> client)
    throws HttpClientException {
        final URIBuilder uriBuilder = new URIBuilder(
                getRequestURI(params, client));
        uriBuilder.addParameters(createParams(params, client.getStatus()));
        final HttpGet request;
        try {
            request = new HttpGet(uriBuilder.build());
        } catch (final URISyntaxException use) {
            throw new HttpClientException(use);
        }
        final List<Header> requestHeaders = createHeaders(params, client.getStatus());
        for (final Header header : requestHeaders) {
            request.addHeader(header);
        }
        return request;
    }
}
