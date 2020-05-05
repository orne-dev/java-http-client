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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.http.HttpEntity;
import org.apache.http.impl.client.AbstractResponseHandler;

/**
 * JSON response handler based on JAXB.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <E> The HTTP response entity type
 * @since 0.1
 */
public class XMLResponseHandler<E>
extends AbstractResponseHandler<E> {

    /** The JAXB context to use. */
    private final JAXBContext context;
    /** The result value type. */
    private final Class<? extends E> valueType;

    /**
     * Creates a new instance.
     * 
     * @param valueType The result value type
     * @throws JAXBException If an error occurs creating the JAXB context
     */
    public XMLResponseHandler(
            final Class<? extends E> valueType)
    throws JAXBException {
        super();
        this.context = JAXBContext.newInstance(valueType);
        this.valueType = valueType;
    }

    /**
     * Creates a new instance.
     * 
     * @param context The JAXBContext to use
     * @param valueType The result value type
     */
    public XMLResponseHandler(
            final JAXBContext context,
            final Class<? extends E> valueType) {
        super();
        this.context = context;
        this.valueType = valueType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E handleEntity(final HttpEntity entity)
    throws IOException {
        E result = null;
        final InputStream entityIS = entity.getContent();
        try {
            final StreamSource source = new StreamSource(entityIS);
            final Unmarshaller unmarshaller = this.context.createUnmarshaller();
            final JAXBElement<? extends E> element = unmarshaller.unmarshal(
                    source,
                    this.valueType);
            if (element != null) {
                result = element.getValue();
            }
        } catch (final JAXBException jaxbe) {
            throw new IOException("Error unmarshalling entity", jaxbe);
        } finally {
            entityIS.close();
        }
        return result;
    }
}
