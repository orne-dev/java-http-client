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

import java.io.InputStream;

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
 * Functional interface for HTTP request body supplier.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
@FunctionalInterface
public interface HttpRequestBodySupplier {

    /**
     * Sets the body of the HTTP request.
     * 
     * @param target The target where set the body.
     */
    void setBody(
            @NotNull BodyTarget target);

    /**
     * Functional interface for HTTP request body destination.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-06
     * @since HttpRequestBodySupplier 1.0
     */
    @FunctionalInterface
    interface BodyTarget {

        /**
         * Adds the specified body to the HTTP request.
         * A unknown (-1) body content length is assumed.
         * 
         * @param body The body content.
         */
        default void send(
                @NotNull InputStream body) {
            send(body, -1);
        }

        /**
         * Adds the specified body to the HTTP request.
         * 
         * @param body The body content.
         * @param legth The body content length, in bytes.
         * Use {@code -1} for unknown.
         */
        void send(
                @NotNull InputStream body,
                long legth);
    }
}
