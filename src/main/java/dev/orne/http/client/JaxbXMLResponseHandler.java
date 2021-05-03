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

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;

/**
 * JSON response handler based on JAXB.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <E> The HTTP response entity type
 * @since 0.1
 */
public class JaxbXMLResponseHandler<E>
extends AbstractMimeTypeResponseHandler<E> {

    /** The default {@code ContentType}. */
    protected static final ContentType DEFAULT_CONTENT_TYPE =
            ContentType.create(ContentType.APPLICATION_XML.getMimeType());

    /** The JAXB context to use. */
    private final @NotNull JAXBContext context;
    /** The result value type. */
    private final @NotNull Class<? extends E> valueType;

    /**
     * Creates a new instance.
     * 
     * @param valueType The result value type
     * @throws JAXBException If an error occurs creating the JAXB context
     */
    public JaxbXMLResponseHandler(
            final @NotNull Class<? extends E> valueType)
    throws JAXBException {
        super();
        this.context = JAXBContext.newInstance(valueType);
        this.valueType = Validate.notNull(valueType, "Result value type is required.");
    }

    /**
     * Creates a new instance.
     * 
     * @param context The JAXBContext to use
     * @param valueType The result value type
     */
    public JaxbXMLResponseHandler(
            final @NotNull JAXBContext context,
            final @NotNull Class<? extends E> valueType) {
        super();
        this.context = Validate.notNull(context, "JAXB context is required.");
        this.valueType = Validate.notNull(valueType, "Result value type is required.");
    }

    /**
     * Returns the JAXB context to use.
     * 
     * @return The JAXB context to use
     */
    protected @NotNull JAXBContext getContext() {
        return this.context;
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
        return ContentType.APPLICATION_XML.getMimeType().equals(mimeType)
                || ContentType.TEXT_XML.getMimeType().equals(mimeType);
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
                final Unmarshaller unmarshaller = this.context.createUnmarshaller();
                final JAXBElement<? extends E> element = unmarshaller.unmarshal(
                        createSource(entityIS, contentType),
                        this.valueType);
                if (element != null) {
                    result = element.getValue();
                }
            } catch (final UnsupportedOperationException uoe) {
                throw new IOException("Error obtaining entity content", uoe);
            } catch (final JAXBException jaxbe) {
                throw new IOException("Error unmarshalling XML entity", jaxbe);
            }
        }
        return result;
    }

    /**
     * Creates a {@code StreamSource} for the entity's content
     * {@code InputStream}
     * 
     * @param entityIS The entity's content {@code InputStream}
     * @param contentType The entity's content type
     * @return The source to use for reading the entity's content
     */
    protected @NotNull StreamSource createSource(
            final @NotNull InputStream entityIS,
            final @NotNull ContentType contentType) {
        final StreamSource source;
        if (contentType.getCharset() == null) {
            source = new StreamSource(entityIS);
        } else {
            source = new StreamSource(new InputStreamReader(
                    entityIS, contentType.getCharset()));
        }
        return source;
    }
}
