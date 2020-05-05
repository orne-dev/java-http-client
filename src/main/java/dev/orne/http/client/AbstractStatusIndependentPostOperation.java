/**
 * 
 */
package dev.orne.http.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;

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
     * Creates a new instance.
     * 
     * @param operationURI The relative URI of the operation
     */
    public AbstractStatusIndependentPostOperation(
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
    protected HttpPost createRequest(
            final P params,
            final HttpServiceClient client)
    throws HttpClientException {
        final URIBuilder uriBuilder = new URIBuilder(
                getRequestURI(params, client));
        uriBuilder.addParameters(createParams(params));
        final HttpPost request;
        try {
            request = new HttpPost(uriBuilder.build());
        } catch (final URISyntaxException use) {
            throw new HttpClientException(use);
        }
        final List<Header> requestHeaders = createHeaders(params);
        for (final Header header : requestHeaders) {
            request.addHeader(header);
        }
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
    protected abstract HttpEntity createEntity(P params)
    throws HttpClientException;
}
