package dev.orne.http;

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

/**
 * Constants for HTTP methods.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public final class Methods {

    /** RFC 9110 Section 9.3.1 */
    public static final String GET = "GET";
    /** RFC 9110 Section 9.3.2 */
    public static final String HEAD = "HEAD";
    /** RFC 9110 Section 9.3.3 */
    public static final String POST = "POST";
    /** RFC 9110 Section 9.3.4 */
    public static final String PUT = "PUT";
    /** RFC 9110 Section 9.3.5 */
    public static final String DELETE = "DELETE";
    /** RFC 9110 Section 9.3.6 */
    public static final String CONNECT = "CONNECT";
    /** RFC 9110 Section 9.3.7 */
    public static final String OPTIONS = "OPTIONS";
    /** RFC 9110 Section 9.3.8 */
    public static final String TRACE = "TRACE";

    /** RFC 5789 */
    public static final String PATCH = "PATCH";

    /**
     * Private constructor.
     */
    private Methods() {
        // Utility class
    }
}
