package dev.orne.http.client;

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
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.http.client.cookie.CookieStore;
import dev.orne.http.client.engine.HttpClientEngine;
import dev.orne.http.client.op.StatusIndependentOperation;

/**
 * Unit tests for {@code BaseHttpServiceClient}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 * @see BaseHttpServiceClient
 */
@Tag("ut")
class BaseHttpServiceClientTest {

    /** The shared engine. */
    protected @Mock HttpClientEngine engine;
    private AutoCloseable mocks;

    @BeforeEach public void openMocks() {
        this.mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach public void releaseMocks() throws Exception {
        this.mocks.close();
    }

    /**
     * Test for {@link BaseHttpServiceClient#BaseHttpServiceClient(HttpClientEngine, URI)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testUriConstructor_requiredParameters()
    throws Throwable {
        final HttpClientEngine engine = mock(HttpClientEngine.class);
        final URI uri = URI.create("http://example.org/base/path/");
        assertThrows(NullPointerException.class, () -> {
            new BaseHttpServiceClient(engine, (URI) null);
        });
        assertThrows(NullPointerException.class, () -> {
            new BaseHttpServiceClient(null, uri);
        });
    }

    /**
     * Test for {@link BaseHttpServiceClient#BaseHttpServiceClient(HttpClientEngine, URI)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testUriConstructor()
    throws Throwable {
        final HttpClientEngine engine = mock(HttpClientEngine.class);
        final URI uri = URI.create("http://example.org/base/path/");
        try (final BaseHttpServiceClient client = new BaseHttpServiceClient(engine, uri)) {
            assertSame(engine, client.getEngine());
            assertEquals(uri, client.getBaseURI());
        }
    }

    /**
     * Test for {@link BaseHttpServiceClient#BaseHttpServiceClient(HttpClientEngine, URL)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testUrlConstructor_requiredParameters()
    throws Throwable {
        final HttpClientEngine engine = mock(HttpClientEngine.class);
        final URL url = new URL("http://example.org/base/path/");
        assertThrows(NullPointerException.class, () -> {
            new BaseHttpServiceClient(engine, (URL) null);
        });
        assertThrows(NullPointerException.class, () -> {
            new BaseHttpServiceClient(null, url);
        });
    }

    /**
     * Test for {@link BaseHttpServiceClient#BaseHttpServiceClient(HttpClientEngine, URL)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testUrlConstructor()
    throws Throwable {
        final HttpClientEngine engine = mock(HttpClientEngine.class);
        final URI uri = URI.create("http://example.org/base/path/");
        final URL url = new URL("http://example.org/base/path/");
        try (final BaseHttpServiceClient client = new BaseHttpServiceClient(engine, url)) {
            assertSame(engine, client.getEngine());
            assertEquals(uri, client.getBaseURI());
        }
    }

    protected @NotNull BaseHttpServiceClient createTestClient() {
        return new BaseHttpServiceClient(this.engine, URI.create("http://example.org/base/path/"));
    }

    /**
     * Test for {@link BaseHttpServiceClient#getCookieStore()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetCookieStore()
    throws Throwable {
        final CookieStore store = mock(CookieStore.class);
        given(engine.getCookieStore()).willReturn(store);
        try (final BaseHttpServiceClient client = createTestClient()) {
            then(engine).shouldHaveNoInteractions();
            final CookieStore result = client.getCookieStore();
            assertSame(store, result);
            then(engine).should().getCookieStore();
            then(engine).shouldHaveNoMoreInteractions();
        }
    }

    /**
     * Test for {@link BaseHttpServiceClient#close()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testClose()
    throws Throwable {
        try (final BaseHttpServiceClient client = createTestClient()) {
            then(engine).shouldHaveNoInteractions();
        }
        then(engine).should().close();
        then(engine).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link BaseHttpServiceClient#execute(StatusIndependentOperation, Object)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("operationParameters")
    void testExecute(
            final Object params)
    throws Throwable {
        @SuppressWarnings("unchecked")
        final StatusIndependentOperation<Object, Object> operation =
                mock(StatusIndependentOperation.class);
        final CompletableFuture<Object> mockResult = new CompletableFuture<Object>();
        try (final BaseHttpServiceClient client = createTestClient()) {
            given(operation.execute(params, client)).willReturn(mockResult);
            final CompletionStage<Object> result = client.execute(operation, params);
            assertNotNull(result);
            assertSame(mockResult, result);
            then(operation).should().execute(params, client);
        }
    }

    private static Stream<Arguments> operationParameters() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(new Object())
            );
    }
}
