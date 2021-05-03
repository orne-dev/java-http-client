package dev.orne.http.client;

/*-
 * #%L
 * Orne HTTP Client
 * %%
 * Copyright (C) 2020 Orne Developments
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON response handler based on FasterXML's Jackson databind.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <E> The HTTP response entity type
 * @since 0.1
 */
public class JacksonJSONResponseHandler<E>
extends AbstractMimeTypeResponseHandler<E> {

    /** The default {@code ContentType}. */
    protected static final ContentType DEFAULT_CONTENT_TYPE =
            ContentType.create(ContentType.APPLICATION_JSON.getMimeType());

    /** The Jackson object mapper to use. */
    private final @NotNull ObjectMapper mapper;
    /** The result value type. */
    private final @NotNull Class<? extends E> valueType;

    /**
     * Creates a new instance.
     * 
     * @param valueType The result value type
     */
    public JacksonJSONResponseHandler(
            final @NotNull Class<? extends E> valueType) {
        super();
        this.mapper = new ObjectMapper();
        this.valueType = Validate.notNull(valueType, "Result value type is required.");
    }

    /**
     * Creates a new instance.
     * 
     * @param mapper Jackson object mapper
     * @param valueType The result value type
     */
    public JacksonJSONResponseHandler(
            final @NotNull ObjectMapper mapper,
            final @NotNull Class<? extends E> valueType) {
        super();
        this.mapper = Validate.notNull(mapper, "Object mapper is required");;
        this.valueType = Validate.notNull(valueType, "Result value type is required.");
    }

    /**
     * Returns the Jackson object mapper to use.
     * 
     * @return The Jackson object mapper to use
     */
    protected ObjectMapper getMapper() {
        return this.mapper;
    }

    /**
     * Returns the result value type.
     * 
     * @return The result value type
     */
    public @NotNull Class<? extends E> getValueType() {
        return this.valueType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isMimeTypeSupported(
            final @NotNull String mimeType) {
        return ContentType.APPLICATION_JSON.getMimeType().equals(mimeType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull ContentType getDefaultContentType() {
        return DEFAULT_CONTENT_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E handleEntity(final HttpEntity entity)
    throws IOException {
        E result = null;
        if (entity != null) {
            final ContentType contentType = getSupportedContentType(entity);
            try (final InputStream entityIS = entity.getContent()) {
                result = this.mapper.readValue(
                        createReader(entityIS, contentType),
                        this.valueType);
            } catch (final UnsupportedOperationException uoe) {
                throw new IOException("Error obtaining entity content", uoe);
            }
        }
        return result;
    }

    /**
     * Creates a {@code Reader} for the entity's content
     * {@code InputStream}
     * 
     * @param entityIS The entity's content {@code InputStream}
     * @param contentType The entity's content type
     * @return The reader to use for reading the entity's content
     */
    protected @NotNull Reader createReader(
            final @NotNull InputStream entityIS,
            final @NotNull ContentType contentType) {
        final Reader source;
        if (contentType.getCharset() == null) {
            source = new InputStreamReader(
                    entityIS, StandardCharsets.UTF_8);
        } else {
            source = new InputStreamReader(
                    entityIS, contentType.getCharset());
        }
        return source;
    }
}
