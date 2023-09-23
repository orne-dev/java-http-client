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

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dev.orne.http.ContentType;
import dev.orne.http.MediaTypes;
import dev.orne.http.UriBuilder;
import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.HttpResponseHandlingException;
import dev.orne.http.client.body.DelegatedHttpRequestBodyParser;
import dev.orne.http.client.body.HttpResponseBodyParser;
import dev.orne.http.client.body.JacksonHttpBody;
import dev.orne.http.client.body.JaxbHttpBody;
import dev.orne.http.client.engine.HttpResponse;
import dev.orne.http.client.engine.HttpResponseBody;
import dev.orne.http.client.op.AuthenticatedOperation;

/**
 * Operation for {@code IpAPI} single IP information retrieval.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-08
 * @since 0.1
 * @see {@linkplain https://ipapi.com/documentation#standard_lookup}
 */
public class IpapiStandardOperation
extends IpapiAbstractOperation<IpapiStandardOperation.Params, IpapiStandardOperation.Response, IpapiResult>
implements AuthenticatedOperation<IpapiStandardOperation.Params, IpapiResult, IpapiClientStatus> {

    /** The response body entity parser. */
    private final @NotNull HttpResponseBodyParser<Response> PARSER;

    /**
     * Creates a new instance.
     * <p>
     * This class has no state, so multiple instances have no sense.
     */
    public IpapiStandardOperation() {
        super();
        try {
            PARSER = new DelegatedHttpRequestBodyParser<>(
                    ContentType.of(MediaTypes.Application.JSON, StandardCharsets.UTF_8),
                    JacksonHttpBody.parser(Response.class),
                    JaxbHttpBody.parser(ErrorResponse.class));
        } catch (HttpClientException e) {
            throw new IllegalStateException("Error creating operation parsers", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configureOperationUri(
            final Params params,
            final @NotNull IpapiClientStatus status,
            final @NotNull UriBuilder builder) {
        if (params == null) {
            throw new IllegalArgumentException("The IP to check is requeired.");
        }
        builder.setPath(params.getIp());
        super.configureOperationUri(params, status, builder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Response parseResponse(
            final Params params,
            final @NotNull IpapiClientStatus status,
            final @NotNull HttpResponse response,
            final @NotNull HttpResponseBody body)
    throws HttpResponseHandlingException {
        return body.parse(PARSER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected IpapiResult processResponse(
            final Params params,
            final @NotNull IpapiClientStatus status,
            final Response entity,
            final @NotNull HttpResponse response)
    throws HttpClientException {
        if (entity instanceof IpapiError) {
            throw processApiError((IpapiError) entity, response);
        }
        return (IpapiResult) entity;
    }

    /**
     * The operation parameters. The common parameters plus the IP to retrieve
     * the information for.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     * @see {@linkplain https://ipapi.com/documentation#standard_lookup}
     */
    public static class Params
    extends IpapiParams {

        /** The IP to retrieve the information for. */
        private final @NotNull String ip;

        /**
         * Creates a new instance with no common parameters.
         * 
         * @param ip The IP to retrieve the information for.
         */
        public Params(
                final @NotNull String ip) {
            super();
            this.ip = Validate.notNull(ip);
        }
        /**
         * Creates a new instance.
         * 
         * @param ip The IP to retrieve the information for.
         * @param commons The common API parameters.
         */
        public Params(
                final @NotNull String ip,
                final @NotNull IpapiParams commons) {
            super(commons);
            this.ip = Validate.notNull(ip);
        }
        public Params(
                final @NotNull Params copy) {
            super(copy);
            this.ip = copy.ip;
        }
        /**
         * Returns the IP to retrieve the information for.
         * 
         * @return The IP to retrieve the information for.
         */
        public @NotNull String getIp() {
            return this.ip;
        }
    }

    /**
     * Interface for {@code IpAPI} API standard operation response.
     * <p>
     * {@code IpAPI} has a bizarre API: If the request is successful returns
     * the response in the requested format, but if the response fails returns
     * a 200 HTTP status code with the API error in JSON format (whatever the
     * requested format is).
     * <p>
     * This interface provides Jackson configuration that detects if the JSON
     * body is a successful response or an API error and deserializes the
     * appropriate bean.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     * @see {@linkplain https://ipapi.com/documentation}
     */
    @JsonDeserialize(using = ResponseDeserializer.class)
    protected interface Response {}

    /**
     * Extension of {@code IpapiResult} that implements standard response interface.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     */
    @XmlRootElement(name = "result", namespace = "")
    protected class SuccessfulResponse
    extends IpapiResult
    implements Response {}

    /**
     * Extension of {@code IpapiError} that implements standard response interface.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     */
    protected class ErrorResponse
    extends IpapiError
    implements Response {}

    /**
     * The Jackson deserializer for standard operation response entities.
     * <p>
     * Detects if the JSON body is a successful response or an API error
     * and deserializes the appropriate bean.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     */
    protected class ResponseDeserializer
    extends StdDeserializer<Response>
    implements ResolvableDeserializer {

        private static final long serialVersionUID = 1L;

        /** The successful response deserializer. */
        private JsonDeserializer<Object> resultDeserializer;
        /** The error response deserializer. */
        private JsonDeserializer<Object> errorDeserializer;

        /**
         * Creates a new instance.
         */
        public ResponseDeserializer() {
            super(Response.class);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void resolve(
                final DeserializationContext ctxt)
        throws JsonMappingException {
            resultDeserializer = ctxt.findRootValueDeserializer(ctxt.getTypeFactory().constructType(
                    SuccessfulResponse.class));
            errorDeserializer = ctxt.findRootValueDeserializer(ctxt.getTypeFactory().constructType(
                    ErrorResponse.class));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Response deserialize(
                final JsonParser p,
                final DeserializationContext ctxt)
        throws IOException, JacksonException {
            final Response result;
            final ObjectNode tree = p.readValueAsTree();
            final JsonParser p2 = tree.traverse(p.getCodec());
            p2.nextToken();
            if (tree.has("success") && !tree.get("success").asBoolean()) {
                result = (ErrorResponse) errorDeserializer.deserialize(p2, ctxt);
            } else {
                result = (SuccessfulResponse) resultDeserializer.deserialize(p2, ctxt);
            }
            return result;
        }
    }
}
