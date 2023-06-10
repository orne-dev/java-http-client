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

/**
 * Functional interface for HTTP request headers supplier.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
@FunctionalInterface
public interface HttpRequestHeadersSupplier {

    /**
     * Sets the headers of the HTTP request.
     * 
     * @param target The target where set the headers.
     */
    void setHeaders(
            @NotNull HeaderTarget target);

    /**
     * Functional interface for HTTP request header destination.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-06
     * @since HttpRequestHeadersSupplier 1.0
     */
    @FunctionalInterface
    interface HeaderTarget {

        /**
         * Adds the specified header to the HTTP request.
         * 
         * @param header The header name.
         * @param values The header values.
         */
        void addHeader(
                @NotNull String header,
                @NotNull String... values);
    }
}
