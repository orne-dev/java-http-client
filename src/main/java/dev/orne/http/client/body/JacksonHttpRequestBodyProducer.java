package dev.orne.http.client.body;

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

import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.orne.http.ContentType;
import dev.orne.http.MediaTypes;
import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.engine.HttpRequest;

/**
 * Jackson based HTTP request JSON body producer. 
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public class JacksonHttpRequestBodyProducer
implements HttpRequestBodyProducer {

    /** The body content type. */
    private final @NotNull ContentType contentType;
    /** The Jackson object mapper to use. */
    private final @NotNull ObjectMapper mapper;
    /** The body content entity. */
    private final Object entity;

    /**
     * Creates a new instance with default settings.
     * <p>
     * A default Jackson object mapper, {@code application/json} media type and
     * UTF-8 charset are set.
     * 
     * @param entity The body content entity.
     */
    public JacksonHttpRequestBodyProducer(
            final Object entity) {
        this(ContentType.of(MediaTypes.Application.JSON, StandardCharsets.UTF_8), entity);
    }

    /**
     * Creates a new instance with a default Jackson object mapper.
     * 
     * @param contentType The body content type.
     * @param entity The body content entity.
     */
    public JacksonHttpRequestBodyProducer(
            final @NotNull ContentType contentType,
            final Object entity) {
        this(contentType, new ObjectMapper(), entity);
    }

    /**
     * Creates a new instance.
     * 
     * @param contentType The body content type.
     * @param mapper The Jackson object mapper to use.
     * @param entity The body content entity.
     */
    public JacksonHttpRequestBodyProducer(
            final @NotNull ContentType contentType,
            final @NotNull ObjectMapper mapper,
            final Object entity) {
        super();
        this.contentType = Validate.notNull(contentType);
        this.mapper = Validate.notNull(mapper);
        this.entity = entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void generate(
            final @NotNull HttpRequest request)
    throws HttpClientException {
        request.setBody(this.contentType, output -> {
            final OutputStreamWriter writer = new OutputStreamWriter(
                    output,
                    this.contentType.getCharset());
            this.mapper.writeValue(writer, this.entity);
        });
    }
}
