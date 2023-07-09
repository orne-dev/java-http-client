package dev.orne.http.client.op;

/*-
 * #%L
 * Orne HTTP Client
 * %%
 * Copyright (C) 2020 - 2021 Orne Developments
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.net.URI;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import dev.orne.http.StatusCodes;
import dev.orne.http.client.AuthenticationRequiredException;
import dev.orne.http.client.HttpClientException;
import dev.orne.http.client.HttpResponseStatusException;
import dev.orne.http.client.HttpServiceClient;
import dev.orne.http.client.engine.HttpResponse;

/**
 * Unit tests for {@code AbstractHttpServiceOperation}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 * @see AbstractHttpServiceOperation
 */
@Tag("ut")
class AbstractHttpServiceOperationTest {

    /**
     * Creates the operation to be tested.
     * 
     * @return The operation created
     */
    protected AbstractHttpServiceOperation<?> createOperation() {
        return spy(AbstractHttpServiceOperation.class);
    }

    protected HttpServiceClient createMockClient() {
        return mock(HttpServiceClient.class);
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#resolveRequestURI(URI, HttpServiceClient)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource
    void testResolveRequestURI(
            final @NotNull String clientBaseUri,
            final @NotNull String operationUri,
            final @NotNull String requestUri)
    throws Throwable {
        final AbstractHttpServiceOperation<?> operation = createOperation();
        final URI clientBaseURI = URI.create(clientBaseUri);
        final URI operationURI = URI.create(operationUri);
        final URI requestURI = URI.create(requestUri);
        final HttpServiceClient client = createMockClient();
        given(client.getBaseURI()).willReturn(clientBaseURI);
        final URI result = operation.resolveRequestURI(operationURI, client);
        assertNotNull(result);
        assertEquals(requestURI, result);
    }

    private static Stream<Arguments> testResolveRequestURI() {
        return Stream.of(
            Arguments.of(
                "http://example.org/base/path/",
                "op/path?q=test#frag",
                "http://example.org/base/path/op/path?q=test#frag"),
            Arguments.of(
                    "http://example.org/base/path/",
                    "/op/path?q=test#frag",
                    "http://example.org/op/path?q=test#frag"),
            Arguments.of(
                    "http://example.org/base/path/",
                    "http://other.example.org/op/path?q=test#frag",
                    "http://other.example.org/op/path?q=test#frag")
        );
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#processResponseStatus(HttpResponse)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("validStatusCodes")
    void testProcessResponseStatus(
            final int statusCode)
    throws Throwable {
        final AbstractHttpServiceOperation<?> operation = createOperation();
        final HttpResponse httpResponse = mock(HttpResponse.class);
        final String statusReason = RandomStringUtils.random(50);
        given(httpResponse.getStatusCode()).willReturn(statusCode);
        given(httpResponse.getStatusReason()).willReturn(statusReason);
        if (statusCode < 200 || statusCode >= 300) {
            final HttpResponseStatusException result = assertThrows(
                    HttpResponseStatusException.class,
                    () -> operation.processResponseStatus(httpResponse));
            assertEquals(statusCode, result.getStatusCode());
            assertEquals(statusReason, result.getStatusReason());
        } else {
            assertDoesNotThrow(() -> operation.processResponseStatus(httpResponse));
        }
    }

    /**
     * Test for {@link AbstractHttpServiceOperation#processResponseStatusException(HttpResponse, HttpResponseStatusException)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("validStatusCodes")
    void testProcessResponseStatusException(
            final int statusCode)
    throws Throwable {
        final AbstractHttpServiceOperation<?> operation = createOperation();
        final HttpResponse httpResponse = mock(HttpResponse.class);
        final HttpResponseStatusException exception = new HttpResponseStatusException(
                statusCode, "Mock reason", "Mock exception");
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.processResponseStatusException(
                    httpResponse, exception);
        });
        assertNotNull(result);
        switch (statusCode) {
            case StatusCodes.UNAUTHORIZED:
                assertInstanceOf(AuthenticationRequiredException.class, result);
                assertSame(exception, result.getCause());
                break;
            default:
                assertSame(exception, result);
                break;
        }
    }

    private static IntStream validStatusCodes() {
        return IntStream.range(100, 600);
    }
}
