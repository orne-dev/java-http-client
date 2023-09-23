package dev.orne.http.client.engine.apache;

import java.io.IOException;

/*-
 * #%L
 * Orne HTTP Client
 * %%
 * Copyright (C) 2023 Orne Developments
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpEntityContainer;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.HttpEntities;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import dev.orne.http.ContentType;
import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.HttpRequestBodyGenerationException;
import dev.orne.http.client.engine.HttpRequest;

/**
 * Implementation of {@code HttpRequest} based on
 * Apache HTTP Client 5.x.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public class ApacheHttpRequest
implements HttpRequest {

    /** The Apache HTTP client request. */
    private final @NotNull org.apache.hc.core5.http.HttpRequest delegate;

    /**
     * Creates a new instance.
     * 
     * @param delegate The Apache HTTP client request.
     */
    public ApacheHttpRequest(
            final @NotNull org.apache.hc.core5.http.HttpRequest delegate) {
        super();
        this.delegate = Validate.notNull(delegate);
    }

    /**
     * Returns the delegated Apache HTTP client request.
     * 
     * @return The Apache HTTP client request.
     */
    protected @NotNull org.apache.hc.core5.http.HttpRequest getDelegate() {
        return this.delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addHeader(
            final @NotNull String header,
            final @NotNull String... values)
    throws HttpClientException {
        Validate.notNull(header);
        Validate.notNull(values);
        Validate.noNullElements(values);
        for (final String value : values) {
            this.delegate.addHeader(header, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBody(
            final @NotNull ContentType contentType,
            final @NotNull String body)
    throws HttpClientException {
        Validate.notNull(body);
        setEntity(HttpEntities.create(
                body,
                asApacheContentType(contentType)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBody(
            final @NotNull ContentType contentType,
            final @NotNull byte[] body)
    throws HttpClientException {
        Validate.notNull(body);
        setEntity(HttpEntities.create(
                body,
                asApacheContentType(contentType)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBody(
            final @NotNull ContentType contentType,
            final long length,
            final @NotNull BodyProducer dataProvider)
    throws HttpClientException {
        Validate.notNull(dataProvider);
        setEntity(HttpEntities.create(
                output -> {
                    try {
                        dataProvider.writeBody(output);
                    } catch (final HttpRequestBodyGenerationException e) {
                        throw new IOException(e);
                    }
                    output.flush();
                },
                asApacheContentType(contentType)));
    }

    /**
     * Sets the entity of the HTTP request body entity.
     * <p>
     * Validates that the HTTP request accepts a body.
     * 
     * @param entity The body entity to set.
     * @throws IllegalStateException If the HTTP request does not accept a
     * body.
     */
    protected void setEntity(
            final @NotNull HttpEntity entity) {
        Validate.validState(
                this.delegate instanceof HttpEntityContainer,
                "The HTTP method of the request does not allow request body");
        ((HttpEntityContainer) this.delegate).setEntity(entity);
    }

    /**
     * Converts the specified content type to an Apache HTTP client content type.
     * 
     * @param contentType The body content type.
     * @return The Apache HTTP client content type.
     */
    protected @NotNull org.apache.hc.core5.http.ContentType asApacheContentType(
            final @NotNull ContentType contentType) {
        Validate.notNull(contentType);
        final NameValuePair[] pairs = contentType.getParameters().entrySet().stream()
                .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
                .toArray(NameValuePair[]::new); 
        return org.apache.hc.core5.http.ContentType.create(
                contentType.getMediaType(),
                pairs);
    }
}
