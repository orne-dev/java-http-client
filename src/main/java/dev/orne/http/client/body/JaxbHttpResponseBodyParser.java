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

import java.io.InputStream;
import java.io.InputStreamReader;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.Validate;

import dev.orne.http.ContentType;
import dev.orne.http.MediaTypes;

/**
 * JAXB based HTTP response XML body parser. 
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @param <E> The HTTP response body entity type.
 * @since 0.1
 */
public class JaxbHttpResponseBodyParser<E>
implements HttpResponseBodyMediaTypeParser<E> {

    /** The JAXB context to use. */
    private final @NotNull JAXBContext context;
    /** The HTTP body content entity type. */
    private final @NotNull Class<? extends E> entityType;

    /**
     * Creates a new instance with a default Jackson object mapper.
     * 
     * @param entityType The HTTP body content entity type.
     * @throws JAXBException If an error occurs creating the JAXB context.
     */
    public JaxbHttpResponseBodyParser(
            final @NotNull Class<? extends E> entityType)
    throws JAXBException {
        this(JAXBContext.newInstance(entityType), entityType);
    }

    /**
     * Creates a new instance.
     * 
     * @param mapper The Jackson object mapper to use.
     * @param entityType The HTTP body content entity type.
     */
    public JaxbHttpResponseBodyParser(
            final @NotNull JAXBContext context,
            final @NotNull Class<? extends E> entityType) {
        super();
        this.context = Validate.notNull(context);
        this.entityType = Validate.notNull(entityType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supportsMediaType(
            final @NotNull String mediaType) {
        return MediaTypes.Application.XML.equalsIgnoreCase(mediaType) ||
                MediaTypes.Text.XML.equalsIgnoreCase(mediaType) ||
                mediaType.toLowerCase().endsWith("+xml");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E parse(
            final @NotNull ContentType type,
            final @NotNull InputStream content,
            final long length)
    throws HttpResponseBodyParsingException {
        E result = null;
        try {
            final Unmarshaller unmarshaller = this.context.createUnmarshaller();
            final JAXBElement<? extends E> element = unmarshaller.unmarshal(
                    createSource(content, type),
                    this.entityType);
            if (element != null) {
                result = element.getValue();
            }
        } catch (final JAXBException e) {
            throw new HttpResponseBodyParsingException("Error parsing HTTP response body", e);
        }
        return result;
    }

    /**
     * Creates a {@code StreamSource} for the HTTP response body
     * {@code InputStream}.
     * 
     * @param content The entity's content {@code InputStream}
     * @param type The entity's content type
     * @return The source to use for reading the entity's content
     */
    protected @NotNull StreamSource createSource(
            final @NotNull InputStream content,
            final @NotNull ContentType type) {
        final StreamSource source;
        if (type.getCharset() == null) {
            source = new StreamSource(content);
        } else {
            source = new StreamSource(new InputStreamReader(
                    content, type.getCharset()));
        }
        return source;
    }
}
