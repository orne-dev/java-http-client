package dev.orne.http.client.engine;

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

import dev.orne.http.client.HttpClientException;

/**
 * Functional interface for HTTP response headers supplier.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
@FunctionalInterface
public interface HttpResponseHeadersSupplier {

    /**
     * Returns {@code true} if the HTTP response contains any header with the
     * specified name.
     * 
     * @param header The header name.
     * @return If the HTTP response contains the specified header.
     * @throws HttpClientException If an error occurs retrieving response's
     * headers.
     */
    default boolean containsHeader(
            @NotNull String header)
    throws HttpClientException {
        return getHeader(header).length > 0;
    }

    /**
     * Returns a single value for the specified HTTP response header.
     * 
     * @param header The header name.
     * @return The value of the header.
     * @throws HttpClientException If an error occurs retrieving response's
     * headers.
     * @throws IllegalStateException If the response contains multiple values
     * for the specified header.
     */
    default String getHeaderValue(
            @NotNull String header)
    throws HttpClientException {
        final String[] values = getHeader(header);
        if (values.length == 0) {
            return null;
        } else if (values.length == 1) {
            return values[0];
        } else {
            throw new IllegalStateException(String.format(
                    "Multiple values for header %s detected: %s",
                    header,
                    values));
        }
    }

    /**
     * Returns the first value for the specified HTTP response header.
     * 
     * @param header The header name.
     * @return The first value of the header.
     * @throws HttpClientException If an error occurs retrieving response's
     * headers.
     */
    default String getFirstHeaderValue(
            @NotNull String header)
    throws HttpClientException {
        final String[] values = getHeader(header);
        if (values.length == 0) {
            return null;
        } else {
            return values[0];
        }
    }

    /**
     * Returns the last value for the specified HTTP response header.
     * 
     * @param header The header name.
     * @return The last value of the header.
     * @throws HttpClientException If an error occurs retrieving response's
     * headers.
     */
    default String getLastHeaderValue(
            @NotNull String header)
    throws HttpClientException {
        final String[] values = getHeader(header);
        if (values.length == 0) {
            return null;
        } else {
            return values[values.length - 1];
        }
    }

    /**
     * Returns the values for the specified HTTP response header.
     * 
     * @param header The header name.
     * @return The values of the header.
     * @throws HttpClientException If an error occurs retrieving response's
     * headers.
     */
    @NotNull String[] getHeader(
            @NotNull String header)
    throws HttpClientException;
}
