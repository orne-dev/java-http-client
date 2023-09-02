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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import dev.orne.test.rnd.Generators;

/**
 * Unit tests for {@code UriBuilder}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-09
 * @since 0.1
 * @see UriBuilder
 */
@Tag("ut")
class UriBuilderTest {

    /** Container of mock factory call parameters. */
    private static final List<List<Object>> FACTORY_CALLS =
            new ArrayList<>();

    /**
     * Clear all factory calls.
     */
    @AfterEach
    void clearFactoryCalls() {
        FACTORY_CALLS.clear();
    }

    /**
     * Test for {@link UriBuilder.FactoryLoader#getFactory()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFactoryLoader()
    throws Throwable {
        final UriBuilder.Factory result = UriBuilder.FactoryLoader.getFactory();
        assertInstanceOf(MockUriBuilderFactory.class, result);
        final UriBuilder.Factory other = UriBuilder.FactoryLoader.getFactory();
        assertSame(result, other);
    }

    /**
     * Test for {@link UriBuilder#create()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testCreateEmpty()
    throws Throwable {
        final UriBuilder result = UriBuilder.create();
        assertNotNull(result);
        assertEquals(1, FACTORY_CALLS.size());
        assertTrue(FACTORY_CALLS.get(0).isEmpty());
    }

    /**
     * Test for {@link UriBuilder#create(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testCreateString()
    throws Throwable {
        final String uri = Generators.randomValue(String.class);
        assertThrows(NullPointerException.class, () -> UriBuilder.create((String) null));
        final UriBuilder result = UriBuilder.create(uri);
        assertNotNull(result);
        assertEquals(1, FACTORY_CALLS.size());
        assertEquals(2, FACTORY_CALLS.get(0).size());
        assertEquals(uri, FACTORY_CALLS.get(0).get(0));
        assertEquals(StandardCharsets.UTF_8, FACTORY_CALLS.get(0).get(1));
    }

    /**
     * Test for {@link UriBuilder#create(String, Charset)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testCreateStringCharset()
    throws Throwable {
        final String uri = Generators.randomValue(String.class);
        final Charset charset = Generators.randomValue(Charset.class);
        assertThrows(NullPointerException.class, () -> UriBuilder.create((String) null, (Charset) null));
        assertThrows(NullPointerException.class, () -> UriBuilder.create(uri, (Charset) null));
        assertThrows(NullPointerException.class, () -> UriBuilder.create((String) null, charset));
        final UriBuilder result = UriBuilder.create(uri, charset);
        assertNotNull(result);
        assertEquals(1, FACTORY_CALLS.size());
        assertEquals(2, FACTORY_CALLS.get(0).size());
        assertEquals(uri, FACTORY_CALLS.get(0).get(0));
        assertEquals(charset, FACTORY_CALLS.get(0).get(1));
    }

    /**
     * Test for {@link UriBuilder#create(URI)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testCreateUri()
    throws Throwable {
        final URI uri = Generators.randomValue(URI.class);
        assertThrows(NullPointerException.class, () -> UriBuilder.create((URI) null));
        final UriBuilder result = UriBuilder.create(uri);
        assertNotNull(result);
        assertEquals(1, FACTORY_CALLS.size());
        assertEquals(2, FACTORY_CALLS.get(0).size());
        assertEquals(uri, FACTORY_CALLS.get(0).get(0));
        assertEquals(StandardCharsets.UTF_8, FACTORY_CALLS.get(0).get(1));
    }

    /**
     * Test for {@link UriBuilder#create(URI, Charset)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testCreateUriCharset()
    throws Throwable {
        final URI uri = Generators.randomValue(URI.class);
        final Charset charset = Generators.randomValue(Charset.class);
        assertThrows(NullPointerException.class, () -> UriBuilder.create((URI) null, (Charset) null));
        assertThrows(NullPointerException.class, () -> UriBuilder.create(uri, (Charset) null));
        assertThrows(NullPointerException.class, () -> UriBuilder.create((URI) null, charset));
        final UriBuilder result = UriBuilder.create(uri, charset);
        assertNotNull(result);
        assertEquals(1, FACTORY_CALLS.size());
        assertEquals(2, FACTORY_CALLS.get(0).size());
        assertEquals(uri, FACTORY_CALLS.get(0).get(0));
        assertEquals(charset, FACTORY_CALLS.get(0).get(1));
    }

    /**
     * Test for {@link UriBuilder#isPathEmpty()}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(strings = { "null", "", "non/empty" })
    void testIsPathEmpty(
            final @NotNull String path)
    throws Throwable {
        final UriBuilder builder = spy(UriBuilder.class);
        given(builder.getPath()).willReturn("null".equals(path) ? null : path);
        assertEquals(!"non/empty".equals(path), builder.isPathEmpty());
        then(builder).should().getPath();
    }

    /**
     * Test for {@link UriBuilder#setPath(String...)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(ints = { -1, 0, 1, 10 })
    void testSetPathVarargs(
            final int count)
    throws Throwable {
        final UriBuilder builder = spy(UriBuilder.class);
        if (count < 0) {
            assertThrows(NullPointerException.class, () -> builder.setPath((String[]) null));
        } else {
            final String[] parts = new String[count];
            for (int i = 0; i < count; i++) {
                parts[i] = Generators.randomValue(String.class);
            }
            builder.setPath(parts);
            then(builder).should().setPath(eq(Arrays.asList(parts)));
        }
    }

    /**
     * Test for {@link UriBuilder#appendPath(String...)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(ints = { -1, 0, 1, 10 })
    void testAppendPathVarargs(
            final int count)
    throws Throwable {
        final UriBuilder builder = spy(UriBuilder.class);
        if (count < 0) {
            assertThrows(NullPointerException.class, () -> builder.appendPath((String[]) null));
        } else {
            final String[] parts = new String[count];
            for (int i = 0; i < count; i++) {
                parts[i] = Generators.randomValue(String.class);
            }
            builder.appendPath(parts);
            then(builder).should().appendPath(eq(Arrays.asList(parts)));
        }
    }

    /**
     * Test for {@link UriBuilder#setParameters(Pair...)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(ints = { -1, 0, 1, 10 })
    void testSetParametersVarargs(
            final int count)
    throws Throwable {
        final UriBuilder builder = spy(UriBuilder.class);
        if (count < 0) {
            assertThrows(NullPointerException.class, () -> builder.setParameters((Pair<String, String>[]) null));
        } else {
            @SuppressWarnings("unchecked")
            final Pair<String, String>[] params = new Pair[count];
            for (int i = 0; i < count; i++) {
                params[i] = randomPair();
            }
            builder.setParameters(params);
            then(builder).should().setParameters(eq(Arrays.asList(params)));
        }
    }

    /**
     * Test for {@link UriBuilder#setParameter(String, String...)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(ints = { -1, 0, 1, 10 })
    void testSetParameterVarargs(
            final int count)
    throws Throwable {
        final UriBuilder builder = spy(UriBuilder.class);
        if (count < 0) {
            assertThrows(NullPointerException.class, () -> builder.setParameter(
                    (String) null,
                    (String[]) null));
            assertThrows(NullPointerException.class, () -> builder.setParameter(
                    "non null",
                    (String[]) null));
            assertThrows(NullPointerException.class, () -> builder.setParameter(
                    (String) null,
                    new String[0]));
        } else {
            final String name = Generators.randomValue(String.class);
            final String[] values = new String[count];
            for (int i = 0; i < count; i++) {
                values[i] = Generators.randomValue(String.class);
            }
            builder.setParameter(name, values);
            then(builder).should().setParameter(eq(name), eq(Arrays.asList(values)));
        }
    }

    /**
     * Test for {@link UriBuilder#addParameters(Pair...)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(ints = { -1, 0, 1, 10 })
    void testAddParametersVarargs(
            final int count)
    throws Throwable {
        final UriBuilder builder = spy(UriBuilder.class);
        if (count < 0) {
            assertThrows(NullPointerException.class, () -> builder.addParameters((Pair<String, String>[]) null));
        } else {
            @SuppressWarnings("unchecked")
            final Pair<String, String>[] params = new Pair[count];
            for (int i = 0; i < count; i++) {
                params[i] = randomPair();
            }
            builder.addParameters(params);
            then(builder).should().addParameters(eq(Arrays.asList(params)));
        }
    }

    /**
     * Test for {@link UriBuilder#addParameter(String, String...)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(ints = { -1, 0, 1, 10 })
    void testAddParameterVarargs(
            final int count)
    throws Throwable {
        final UriBuilder builder = spy(UriBuilder.class);
        if (count < 0) {
            assertThrows(NullPointerException.class, () -> builder.addParameter(
                    (String) null,
                    (String[]) null));
            assertThrows(NullPointerException.class, () -> builder.addParameter(
                    "non null",
                    (String[]) null));
            assertThrows(NullPointerException.class, () -> builder.addParameter(
                    (String) null,
                    new String[0]));
        } else {
            final String name = Generators.randomValue(String.class);
            final String[] values = new String[count];
            for (int i = 0; i < count; i++) {
                values[i] = Generators.randomValue(String.class);
            }
            builder.addParameter(name, values);
            then(builder).should().addParameter(eq(name), eq(Arrays.asList(values)));
        }
    }

    /**
     * Creates a random string pair.
     * 
     * @return The generated pair.
     */
    private @NotNull Pair<String, String> randomPair() {
        return Pair.of(
                Generators.randomValue(String.class),
                Generators.randomValue(String.class));
    }

    /**
     * Mock implementation of {@code UriBuilder.Factory} that records
     * received calls.
     */
    public static class MockUriBuilderFactory
    implements UriBuilder.Factory {

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull UriBuilder create() {
            FACTORY_CALLS.add(Collections.emptyList());
            return mock(UriBuilder.class);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull UriBuilder create(
                final @NotNull String uri,
                final @NotNull Charset charset) {
            FACTORY_CALLS.add(Arrays.asList(uri, charset));
            return mock(UriBuilder.class);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull UriBuilder create(
                final @NotNull URI uri,
                final @NotNull Charset charset) {
            FACTORY_CALLS.add(Arrays.asList(uri, charset));
            return mock(UriBuilder.class);
        }
    }
}
