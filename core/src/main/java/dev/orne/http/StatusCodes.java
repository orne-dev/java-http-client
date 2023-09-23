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

import org.apiguardian.api.API;

/**
 * Constants for HTTP status codes.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public class StatusCodes {

    // --- 1xx Informational ---

    /**
     * Returns {@code true} if the status code is in the informational status
     * codes range.
     * 
     * @param statusCode The HTTP response status code.
     * @return If the status code is in the informational status codes range.
     */
    public static boolean isInformational(
            final int statusCode) {
        return statusCode >= 100 && statusCode < 200;
    }

    /** RFC 9110 Section 15.2.1 */
    public static final int CONTINUE = 100;
    /** RFC 9110 Section 15.2.2 */
    public static final int SWITCHING_PROTOCOLS = 101;

    // --- 2xx Success ---

    /**
     * Returns {@code true} if the status code is in the success status
     * codes range.
     * 
     * @param statusCode The HTTP response status code.
     * @return If the status code is in the success status codes range.
     */
    public static boolean isSuccess(
            final int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }

    /** RFC 9110 Section 15.3.1 */
    public static final int OK = 200;
    /** RFC 9110 Section 15.3.2 */
    public static final int CREATED = 201;
    /** RFC 9110 Section 15.3.3 */
    public static final int ACCEPTED = 202;
    /** RFC 9110 Section 15.3.4 */
    public static final int NON_AUTHORITATIVE_INFORMATION = 203;
    /** RFC 9110 Section 15.3.5 */
    public static final int NO_CONTENT = 204;
    /** RFC 9110 Section 15.3.6 */
    public static final int RESET_CONTENT = 205;
    /** RFC 9110 Section 15.3.7 */
    public static final int PARTIAL_CONTENT = 206;
    /** RFC 4918 Section 11.1 */
    public static final int MULTI_STATUS = 207;

    // --- 3xx Redirection ---

    /**
     * Returns {@code true} if the status code is in the redirection status
     * codes range.
     * 
     * @param statusCode The HTTP response status code.
     * @return If the status code is in the redirection status codes range.
     */
    public static boolean isRedirection(
            final int statusCode) {
        return statusCode >= 300 && statusCode < 400;
    }

    /** RFC 9110 Section 15.4.1 */
    public static final int MULTIPLE_CHOICES = 300;
    /** RFC 9110 Section 15.4.2 */
    public static final int MOVED_PERMANENTLY = 301;
    /** RFC 9110 Section 15.4.3 */
    public static final int FOUND = 302;
    /** RFC 9110 Section 15.4.4 */
    public static final int SEE_OTHER = 303;
    /** RFC 9110 Section 15.4.5 */
    public static final int NOT_MODIFIED = 304;
    /** RFC 9110 Section 15.4.6 */
    public static final int USE_PROXY = 305;
    /** RFC 9110 Section 15.4.8 */
    public static final int TEMPORARY_REDIRECT = 307;
    /** RFC 9110 Section 15.4.9 */
    public static final int PERMANENT_REDIRECT = 308;

    // --- 4xx Client Error ---

    /**
     * Returns {@code true} if the status code is in the client error status
     * codes range.
     * 
     * @param statusCode The HTTP response status code.
     * @return If the status code is in the client error status codes range.
     */
    public static boolean isClientError(
            final int statusCode) {
        return statusCode >= 400 && statusCode < 500;
    }

    /** RFC 9110 Section 15.5.1 */
    public static final int BAD_REQUEST = 400;
    /** RFC 9110 Section 15.5.2 */
    public static final int UNAUTHORIZED = 401;
    /** RFC 9110 Section 15.5.3 */
    public static final int PAYMENT_REQUIRED = 402;
    /** RFC 9110 Section 15.5.4 */
    public static final int FORBIDDEN = 403;
    /** RFC 9110 Section 15.5.5 */
    public static final int NOT_FOUND = 404;
    /** RFC 9110 Section 15.5.6 */
    public static final int METHOD_NOT_ALLOWED = 405;
    /** RFC 9110 Section 15.5.7 */
    public static final int NOT_ACCEPTABLE = 406;
    /** RFC 9110 Section 15.5.8 */
    public static final int PROXY_AUTHENTICATION_REQUIRED = 407;
    /** RFC 9110 Section 15.5.9 */
    public static final int REQUEST_TIMEOUT = 408;
    /** RFC 9110 Section 15.5.10 */
    public static final int CONFLICT = 409;
    /** RFC 9110 Section 15.5.11 */
    public static final int GONE = 410;
    /** RFC 9110 Section 15.5.12 */
    public static final int LENGTH_REQUIRED = 411;
    /** RFC 9110 Section 15.5.13 */
    public static final int PRECONDITION_FAILED = 412;
    /** RFC 9110 Section 15.5.14 */
    public static final int CONTENT_TOO_LONG = 413;
    /** RFC 9110 Section 15.5.15 */
    public static final int URI_TOO_LONG = 414;
    /** RFC 9110 Section 15.5.16 */
    public static final int UNSUPPORTED_MEDIA_TYPE = 415;
    /** RFC 9110 Section 15.5.17 */
    public static final int RANGE_NOT_SATISFIABLE = 416;
    /** RFC 9110 Section 15.5.18 */
    public static final int EXPECTATION_FAILED = 417;
    /** RFC 2324 Section 2.3.2 */
    public static final int IM_A_TEAPOT = 418;
    /** RFC 9110 Section 15.5.20 */
    public static final int MISDIRECTED_REQUEST = 421;
    /** RFC 9110 Section 15.5.21 */
    public static final int UNPROCESSABLE_CONTENT = 422;
    /** RFC 9110 Section 15.5.22 */
    public static final int UPGRADE_REQUIRED = 426;
    /** RFC 6585 Section 3 */
    public static final int PRECONDITION_REQUIRED = 428;
    /** RFC 6585 Section 4 */
    public static final int TOO_MANY_REQUESTS = 429;
    /** RFC 6585 Section 5 */
    public static final int REQUEST_HEADER_FIELDS_TOO_LARGE = 431;
    /** RFC 7725 Section 3 */
    public static final int UNAVAILABLE_FOR_LEGAL_REASONS = 451;

    // --- 5xx Server Error ---

    /**
     * Returns {@code true} if the status code is in the server error status
     * codes range.
     * 
     * @param statusCode The HTTP response status code.
     * @return If the status code is in the server error status codes range.
     */
    public static boolean isServerError(
            final int statusCode) {
        return statusCode >= 500 && statusCode < 600;
    }

    /** RFC 9110 Section 15.6.1 */
    public static final int INTERNAL_SERVER_ERROR = 500;
    /** RFC 9110 Section 15.6.2 */
    public static final int NOT_IMPLEMENTED = 501;
    /** RFC 9110 Section 15.6.3 */
    public static final int BAD_GATEWAY = 502;
    /** RFC 9110 Section 15.6.4 */
    public static final int SERVICE_UNAVAILABLE = 503;
    /** RFC 9110 Section 15.6.5 */
    public static final int GATEWAY_TIMEOUT = 504;
    /** RFC 9110 Section 15.6.6 */
    public static final int HTTP_VERSION_NOT_SUPPORTED = 505;
    /** RFC 2774 Section 7 */
    public static final int NOT_EXTENDED = 510;
    /** RFC 6585 Section 5 */
    public static final int NETWORK_AUTHENTICATION_REQUIRED = 511;

    /**
     * Private consturctor.
     */
    private StatusCodes() {
        // Utility class
    }

    /**
     * Constants for experimental HTTP status codes.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-06
     * @since 0.1
     */
    @API(status = API.Status.EXPERIMENTAL, since = "0.1")
    public static final class Experimental {

        // --- 1xx Informational ---

        /** RFC 8297 Section 4.2.4.5 */
        public static final int EARLY_HINT = 103 ;

        // --- 2xx Success ---

        /** RFC 3229 Section 10.4.1 */
        public static final int IM_USED = 226 ;

        // --- 4xx Client Error ---

        /** RFC 8470 Section 10.4.1 */
        public static final int TOO_EARLY = 425 ;

        // --- 5xx Server Error ---

        /** RFC 2295 Section 8.1 */
        public static final int VARIANT_ALSO_NEGOTIATES = 506;

        /**
         * Private constructor.
         */
        private Experimental() {
            // Utility class
        }
    }

    /**
     * Constants for WebDav HTTP status codes.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-06
     * @since 0.1
     */
    @API(status = API.Status.EXPERIMENTAL, since = "0.1")
    public static final class WebDav {

        // --- 1xx Informational ---

        /** RFC 2518 Section 10.1 */
        public static final int PROCESSING = 102;

        // --- 2xx Success ---

        /** RFC 4918 (WevDAV) Section 11.1 */
        public static final int MULTI_STATUS = 207;
        /** RFC 5842 (WevDAV Binding Extensions) Section 7.1 */
        public static final int ALREADY_REPORTED = 208;

        // --- 4xx Client Error ---

        /** RFC 4918 (WevDAV) Section 11.2 */
        public static final int UNPROCESSABLE_ENTITY = 422;
        /** RFC 4918 (WevDAV) Section 11.3 */
        public static final int LOCKED = 423;
        /** RFC 4918 (WevDAV) Section 11.4 */
        public static final int FAILED_DEPENDENCY = 424;

        // --- 5xx Server Error ---

        /** RFC 4918 (WevDAV) Section 11.5 */
        public static final int INSUFFICIENT_STORAGE = 507;
        /** RFC 5842 (WevDAV Binding Extensions) Section 7.2 */
        public static final int LOOP_DETECTED = 508;

        /**
         * Private constructor.
         */
        private WebDav() {
            // Utility class
        }
    }
}
