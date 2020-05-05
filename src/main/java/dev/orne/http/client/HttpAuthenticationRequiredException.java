/**
 * 
 */
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

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

/**
 * HTTP service client error for authenticated operation
 * executed without previous required authentication.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 */
public class HttpAuthenticationRequiredException
extends HttpResponseException {

    /** The Serial Version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     * 
     * @param request The original HTTP request
     * @param response The offending HTTP response
     */
    public HttpAuthenticationRequiredException(
            final HttpRequest request,
            final HttpResponse response) {
        super(request, response);
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     * @param request The original HTTP request
     * @param response The offending HTTP response
     */
    public HttpAuthenticationRequiredException(
            final String message,
            final HttpRequest request,
            final HttpResponse response) {
        super(message, request, response);
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * java.security.PrivilegedActionException}).
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @param request The original HTTP request
     * @param response The offending HTTP response
     */
    public HttpAuthenticationRequiredException(
            final Throwable cause,
            final HttpRequest request,
            final HttpResponse response) {
        super(cause, request, response);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @param request The original HTTP request
     * @param response The offending HTTP response
     */
    public HttpAuthenticationRequiredException(
            final String message,
            final Throwable cause,
            final HttpRequest request,
            final HttpResponse response) {
        super(message, cause, request, response);
    }

    /**
     * Constructs a new exception with the specified detail message,
     * cause, suppression enabled or disabled, and writable stack
     * trace enabled or disabled.
     *
     * @param  message the detail message.
     * @param cause the cause.  (A {@code null} value is permitted,
     * and indicates that the cause is nonexistent or unknown.)
     * @param enableSuppression whether or not suppression is enabled
     *                          or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     * @param request The original HTTP request
     * @param response The offending HTTP response
     */
    public HttpAuthenticationRequiredException(
            final String message,
            final Throwable cause,
            final boolean enableSuppression,
            final boolean writableStackTrace,
            final HttpRequest request,
            final HttpResponse response) {
        super(message, cause, enableSuppression, writableStackTrace, request, response);
    }
}
