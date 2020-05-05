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

import java.io.Closeable;
import java.net.URI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * HTTP service client interface.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 */
public interface HttpServiceClient
extends Closeable {

    /**
     * @return The HTTP service's host
     */
    @Nonnull
    HttpHost getHost();

    /**
     * @return The HTTP service's base URI
     */
    @Nonnull
    URI getBaseURI();

    /**
     * @return The HTTP client's cookie store
     */
    @Nonnull
    CookieStore getCookieStore();

    /**
     * @return The HTTP client
     */
    @Nonnull
    CloseableHttpClient getClient();

    /**
     * Executes the specified status unaware operation for this HTTP service.
     * 
     * @param <P> The operation's parameter type
     * @param <R> The operation execution's result type
     * @param operation The operation to execute
     * @param params The operation parameter
     * @return The operation execution's result
     * @throws HttpClientException If an error occurs executing the operation
     */
    @Nullable
    <P, R> R execute(
            @Nonnull
            final StatusIndependentOperation<P, R> operation,
            @Nullable
            final P params)
    throws HttpClientException;
}
