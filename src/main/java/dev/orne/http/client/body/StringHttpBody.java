package dev.orne.http.client.body;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.validation.constraints.NotNull;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;

import dev.orne.http.ContentType;
import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.HttpResponseBodyParsingException;
import dev.orne.http.client.HttpResponseHandlingException;
import dev.orne.http.client.engine.HttpRequest;
import dev.orne.http.client.engine.HttpResponseBody;

/**
 * API for generation of string HTTP request and response body
 * handlers.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-08
 * @since 0.1
 */
public final class StringHttpBody {

    /**
     * Private constructor.
     */
    private StringHttpBody() {
        // Utility class
    }

    /**
     * Produce the specified entity as string HTTP request body
     * with {@code text/plain;charset=UTF-8} as content type.
     * 
     * @param entity The HTTP request body entity.
     * @param request The HTTP request.
     * @throws HttpClientException If an error occurs producing
     * or setting the request body.
     */
    public static void produce(
            final Object entity,
            final @NotNull HttpRequest request)
    throws HttpClientException {
        produce(
                entity,
                request,
                StringHttpResponseBodyParser.DEFAULT_CONTENT_TYPE);
    }

    /**
     * Produce the specified entity as string HTTP request body
     * with the specified content type.
     * 
     * @param entity The HTTP request body entity.
     * @param request The HTTP request.
     * @param contentType The HTTP request body content type.
     * @throws HttpClientException If an error occurs producing
     * or setting the request body.
     */
    public static void produce(
            final Object entity,
            final @NotNull HttpRequest request,
            final @NotNull ContentType contentType)
    throws HttpClientException {
        Validate.notNull(request);
        Validate.notNull(contentType);
        Validate.notNull(
                contentType.getCharset(),
                "Content type must include a charset parameter.");
        if (entity != null) {
            request.setBody(contentType, entity.toString());
        }
    }

    /**
     * Parses the HTTP response body string entity
     * with {@code text/plain;charset=UTF-8} as default content type.
     * 
     * @param body The HTTP response body.
     * @return The parsed HTTP response body.
     * @throws HttpResponseHandlingException If an error occurs parsing the
     * HTTP response body.
     */
    public static String parse(
            final @NotNull HttpResponseBody body)
    throws HttpResponseHandlingException {
        return parse(
                body,
                StringHttpResponseBodyParser.DEFAULT_CONTENT_TYPE);
    }

    /**
     * Parses the HTTP response body string entity.
     * 
     * @param body The HTTP response body.
     * @param defaultContentType The default content type to use if the HTTP
     * response does not specify one.
     * @return The parsed HTTP response body.
     * @throws HttpResponseHandlingException If an error occurs parsing the
     * HTTP response body.
     */
    public static String parse(
            final @NotNull HttpResponseBody body,
            final @NotNull ContentType defaultContentType)
    throws HttpResponseHandlingException {
        Validate.notNull(body);
        return body.parse(parser(defaultContentType));
    }

    /**
     * Creates a new HTTP response string body parser
     * with {@code text/plain;charset=UTF-8} as default content type.
     * 
     * @param defaultContentType The default content type to use if the HTTP
     * response does not specify one.
     * @return The created HTTP response body parser.
     */
    public static @NotNull StringHttpResponseBodyParser parser() {
        return parser(
                StringHttpResponseBodyParser.DEFAULT_CONTENT_TYPE);
    }

    /**
     * Creates a new HTTP response body string entity parser.
     * 
     * @param defaultContentType The default content type to use if the HTTP
     * response does not specify one.
     * @return The created HTTP response body parser.
     */
    public static @NotNull StringHttpResponseBodyParser parser(
            final @NotNull ContentType defaultContentType) {
        return new StringBodyParser(defaultContentType);
    }

    /**
     * Default implementation of {@code StringHttpResponseBodyParser}.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     */
    public static class StringBodyParser
    extends AbstractHttpResponseBodyMediaTypeParser<String>
    implements StringHttpResponseBodyParser {

        /** The default content type to use. */
        private final @NotNull ContentType defaultContentType;

        /**
         * Creates a new instance.
         * 
         * @param entityType The HTTP response body entity type.
         * @param defaultContentType The default content type to use if the HTTP
         * response does not specify one.
         * @param mapper The Jackson object mapper to use.
         */
        public StringBodyParser(
                final @NotNull ContentType defaultContentType) {
            super();
            this.defaultContentType = Validate.notNull(defaultContentType);
            Validate.notNull(
                    defaultContentType.getCharset(),
                    "Default content type must include a charset parameter.");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull ContentType getDefaultContentType() {
            return this.defaultContentType;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected String parseSupportedContent(
                final @NotNull ContentType type,
                final @NotNull InputStream content,
                final long length)
        throws HttpResponseBodyParsingException {
            try (final InputStreamReader reader = new InputStreamReader(
                    content,
                    ObjectUtils.defaultIfNull(
                        type.getCharset(),
                        this.defaultContentType.getCharset()))) {
                return IOUtils.toString(reader);
            } catch (IOException e) {
                throw new HttpResponseBodyParsingException("Error parsing HTTP response body", e);
            }
        }
    }
}
