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
 * Constants for HTTP headers.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public final class Headers {

    /**
     * Private consturctor.
     */
    private Headers() {
        // Utility class
    }

    /**
     * Constants for standard HTTP request headers.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-06
     * @since 0.1
     */
    public static final class Request {

        /**
         * Acceptable instance-manipulations for the request.
         * <p>
         * Status: Permanent
         * From: RFC 3229
         */
        public static final String A_IM = "A-IM";
        /**
         * Media type(s) that is/are acceptable for the response.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String ACCEPT = "Accept";
        /**
         * Character sets that are acceptable.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String ACCEPT_CHARSET = "Accept-Charset";
        /**
         * Acceptable version in time.
         * <p>
         * Status: Provisional
         * From: RFC 7089
         */
        public static final String ACCEPT_DATETIME = "Accept-Datetime";
        /**
         * List of acceptable encodings.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String ACCEPT_ENCODING = "Accept-Encoding";
        /**
         * List of acceptable human languages for response.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String ACCEPT_LANGUAGE = "Accept-Language";
        /**
         * Initiates a request for cross-origin resource sharing with Origin.
         * <p>
         * Status: Permanent
         * From: 
         */
        public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
        /**
         * Initiates a request for cross-origin resource sharing with Origin.
         * <p>
         * Status: Permanent
         * From: 
         */
        public static final String ACCESS_CONTROL_REQUEST_HEADER = "Access-Control-Request-Headers";
        /**
         * Authentication credentials for HTTP authentication.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String AUTHORIZATION = "Authorization";
        /**
         * Used to specify directives that must be obeyed by all caching mechanisms along the request-response chain.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String CACHE_CONTROL = "Cache-Control";
        /**
         * Control options for the current connection and list of hop-by-hop request fields.
         * Must not be used with HTTP/2.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String CONNECTION = "Connection";
        /**
         * The type of encoding used on the data.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String CONTENT_ENCODING = "Content-Encoding";
        /**
         * The length of the request body in octets (8-bit bytes).
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String CONTENT_LENGTH = "Content-Length";
        /**
         * A Base64-encoded binary MD5 sum of the content of the request body.
         * <p>
         * Status: Obsolete
         * From: RFC 1544, RFC 1864, RFC 4021
         */
        public static final String CONTENT_MD5 = "Content-MD5";
        /**
         * The Media type of the body of the request (used with POST and PUT requests).
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String CONTENT_TYPE = "Content-Type";
        /**
         * An HTTP cookie previously sent by the server with Set-Cookie.
         * <p>
         * Status: Permanent
         * From: RFC 2965, RFC 6265
         */
        public static final String COOKIE = "Cookie";
        /**
         * The date and time at which the message was originated (in "HTTP-date"
         * format as defined by RFC 7231 Date/Time Formats).
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String DATE = "Date";
        /**
         * Indicates that particular server behaviors are required by the client.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String EXPECT = "Expect";
        /**
         * Disclose original information of a client connecting to a web server
         * through an HTTP proxy.
         * <p>
         * Status: Permanent
         * From: RFC 7239
         */
        public static final String FORWARDED = "Forwarded";
        /**
         * The email address of the user making the request.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String FROM = "From";
        /**
         * The domain name of the server (for virtual hosting), and the TCP port
         * number on which the server is listening. The port number may be omitted
         * if the port is the standard port for the service requested.
         * <p>
         * Mandatory since HTTP/1.1. If the request is generated directly in
         * HTTP/2, it should not be used.
         * <p>
         * Status: 
         * From: 
         */
        public static final String HOST = "Host";
        /**
         * A request that upgrades from HTTP/1.1 to HTTP/2 MUST include exactly one
         * HTTP2-Setting header field. The HTTP2-Settings header field is a
         * connection-specific header field that includes parameters that govern
         * the HTTP/2 connection, provided in anticipation of the server accepting
         * the request to upgrade.
         * <p>
         * Status: Permanent
         * From: 
         */
        public static final String HTTP2_SETTINGS = "HTTP2-Settings";
        /**
         * Only perform the action if the client supplied entity matches the same
         * entity on the server. This is mainly for methods like PUT to only update
         * a resource if it has not been modified since the user last updated it.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String IF_MATCH = "If-Match";
        /**
         * Allows a 304 Not Modified to be returned if content is unchanged.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
        /**
         * Allows a 304 Not Modified to be returned if content is unchanged, see
         * HTTP ETag.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String IF_NONE_MATCH = "If-None-Match";
        /**
         * If the entity is unchanged, send me the part(s) that I am missing;
         * otherwise, send me the entire new entity.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String IF_RANGE = "If-Range";
        /**
         * Only send the response if the entity has not been modified since a
         * specific time.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String IF_UNMODIFIED_SINCE = "If-Modified-Since";
        /**
         * Limit the number of times the message can be forwarded through proxies
         * or gateways.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String MAX_FORWARDS = "Max-Forwards";
        /**
         * Initiates a request for cross-origin resource sharing (asks server for
         * Access-Control-* response fields).
         * <p>
         * Status: Permanent
         * From: RFC 6454
         */
        public static final String ORIGIN = "Origin";
        /**
         * Implementation-specific fields that may have various effects anywhere
         * along the request-response chain.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String PRAGMA = "Pragma";
        /**
         * Implementation-specific fields that may have various effects anywhere
         * along the request-response chain.
         * <p>
         * Status: Permanent
         * From: RFC 7240
         */
        public static final String PREFER = "Prefer";
        /**
         * Authorization credentials for connecting to a proxy.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
        /**
         * Request only part of an entity. Bytes are numbered from 0.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String RANGE = "Range";
        /**
         * This is the address of the previous web page from which a link to the
         * currently requested page was followed. (The word "referrer" has been
         * misspelled in the RFC as well as in most implementations to the point
         * that it has become standard usage and is considered correct
         * terminology).
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String REFERER = "Referer";
        /**
         * The transfer encodings the user agent is willing to accept: the same
         * values as for the response header field Transfer-Encoding can be used,
         * plus the "trailers" value (related to the "chunked" transfer method) to
         * notify the server it expects to receive additional fields in the trailer
         * after the last, zero-sized, chunk.
         * <p>
         * Only trailers is supported in HTTP/2.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String TE = "TE";
        /**
         * The Trailer general field value indicates that the given set of header
         * fields is present in the trailer of a message encoded with chunked
         * transfer coding.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String TRAILER = "Trailer";
        /**
         * The form of encoding used to safely transfer the entity to the user.
         * Currently defined methods are: chunked, compress, deflate, gzip,
         * identity.
         * <p>
         * Must not be used with HTTP/2.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String TRANSFER_ENCODING = "Transfer-Encoding";
        /**
         * The user agent string of the user agent.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String USER_AGENT = "User-Agent";
        /**
         * Ask the server to upgrade to another protocol.
         * <p>
         * Must not be used in HTTP/2.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String UPGRADE = "Upgrade";
        /**
         * Informs the server of proxies through which the request was sent.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String VIA = "Via";
        /**
         * A general warning about possible problems with the entity body.
         * <p>
         * Status: Permanent
         * From: RFC 7234, RFC 9111
         */
        public static final String WARNING = "Warning";

        /**
         * Private constructor.
         */
        private Request() {
            // Utility class
        }

        /**
         * Constants for non-standard HTTP request headers.
         * 
         * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
         * @version 1.0, 2023-06
         * @since HttpHeaders 1.0
         */
        public static final class NonStandard {

            /**
             * Tells a server which (presumably in the middle of a HTTP ->
             * HTTPS migration) hosts mixed content that the client would
             * prefer redirection to HTTPS and can handle
             * Content-Security-Policy: upgrade-insecure-requests.
             * <p>
             * Must not be used with HTTP/2[
             */
            public static final String UPGRADE_INSECURE_REQUESTS = "Upgrade-Insecure-Requests";
            /**
             * Mainly used to identify Ajax requests (most JavaScript
             * frameworks send this field with value of XMLHttpRequest).
             * Also identifies Android apps using WebView.
             */
            public static final String X_REQUEST_WITH = "X-Requested-With";
            /**
             * Requests a web application to disable their tracking of a user.
             * This is Mozilla's version of the X-Do-Not-Track header field
             * (since Firefox 4.0 Beta 11).
             * Safari and IE9 also have support for this field.
             * On March 7, 2011, a draft proposal was submitted to IETF.
             * The W3C Tracking Protection Working Group is producing a
             * specification.
             */
            public static final String DNT = "DNT";
            /**
             * A de facto standard for identifying the originating IP address
             * of a client connecting to a web server through an HTTP proxy or
             * load balancer.
             * <p>
             * Superseded by Forwarded header.
             */
            public static final String X_FORWARDED_FOR = "X-Forwarded-For";
            /**
             * A de facto standard for identifying the original host requested
             * by the client in the Host HTTP request header, since the host
             * name and/or port of the reverse proxy (load balancer) may differ
             * from the origin server handling the request.
             * <p>
             * Superseded by Forwarded header.
             */
            public static final String X_FORWARDED_HOST = "X-Forwarded-Host";
            /**
             * A de facto standard for identifying the originating protocol of
             * an HTTP request, since a reverse proxy (or a load balancer) may
             * communicate with a web server using HTTP even if the request to
             * the reverse proxy is HTTPS.
             * An alternative form of the header (X-ProxyUser-Ip) is used by
             * Google clients talking to Google servers.
             * <p>
             * Superseded by Forwarded header.
             */
            public static final String X_FORWARDED_PROTO = "X-Forwarded-Proto";
            /**
             * Non-standard header field used by Microsoft applications and
             * load-balancers.
             */
            public static final String FRONT_END_HTTPS = "Front-End-Https";
            /**
             * Requests a web application to override the method specified in
             * the request (typically POST) with the method given in the header
             * field (typically PUT or DELETE).
             * This can be used when a user agent or firewall prevents PUT or
             * DELETE methods from being sent directly (note that this is
             * either a bug in the software component, which ought to be fixed,
             * or an intentional configuration, in which case bypassing it may
             * be the wrong thing to do).
             */
            public static final String X_HTTP_METHOD_OVERRIDE = "X-Http-Method-Override";
            /**
             * Allows easier parsing of the MakeModel/Firmware that is usually
             * found in the User-Agent String of AT&T Devices.
             */
            public static final String X_ATT_DEVICE_ID = "X-ATT-DeviceId";
            /**
             * Links to an XML file on the Internet with a full description and
             * details about the device currently connecting.
             */
            public static final String X_WAP_PROFILE = "X-Wap-Profile";
            /**
             * Implemented as a misunderstanding of the HTTP specifications.
             * Common because of mistakes in implementations of early HTTP
             * versions.
             * Has exactly the same functionality as standard Connection field.
             * <p>
             * Must not be used with HTTP/2.
             */
            public static final String PROXY_CONNECTION = "Proxy-Connection";
            /**
             * Server-side deep packet inspection of a unique ID identifying
             * customers of Verizon Wireless.
             * Also known as "perma-cookie" or "supercookie".
             */
            public static final String X_UIDH = "X-UIDH";
            /**
             * Used to prevent cross-site request forgery.
             * Alternative header names are: X-CSRFToken and X-XSRF-TOKEN.
             */
            public static final String X_CSRF_TOKEN = "X-Csrf-Token";
            /**
             * Correlates HTTP requests between a client and server.
             */
            public static final String X_REQUEST_ID = "X-Request-ID";
            /**
             * Correlates HTTP requests between a client and server.
             */
            public static final String X_CORRELATION_ID = "X-Correlation-ID";
            /**
             * Correlates HTTP requests between a client and server.
             */
            public static final String CORRELATION_ID = "Correlation-ID";
            /**
             * The Save-Data client hint request header available in Chrome,
             * Opera, and Yandex browsers lets developers deliver lighter,
             * faster applications to users who opt-in to data saving mode in
             * their browser.
             */
            public static final String SAVE_DATA = "Save-Data";
            /**
             * The Sec-GPC (Global Privacy Control) request header indicates
             * whether the user consents to a website or service selling or
             * sharing their personal information with third parties.
             */
            public static final String SEC_GPC = "Sec-GPC";

            /**
             * Private constructor.
             */
            private NonStandard() {
                // Utility class
            }
        }

        /**
         * Constants for WebDav HTTP request headers.
         * 
         * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
         * @version 1.0, 2023-06
         * @since HttpHeaders 1.0
         */
        public final class WebDav {

            /** RFC 2518 (WevDAV) Section 9.2 */
            public static final String DEPTH = "Depth";

            /** RFC 2518 (WevDAV) Section 9.3 */
            public static final String DESTINATION = "Destination";

            /** RFC 2518 (WevDAV) Section 9.4 */
            public static final String IF = "If";

            /** RFC 2518 (WevDAV) Section 9.5 */
            public static final String LOCK_TOKEN = "Lock-Token";

            /** RFC 2518 (WevDAV) Section 9.6 */
            public static final String OVERWRITE = "Overwrite";

            /** RFC 2518 (WevDAV) Section 9.8 */
            public static final String TIMEOUT = "Timeout";

            /**
             * Private constructor.
             */
            private WebDav() {
                // Utility class
            }
        }
    }

    /**
     * Constants for standard HTTP response headers.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-06
     * @since 0.1
     */
    public static final class Response {

        /**
         * Requests HTTP Client Hints.
         * <p>
         * Status: Permanent
         * From: RFC 8942
         */
        public static final String ACCEPT_CH = "Accept-CH";
        /**
         * Specifying which web sites can participate in cross-origin resource
         * sharing.
         * <p>
         * Status: Permanent
         * From: RFC 7480
         */
        public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
        /**
         * Specifying which web sites can participate in cross-origin resource
         * sharing.
         * <p>
         * Status: Permanent
         * From: RFC 7480
         */
        public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
        /**
         * Specifying which web sites can participate in cross-origin resource
         * sharing.
         * <p>
         * Status: Permanent
         * From: RFC 7480
         */
        public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
        /**
         * Specifying which web sites can participate in cross-origin resource
         * sharing.
         * <p>
         * Status: Permanent
         * From: RFC 7480
         */
        public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
        /**
         * Specifying which web sites can participate in cross-origin resource
         * sharing.
         * <p>
         * Status: Permanent
         * From: RFC 7480
         */
        public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
        /**
         * Specifying which web sites can participate in cross-origin resource
         * sharing.
         * <p>
         * Status: Permanent
         * From: RFC 7480
         */
        public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
        /**
         * Specifies which patch document formats this server supports.
         * <p>
         * Status: Permanent
         * From: RFC 5789
         */
        public static final String ACCEPT_PATCH = "Accept-Patch";
        /**
         * What partial content range types this server supports via byte
         * serving.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String ACCEPT_RANGES = "Accept-Ranges";
        /**
         * The age the object has been in a proxy cache in seconds.
         * <p>
         * Status: Permanent
         * From: RFC 9111
         */
        public static final String AGE = "Age";
        /**
         * Valid methods for a specified resource.
         * To be used for a 405 Method not allowed.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String ALLOW = "Allow";
        /**
         * A server uses "Alt-Svc" header (meaning Alternative Services) to
         * indicate that its resources can also be accessed at a different
         * network location (host or port) or using a different protocol.
         * <p>
         * When using HTTP/2, servers should instead send an ALTSVC frame.
         * <p>
         * Status: Permanent
         * From: 
         */
        public static final String ALT_SVC = "Alt-Svc";
        /**
         * Tells all caching mechanisms from server to client whether they may
         * cache this object. It is measured in seconds.
         * <p>
         * Status: Permanent
         * From: RFC 9111
         */
        public static final String CACHE_CONTROL = "Cache-Control";
        /**
         * Control options for the current connection and list of hop-by-hop
         * response fields.
         * <p>
         * Must not be used with HTTP/2.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String CONNECTION = "Connection";
        /**
         * An opportunity to raise a "File Download" dialogue box for a known
         * MIME type with binary format or suggest a filename for dynamic
         * content. Quotes are necessary with special characters.
         * <p>
         * Status: Permanent
         * From: RFC 2616, RFC 4021, RFC 6266
         */
        public static final String CONTENT_DISPOSITION = "Content-Disposition";
        /**
         * The type of encoding used on the data.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String CONTENT_ENCODING = "Content-Encoding";
        /**
         * The natural language or languages of the intended audience for the
         * enclosed content.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String CONTENT_LANGUAGE = "Content-Language";
        /**
         * The length of the response body in octets (8-bit bytes).
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String CONTENT_LENGTH = "Content-Length";
        /**
         * An alternate location for the returned data.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String CONTENT_LOCATION = "Content-Location";
        /**
         * A Base64-encoded binary MD5 sum of the content of the response.
         * <p>
         * Status: Obsolete
         * From: RFC 1544, RFC 1864, RFC 4021
         */
        public static final String CONTENT_MD5 = "Content-MD5";
        /**
         * Where in a full body message this partial message belongs.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String CONTENT_RANGE = "Content-Range";
        /**
         * The MIME type of this content.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String CONTENT_TYPE = "Content-Type";
        /**
         * The date and time that the message was sent (in "HTTP-date" format
         * as defined by RFC 9110).
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String DATE = "Date";
        /**
         * Specifies the delta-encoding entity tag of the response.
         * <p>
         * Status: Permanent
         * From: RFC 3229
         */
        public static final String DELTA_BASE = "Delta-Base";
        /**
         * An identifier for a specific version of a resource, often a message
         * digest.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String ETAG = "ETag";
        /**
         * Gives the date/time after which the response is considered stale
         * (in "HTTP-date" format as defined by RFC 9110).
         * <p>
         * Status: Permanent
         * From: RFC 9111
         */
        public static final String EXPIRES = "Expires";
        /**
         * Instance-manipulations applied to the response.
         * <p>
         * Status: Permanent
         * From: RFC 3229
         */
        public static final String IM = "IM";
        /**
         * The last modified date for the requested object
         * (in "HTTP-date" format as defined by RFC 9110).
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String LAST_MODIFIED = "Last-Modified";
        /**
         * Used to express a typed relationship with another resource,
         * where the relation type is defined by RFC 5988.
         * <p>
         * Status: Permanent
         * From: RFC 5988
         */
        public static final String LINK = "Link";
        /**
         * Used in redirection, or when a new resource has been created.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String LOCATION = "Location";
        /**
         * Set P3P policy.
         * <p>
         * Status: Permanent
         * From: 
         */
        public static final String P3P = "P3P";
        /**
         * Implementation-specific fields that may have various effects
         * anywhere along the request-response chain.
         * <p>
         * Status: Permanent
         * From: RFC 9111
         */
        public static final String PRAGMA = "Pragma";
        /**
         * Indicates which Prefer tokens were honored by the server and applied
         * to the processing of the request.
         * <p>
         * Status: Permanent
         * From: RFC 7240
         */
        public static final String PREFERENCE_APPLIED = "Preference-Applied";
        /**
         * Request authentication to access the proxy.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
        /**
         * HTTP Public Key Pinning, announces hash of website's authentic TLS
         * certificate.
         * <p>
         * Status: Permanent
         * From: RFC 7469
         */
        public static final String PUBLIC_KEY_PINS = "Public-Key-Pins";
        /**
         * If an entity is temporarily unavailable, this instructs the client
         * to try again later.
         * Value could be a specified period of time (in seconds) or a
         * HTTP-date.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String RETRY_AFTER = "Retry-After";
        /**
         * A name for the server
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String SERVER = "Server";
        /**
         * An HTTP cookie.
         * <p>
         * Status: Permanent
         * From: RFC 6265
         */
        public static final String SET_COOKIE = "Set-Cookie";
        /**
         * A HSTS Policy informing the HTTP client how long to cache the HTTPS
         * only policy and whether this applies to subdomains.
         * <p>
         * Status: Permanent
         * From: 
         */
        public static final String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";
        /**
         * The Trailer general field value indicates that the given set of
         * header fields is present in the trailer of a message encoded with
         * chunked transfer coding.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String TRAILER = "Trailer";
        /**
         * The form of encoding used to safely transfer the entity to the user.
         * Currently defined methods are: chunked, compress, deflate, gzip,
         * identity.
         * <p>
         * Must not be used with HTTP/2.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String TRANSFER_ENCODING = "Transfer-Encoding";
        /**
         * Tracking Status header, value suggested to be sent in response to a
         * DNT(do-not-track), possible values:
         * <ul>
         * <li>"!" — under construction</li>
         * <li>"?" — dynamic</li>
         * <li>"G" — gateway to multiple parties</li>
         * <li>"N" — not tracking</li>
         * <li>"T" — tracking</li>
         * <li>"C" — tracking with consent</li>
         * <li>"P" — tracking only if consented</li>
         * <li>"D" — disregarding DNT</li>
         * <li>"U" — updated</li>
         * </ul>
         * <p>
         * Status: Permanent
         * From: 
         */
        public static final String TK = "Tk";
        /**
         * Ask the client to upgrade to another protocol.
         * <p>
         * Must not be used in HTTP/2.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String UPGRADE = "Upgrade";
        /**
         * Tells downstream proxies how to match future request headers to
         * decide whether the cached response can be used rather than
         * requesting a fresh one from the origin server.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String VARY = "Vary";
        /**
         * Informs the client of proxies through which the response was sent.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String VIA = "Via";
        /**
         * A general warning about possible problems with the entity body.
         * <p>
         * Status: Obsolete
         * From: RFC 7234, RFC 9111
         */
        public static final String WARNING = "Warning";
        /**
         * Indicates the authentication scheme that should be used to access
         * the requested entity.
         * <p>
         * Status: Permanent
         * From: RFC 9110
         */
        public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
        /**
         * Clickjacking protection:
         * <ul>
         * <li>deny - no rendering within a frame</li>
         * <li>sameorigin - no rendering if origin mismatch</li>
         * <li>allow-from - allow from specified location</li>
         * <li>allowall - non-standard, allow from any location</li>
         * </ul>
         * <p>
         * Status: Obsolete
         * From: 
         */
        public static final String X_FRAME_OPTIONS = "X-Frame-Options";

        /**
         * Private constructor.
         */
        private Response() {
            // Utility class
        }

        /**
         * Constants for non-standard HTTP response headers.
         * 
         * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
         * @version 1.0, 2023-06
         * @since HttpHeaders 1.0
         */
        public static final class NonStandard {

            /**
             * Content Security Policy definition.
             */
            public static final String CONTENT_SECURITY_POLICY = "Content-Security-Policy";
            /**
             * Content Security Policy definition.
             */
            public static final String X_CONTENT_SECURITY_POLICY = "X-Content-Security-Policy";
            /**
             * Content Security Policy definition.
             */
            public static final String X_WEBKIT_CSP = "X-WebKit-CSP";
            /**
             * Notify to prefer to enforce Certificate Transparency.
             */
            public static final String EXPECT_CT = "Expect-CT";
            /**
             * Used to configure network request logging.
             */
            public static final String NEL = "NEL";
            /**
             * To allow or disable different features or APIs of the browser.
             */
            public static final String PERMISSIONS_POLICY = "Permissions-Policy";
            /**
             * Used in redirection, or when a new resource has been created.
             * This refresh redirects after 5 seconds.
             * Header extension introduced by Netscape and supported by most
             * web browsers.
             * Defined by HTML Standard.
             */
            public static final String REFRESH = "Refresh";
            /**
             * Instructs the user agent to store reporting endpoints for an
             * origin.
             */
            public static final String REPORT_TO = "Report-To";
            /**
             * CGI header field specifying the status of the HTTP response.
             * Normal HTTP responses use a separate "Status-Line" instead,
             * defined by RFC 9110.
             */
            public static final String STATUS = "Status";
            /**
             * The Timing-Allow-Origin response header specifies origins that
             * are allowed to see values of attributes retrieved via features
             * of the Resource Timing API, which would otherwise be reported
             * as zero due to cross-origin restrictions.
             */
            public static final String TIMING_ALLOW_ORIGIN = "Timing-Allow-Origin";
            /**
             * Provide the duration of the audio or video in seconds.
             * Only supported by Gecko browsers.
             */
            public static final String X_CONTENT_DURATION = "X-Content-Duration";
            /**
             * The only defined value, "nosniff", prevents Internet Explorer
             * from MIME-sniffing a response away from the declared
             * content-type.
             * This also applies to Google Chrome, when downloading extensions.
             */
            public static final String X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";
            /**
             * Specifies the technology (e.g. ASP.NET, PHP, JBoss) supporting
             * the web application (version details are often in X-Runtime,
             * X-Version, or X-AspNet-Version)
             */
            public static final String X_POWERED_BY = "X-Powered-By";
            /**
             * Specifies the component that is responsible for a particular
             * redirect.
             */
            public static final String X_REDIRECT_BY = "X-Redirect-By";
            /**
             * Correlates HTTP requests between a client and server.
             */
            public static final String X_REQUEST_ID = "X-Request-ID";
            /**
             * Correlates HTTP requests between a client and server.
             */
            public static final String X_CORRELATION_ID = "X-Correlation-ID";
            /**
             * Recommends the preferred rendering engine (often a
             * backward-compatibility mode) to use to display the content.
             * Also used to activate Chrome Frame in Internet Explorer.
             * In HTML Standard, only the IE=edge value is defined.
             */
            public static final String X_UA_COMPATIBLE = "X-UA-Compatible";
            /**
             * Cross-site scripting (XSS) filter.
             */
            public static final String X_XSS_PROTECTION = "X-XSS-Protection";

            /**
             * Private constructor.
             */
            private NonStandard() {
                // Utility class
            }
        }

        /**
         * Constants for WebDav HTTP response headers.
         * 
         * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
         * @version 1.0, 2023-06
         * @since HttpHeaders 1.0
         */
        public final class WebDav {

            /** RFC 2518 (WevDAV) Section 9.1 */
            public static final String DAV = "Dav";

            /** RFC 2518 (WevDAV) Section 9.7 */
            public static final String STATUS_URI = "Status-URI";

            /**
             * Private constructor.
             */
            private WebDav() {
                // Utility class
            }
        }
    }
}
