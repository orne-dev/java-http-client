package dev.orne.http.client;

/*-
 * #%L
 * Orne HTTP Client
 * %%
 * Copyright (C) 2020 - 2021 Orne Developments
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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

/**
 * Abstract status independent operation for {@code HttpServiceClient} based on
 * HTTP POST requests with URL encoding entity.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <P> El execution parameters type
 * @param <E> El HTTP response entity type
 * @param <R> El execution result type
 * @since 0.1
 */
public abstract class AbstractStatusIndependentUrlEncodedPostOperation<P, E, R>
extends AbstractStatusIndependentPostOperation<P, E, R> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected UrlEncodedFormEntity createEntity(
            final P params)
    throws HttpClientException {
        final List<NameValuePair> requestParams = createEntityParams(params);
        return new UrlEncodedFormEntity(requestParams,
                getEntityCharset(params));
    }

    /**
     * Returns the {@code Charset} to use in request entity.
     * 
     * @param params The operation execution parameters
     * @return The {@code Charset} to use in request entity
     * @throws HttpClientException If an exception occurs resolving the
     * entity's {@code Charset}
     */
    protected @NotNull Charset getEntityCharset(
            final P params)
    throws HttpClientException {
        return StandardCharsets.UTF_8;
    }

    /**
     * Creates the URL encoded HTTP request entity parameters.
     * 
     * @param params The operation execution parameters
     * @return The generated HTTP entity parameters
     * @throws HttpClientException If an exception occurs generating the
     * entity parameters
     */
    protected abstract @NotNull List<NameValuePair> createEntityParams(
            P params)
    throws HttpClientException;
}
