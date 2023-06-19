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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang3.Validate;

import dev.orne.http.ContentType;
import dev.orne.http.MediaTypes;
import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.engine.HttpRequest;

/**
 * JAXB based HTTP request XML body producer. 
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public class JaxbHttpResponseBodyProducer
implements HttpRequestBodyProducer {

    /** The body content type. */
    private final @NotNull ContentType contentType;
    /** The JAXB context to use. */
    private final @NotNull JAXBContext context;
    /** The body content entity. */
    private final @NotNull Object entity;

    /**
     * Creates a new instance with default settings.
     * <p>
     * A default JAXB context, {@code application/xml} media type and
     * UTF-8 charset are set.
     * 
     * @param entity The body content entity.
     * @throws JAXBException If an error occurs creating the JAXB context.
     */
    public JaxbHttpResponseBodyProducer(
            final @NotNull Object entity)
    throws JAXBException {
        this(ContentType.of(MediaTypes.Application.XML, StandardCharsets.UTF_8), entity);
    }

    /**
     * Creates a new instance with a default JAXB context.
     * 
     * @param contentType The body content type.
     * @param entity The body content entity.
     * @throws JAXBException If an error occurs creating the JAXB context.
     */
    public JaxbHttpResponseBodyProducer(
            final @NotNull ContentType contentType,
            final @NotNull Object entity)
    throws JAXBException {
        this(contentType, JAXBContext.newInstance(entity.getClass()), entity);
    }

    /**
     * Creates a new instance.
     * 
     * @param contentType The body content type.
     * @param mapper The JAXB context to use.
     * @param entity The body content entity.
     */
    public JaxbHttpResponseBodyProducer(
            final @NotNull ContentType contentType,
            final @NotNull JAXBContext context,
            final @NotNull Object entity) {
        super();
        this.contentType = Validate.notNull(contentType);
        this.context = Validate.notNull(context);
        this.entity = Validate.notNull(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void generate(
            final @NotNull HttpRequest request)
    throws HttpClientException {
        request.setBody(this.contentType, output -> {
            try {
                final Marshaller marshaller = this.context.createMarshaller();
                final OutputStreamWriter writer = new OutputStreamWriter(
                        output,
                        this.contentType.getCharset());
                marshaller.marshal(this.entity, writer);
            } catch (final JAXBException e) {
                throw new IOException("Error producing HTTP request body", e);
            }
        });
    }
}
