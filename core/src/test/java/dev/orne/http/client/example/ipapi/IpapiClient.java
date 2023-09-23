package dev.orne.http.client.example.ipapi;

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

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.validation.constraints.NotNull;

import dev.orne.http.client.AuthenticationRequiredException;
import dev.orne.http.client.AuthorizationException;
import dev.orne.http.client.BaseAuthenticableHttpServiceClient;
import dev.orne.http.client.CredentialsInvalidException;
import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.engine.HttpClientEngine;
import dev.orne.http.client.op.AuthenticationOperation;
import dev.orne.http.client.op.StatusInitOperation;

/**
 * Example {@code IpAPI} client.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-08
 * @since 0.1
 * @see {@linkplain https://ipapi.com/}
 */
public class IpapiClient
extends BaseAuthenticableHttpServiceClient<IpapiClientStatus, String> {

    /** The default {@code IpAPI} service base URI. */
    public static final String API_URI = "http://api.ipapi.com/api/";

    /** The status initialization operation. */
    private static final StatusInitOperation<IpapiClientStatus> INIT_OP = (params, client) -> {
        return CompletableFuture.completedFuture(new IpapiClientStatus());
    };
    /** The authentication operation. */
    private static final AuthenticationOperation<String, ?, IpapiClientStatus> AUTH_OP = (accessKey, status, client) -> {
        status.setAccessKey(accessKey);
        return CompletableFuture.completedFuture(null);
    };
    /** The standard (single IP) operation. */
    private static final IpapiStandardOperation STANDARD_OP = new IpapiStandardOperation();
    /** The origin (caller IP) operation. */
    private static final IpapiOriginOperation ORIGIN_OP = new IpapiOriginOperation();

    /**
     * Creates a new instance with the {@code IpAPI} service base URI.
     * 
     * @param engine The HTTP client engine.
     */
    public IpapiClient(
            final @NotNull HttpClientEngine engine) {
        this(engine, URI.create(API_URI));
    }

    /**
     * Creates a new instance with the specified {@code IpAPI} service base
     * URI.
     * 
     * @param engine The HTTP client engine.
     * @param baseURI The HTTP service's base URI
     */
    public IpapiClient(
            final @NotNull HttpClientEngine engine,
            final @NotNull URI baseURI) {
        super(engine, baseURI, INIT_OP, AUTH_OP);
    }

    /**
     * Creates a new instance with the specified {@code IpAPI} service base
     * URL.
     * 
     * @param engine The HTTP client engine.
     * @param baseURL The HTTP service's base URL
     */
    public IpapiClient(
            final @NotNull HttpClientEngine engine,
            final @NotNull URL baseURL)
    throws URISyntaxException {
        super(engine, baseURL, INIT_OP, AUTH_OP);
    }

    /**
     * Returns the IP information of the caller IP.
     * 
     * @return The IP information promise.
     */
    public @NotNull CompletionStage<IpapiResult> getIpInfo() {
        return getIpInfo((IpapiParams) null);
    }

    /**
     * Returns the IP information of the caller IP.
     * 
     * @param params The request parameters.
     * @return The IP information promise.
     */
    public @NotNull CompletionStage<IpapiResult> getIpInfo(
            final IpapiParams params) {
        return execute(ORIGIN_OP, params);
    }

    /**
     * Returns the IP information of the specified IP.
     * 
     * @param ip The IP to obtain the information for.
     * @return The IP information promise.
     */
    public @NotNull CompletionStage<IpapiResult> getIpInfo(
            final @NotNull String ip) {
        return getIpInfo(ip, null);
    }

    /**
     * Returns the IP information of the specified IP.
     * 
     * @param ip The IP to obtain the information for.
     * @param params The request parameters.
     * @return The IP information promise.
     */
    public @NotNull CompletionStage<IpapiResult> getIpInfo(
            final @NotNull String ip,
            final IpapiParams params) {
        final IpapiStandardOperation.Params opParams;
        if (params == null) {
            opParams = new IpapiStandardOperation.Params(ip);
        } else {
            opParams = new IpapiStandardOperation.Params(ip, params);
        }
        return execute(STANDARD_OP, opParams);
    }

    /**
     * {@code IpAPI} error for missing access key error.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     */
    public static class MissingAccessKeyException
    extends AuthenticationRequiredException {
        private static final long serialVersionUID = 1L;
        public MissingAccessKeyException() {
            super();
        }
        public MissingAccessKeyException(String message) {
            super(message);
        }
        public MissingAccessKeyException(Throwable cause) {
            super(cause);
        }
        public MissingAccessKeyException(String message, Throwable cause) {
            super(message, cause);
        }
        public MissingAccessKeyException(String message, Throwable cause, boolean enableSuppression,
                boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
    /**
     * {@code IpAPI} error for invalid access key error.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     */
    public static class InvalidAccessKeyException
    extends CredentialsInvalidException {
        private static final long serialVersionUID = 1L;
        public InvalidAccessKeyException() {
            super();
        }
        public InvalidAccessKeyException(String message) {
            super(message);
        }
        public InvalidAccessKeyException(Throwable cause) {
            super(cause);
        }
        public InvalidAccessKeyException(String message, Throwable cause) {
            super(message, cause);
        }
        public InvalidAccessKeyException(String message, Throwable cause, boolean enableSuppression,
                boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
    /**
     * {@code IpAPI} error for inactive access key error.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     */
    public static class InactiveAccessKeyException
    extends CredentialsInvalidException {
        private static final long serialVersionUID = 1L;
        public InactiveAccessKeyException() {
            super();
        }
        public InactiveAccessKeyException(String message) {
            super(message);
        }
        public InactiveAccessKeyException(Throwable cause) {
            super(cause);
        }
        public InactiveAccessKeyException(String message, Throwable cause) {
            super(message, cause);
        }
        public InactiveAccessKeyException(String message, Throwable cause, boolean enableSuppression,
                boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
    /**
     * {@code IpAPI} error for invalid function call error.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     */
    public static class InvalidApiFunctionException
    extends HttpClientException {
        private static final long serialVersionUID = 1L;
        public InvalidApiFunctionException() {
            super();
        }
        public InvalidApiFunctionException(String message) {
            super(message);
        }
        public InvalidApiFunctionException(Throwable cause) {
            super(cause);
        }
        public InvalidApiFunctionException(String message, Throwable cause) {
            super(message, cause);
        }
        public InvalidApiFunctionException(String message, Throwable cause, boolean enableSuppression,
                boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
    /**
     * {@code IpAPI} error for usage limit reached error.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     */
    public static class UsageLimitReachedException
    extends AuthorizationException {
        private static final long serialVersionUID = 1L;
        public UsageLimitReachedException() {
            super();
        }
        public UsageLimitReachedException(String message) {
            super(message);
        }
        public UsageLimitReachedException(Throwable cause) {
            super(cause);
        }
        public UsageLimitReachedException(String message, Throwable cause) {
            super(message, cause);
        }
        public UsageLimitReachedException(String message, Throwable cause, boolean enableSuppression,
                boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
    /**
     * {@code IpAPI} error for access restriction error.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     */
    public static class FunctionAccessRestrictedException
    extends AuthorizationException {
        private static final long serialVersionUID = 1L;
        public FunctionAccessRestrictedException() {
            super();
        }
        public FunctionAccessRestrictedException(String message) {
            super(message);
        }
        public FunctionAccessRestrictedException(Throwable cause) {
            super(cause);
        }
        public FunctionAccessRestrictedException(String message, Throwable cause) {
            super(message, cause);
        }
        public FunctionAccessRestrictedException(String message, Throwable cause, boolean enableSuppression,
                boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
    /**
     * {@code IpAPI} error for access access error.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     */
    public static class HttpsAccessRestrictedException
    extends AuthorizationException {
        private static final long serialVersionUID = 1L;
        public HttpsAccessRestrictedException() {
            super();
        }
        public HttpsAccessRestrictedException(String message) {
            super(message);
        }
        public HttpsAccessRestrictedException(Throwable cause) {
            super(cause);
        }
        public HttpsAccessRestrictedException(String message, Throwable cause) {
            super(message, cause);
        }
        public HttpsAccessRestrictedException(String message, Throwable cause, boolean enableSuppression,
                boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
    /**
     * {@code IpAPI} error for invalid request fields error.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     */
    public static class InvalidFieldsException
    extends HttpClientException {
        private static final long serialVersionUID = 1L;
        public InvalidFieldsException() {
            super();
        }
        public InvalidFieldsException(String message) {
            super(message);
        }
        public InvalidFieldsException(Throwable cause) {
            super(cause);
        }
        public InvalidFieldsException(String message, Throwable cause) {
            super(message, cause);
        }
        public InvalidFieldsException(String message, Throwable cause, boolean enableSuppression,
                boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
    /**
     * {@code IpAPI} error for too many IPs in the request error.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     */
    public static class TooManyIpsException
    extends HttpClientException {
        private static final long serialVersionUID = 1L;
        public TooManyIpsException() {
            super();
        }
        public TooManyIpsException(String message) {
            super(message);
        }
        public TooManyIpsException(Throwable cause) {
            super(cause);
        }
        public TooManyIpsException(String message, Throwable cause) {
            super(message, cause);
        }
        public TooManyIpsException(String message, Throwable cause, boolean enableSuppression,
                boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
    /**
     * {@code IpAPI} error for batch request no supported error.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     */
    public static class BatchNotSupportedException
    extends AuthorizationException {
        private static final long serialVersionUID = 1L;
        public BatchNotSupportedException() {
            super();
        }
        public BatchNotSupportedException(String message) {
            super(message);
        }
        public BatchNotSupportedException(Throwable cause) {
            super(cause);
        }
        public BatchNotSupportedException(String message, Throwable cause) {
            super(message, cause);
        }
        public BatchNotSupportedException(String message, Throwable cause, boolean enableSuppression,
                boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
