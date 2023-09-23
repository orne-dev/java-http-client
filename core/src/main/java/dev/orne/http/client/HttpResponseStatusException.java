package dev.orne.http.client;

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
 * HTTP service client error for HTTP response parsing problems.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public class HttpResponseStatusException
extends HttpClientException {

    /** The Serial Version UID. */
    private static final long serialVersionUID = 1L;

    /** Default template for status code based message. */
    public static final String DEFAULT_MSG_TMPL = "[%d] %s";

    /** The HTTP response's status code. */
    private final int statusCode;
    /** The HTTP response's status reason phrase. */
    private final String statusReason;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     * 
     * @param   statusCode      The HTTP response's status code.
     * @param   statusReason    The HTTP response's status reason phrase.
     */
    public HttpResponseStatusException(
            final int statusCode,
            final String statusReason) {
        this(
                statusCode,
                statusReason,
                String.format(DEFAULT_MSG_TMPL, statusCode, statusReason));
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param   statusCode      The HTTP response's status code.
     * @param   statusReason    The HTTP response's status reason phrase.
     * @param   message         The detail message. The detail message is saved
     *          for later retrieval by the {@link #getMessage()} method.
     */
    public HttpResponseStatusException(
            final int statusCode,
            final String statusReason,
            final String message) {
        super(message);
        this.statusCode = statusCode;
        this.statusReason = statusReason;
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * java.security.PrivilegedActionException}).
     *
     * @param   statusCode      The HTTP response's status code.
     * @param   statusReason    The HTTP response's status reason phrase.
     * @param   cause           The cause (which is saved for later retrieval
     *          by the {@link #getCause()} method).  (A <tt>null</tt> value is
     *          permitted, and indicates that the cause is nonexistent or
     *          unknown.)
     */
    public HttpResponseStatusException(
            final int statusCode,
            final String statusReason,
            final Throwable cause) {
        this(
                statusCode,
                statusReason,
                String.format(DEFAULT_MSG_TMPL, statusCode, statusReason),
                cause);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param   statusCode      The HTTP response's status code.
     * @param   statusReason    The HTTP response's status reason phrase.
     * @param   message         The detail message (which is saved for later
     *          retrieval by the {@link #getMessage()} method).
     * @param   cause           The cause (which is saved for later retrieval
     *          by the {@link #getCause()} method).  (A <tt>null</tt> value is
     *          permitted, and indicates that the cause is nonexistent or
     *          unknown.)
     */
    public HttpResponseStatusException(
            final int statusCode,
            final String statusReason,
            final String message,
            final Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.statusReason = statusReason;
    }

    /**
     * Constructs a new exception with the specified detail message,
     * cause, suppression enabled or disabled, and writable stack
     * trace enabled or disabled.
     *
     * @param   statusCode          The HTTP response's status code.
     * @param   statusReason        The HTTP response's status reason phrase.
     * @param   message             The detail message.
     * @param   cause               The cause.  (A {@code null} value is
     *          permitted, and indicates that the cause is nonexistent or
     *          unknown.)
     * @param   enableSuppression   Whether or not suppression is enabled
     *          or disabled.
     * @param   writableStackTrace  Whether or not the stack trace should
     *          be writable.
     */
    public HttpResponseStatusException(
            final int statusCode,
            final String statusReason,
            final String message,
            final Throwable cause,
            final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.statusCode = statusCode;
        this.statusReason = statusReason;
    }

    /**
     * Returns the HTTP response's status code.
     * 
     * @return The HTTP response's status code.
     */
    public int getStatusCode() {
        return this.statusCode;
    }

    /**
     * Returns the HTTP response's status reason phrase.
     * 
     * @return The HTTP response's status reason phrase.
     */
    public String getStatusReason() {
        return this.statusReason;
    }
}
