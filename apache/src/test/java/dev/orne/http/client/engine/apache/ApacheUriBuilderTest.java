package dev.orne.http.client.engine.apache;

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

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.http.UriBuilder;
import dev.orne.test.rnd.Generators;
import dev.orne.test.rnd.params.GenerationParameters;

/**
 * Unit tests for {@code ApacheUriBuilder}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-09
 * @since 0.1
 * @see ApacheUriBuilder
 */
@Tag("ut")
class ApacheUriBuilderTest {

    private @Mock URIBuilder delegate;
    private AutoCloseable mocks;

    @BeforeEach public void openMocks() {
        this.mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach public void releaseMocks() throws Exception {
        this.mocks.close();
    }

    /**
     * Test for {@link UriBuilder#create()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSPI()
    throws Throwable {
        assertInstanceOf(ApacheUriBuilder.class, UriBuilder.create());
    }

    /**
     * Test for {@link ApacheUriBuilder#ApacheUriBuilder(URIBuilder)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testConstructor()
    throws Throwable {
        assertThrows(NullPointerException.class, () -> new ApacheUriBuilder(null));
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        assertEquals(delegate, builder.getDelegate());
        then(delegate).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#getScheme()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetScheme()
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        final String value = Generators.randomValue(String.class);
        given(delegate.getScheme()).willReturn(value);
        assertEquals(value, builder.getScheme());
        then(delegate).should().getScheme();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#setScheme(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSetScheme()
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        final String value = Generators.randomValue(String.class);
        builder.setScheme(value);
        then(delegate).should().setScheme(value);
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#getUserInfo()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetUserInfo()
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        final String value = Generators.randomValue(String.class);
        given(delegate.getUserInfo()).willReturn(value);
        assertEquals(value, builder.getUserInfo());
        then(delegate).should().getUserInfo();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#setUserInfo(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSetUserInfo()
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        final String value = Generators.randomValue(String.class);
        builder.setUserInfo(value);
        then(delegate).should().setUserInfo(value);
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#getHost()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetHost()
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        final String value = Generators.randomValue(String.class);
        given(delegate.getHost()).willReturn(value);
        assertEquals(value, builder.getHost());
        then(delegate).should().getHost();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#setHost(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSetHost()
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        final String value = Generators.randomValue(String.class);
        builder.setHost(value);
        then(delegate).should().setHost(value);
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#setHost(InetAddress)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSetHostInetAddress()
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        final InetAddress value = InetAddress.getLocalHost();
        builder.setHost(value);
        then(delegate).should().setHost(value);
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#getPort()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetPort()
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        final int value = Generators.randomValue(Integer.class);
        given(delegate.getPort()).willReturn(value);
        assertEquals(value, builder.getPort());
        then(delegate).should().getPort();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#setPort(int)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSetPort()
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        final int value = Generators.randomValue(Integer.class);
        builder.setPort(value);
        then(delegate).should().setPort(value);
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#isPathEmpty()}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void testIsPathEmpty(
            final boolean empty)
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        given(delegate.isPathEmpty()).willReturn(empty);
        assertEquals(empty, builder.isPathEmpty());
        then(delegate).should().isPathEmpty();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#getPath()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetPath()
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        final String value = Generators.randomValue(String.class);
        given(delegate.getPath()).willReturn(value);
        assertEquals(value, builder.getPath());
        then(delegate).should().getPath();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#setPath(java.util.Collection)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(ints = { -1, 0, 1, 10 })
    @SuppressWarnings("unchecked")
    void testSetPath(
            int size)
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        final List<String> value;
        if (size < 0) {
            value = null;
        } else {
            value = Generators.randomValue(
                    List.class,
                    GenerationParameters.forSimpleGenerics().withElementsType(String.class),
                    GenerationParameters.forSizes().withMinSize(size).withMaxSize(size));
        }
        builder.setPath(value);
        then(delegate).should().setPath(null);
        if (size > 0) {
            for (final String path : value) {
                then(delegate).should().appendPath(path);
            }
        }
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#appendPath(java.util.Collection)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(ints = { -1, 0, 1, 10 })
    @SuppressWarnings("unchecked")
    void testAppendPath(
            int size)
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        final List<String> value;
        if (size < 0) {
            value = null;
        } else {
            value = Generators.randomValue(
                    List.class,
                    GenerationParameters.forSimpleGenerics().withElementsType(String.class),
                    GenerationParameters.forSizes().withMinSize(size).withMaxSize(size));
        }
        builder.appendPath(value);
        if (size > 0) {
            for (final String path : value) {
                then(delegate).should().appendPath(path);
            }
        }
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#clearPath()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testClearPath()
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        builder.clearPath();
        then(delegate).should().setPath(null);
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#isQueryEmpty()}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void testIsQueryEmpty(
            final boolean empty)
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        given(delegate.isQueryEmpty()).willReturn(empty);
        assertEquals(empty, builder.isQueryEmpty());
        then(delegate).should().isQueryEmpty();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#getParameters()}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 10 })
    void testGetParameters(
            int size)
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        final List<NameValuePair> params = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            params.add(new BasicNameValuePair(
                    Generators.randomValue(String.class),
                    Generators.randomValue(String.class)));
        }
        given(delegate.getQueryParams()).willReturn(params);
        final List<Pair<String, String>> result = builder.getParameters();
        then(delegate).should().getQueryParams();
        then(delegate).shouldHaveNoMoreInteractions();
        for (int i = 0; i < size; i++) {
            assertPairEquals(params.get(i), result.get(i));
        }
    }

    /**
     * Test for {@link ApacheUriBuilder#getParameterValues(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 10 })
    void testGetParameterValues(
            int size)
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        assertThrows(NullPointerException.class, () -> builder.getParameterValues(null));
        then(delegate).shouldHaveNoInteractions();
        final String name = Generators.randomValue(String.class);
        final List<NameValuePair> params = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            params.add(new BasicNameValuePair(
                    RandomUtils.nextBoolean() ? name : "NotSame" + i,
                    Generators.randomValue(String.class)));
        }
        given(delegate.getQueryParams()).willReturn(params);
        final List<String> result = builder.getParameterValues(name);
        then(delegate).should().getQueryParams();
        then(delegate).shouldHaveNoMoreInteractions();
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (name.equals(params.get(i).getName())) {
                assertEquals(params.get(i).getValue(), result.get(j++));
            }
        }
        assertEquals(j, result.size());
    }

    /**
     * Test for {@link ApacheUriBuilder#getParameterValue(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void testGetParameterValue(
            boolean exists)
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        assertThrows(NullPointerException.class, () -> builder.getParameterValue(null));
        then(delegate).shouldHaveNoInteractions();
        final String name = Generators.randomValue(String.class);
        final String value = exists ? Generators.randomValue(String.class) : null;
        final NameValuePair pair = exists ? new BasicNameValuePair(name, value) : null;
        given(delegate.getFirstQueryParam(name)).willReturn(pair);
        final String result = builder.getParameterValue(name);
        then(delegate).should().getFirstQueryParam(name);
        then(delegate).shouldHaveNoMoreInteractions();
        assertEquals(value, result);
    }

    /**
     * Test for {@link ApacheUriBuilder#setParameters(java.util.Collection)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(ints = { -1, 0, 1, 10 })
    void testSetParameters(
            int size)
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        final List<Pair<String, String>> value;
        if (size < 0) {
            value = null;
        } else {
            value = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                value.add(Pair.of(
                        Generators.randomValue(String.class),
                        Generators.randomValue(String.class)));
            }
        }
        builder.setParameters(value);
        then(delegate).should().removeQuery();
        if (size > 0) {
            for (final Pair<String, String> pair : value) {
                then(delegate).should().addParameter(pair.getKey(), pair.getValue());
            }
        }
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#setParameter(String, java.util.Collection)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(ints = { -1, 0, 1, 10 })
    void testSetParameter(
            int size)
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        final String name = Generators.randomValue(String.class);
        final List<String> values;
        if (size < 0) {
            values = null;
        } else {
            values = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                values.add(Generators.randomValue(String.class));
            }
        }
        assertThrows(NullPointerException.class, () -> builder.setParameter(null, (Collection<String>) null));
        assertThrows(NullPointerException.class, () -> builder.setParameter(null, values));
        assertThrows(IllegalArgumentException.class, () -> builder.setParameter(name, null, "some", "value"));
        assertThrows(IllegalArgumentException.class, () -> builder.setParameter(name, "some", null, "value"));
        assertThrows(IllegalArgumentException.class, () -> builder.setParameter(name, "some", "value", null));
        then(delegate).shouldHaveNoInteractions();
        builder.setParameter(name, values);
        then(delegate).should().removeParameter(name);
        if (size > 0) {
            for (final String value : values) {
                then(delegate).should().addParameter(name, value);
            }
        }
        then(delegate).shouldHaveNoMoreInteractions();
        assertDoesNotThrow(() -> builder.setParameter("only-remove", (Collection<String>) null));
        then(delegate).should().removeParameter("only-remove");
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#addParameters(java.util.Collection)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(ints = { -1, 0, 1, 10 })
    void testAddParameters(
            int size)
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        final List<Pair<String, String>> value;
        if (size < 0) {
            value = null;
        } else {
            value = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                value.add(Pair.of(
                        Generators.randomValue(String.class),
                        Generators.randomValue(String.class)));
            }
        }
        builder.addParameters(value);
        if (size > 0) {
            for (final Pair<String, String> pair : value) {
                then(delegate).should().addParameter(pair.getKey(), pair.getValue());
            }
        }
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#addParameter(String, java.util.Collection)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(ints = { -1, 0, 1, 10 })
    void testAddParameter(
            int size)
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        final String name = Generators.randomValue(String.class);
        final List<String> values;
        if (size < 0) {
            values = null;
        } else {
            values = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                values.add(Generators.randomValue(String.class));
            }
        }
        assertThrows(NullPointerException.class, () -> builder.addParameter(null, (Collection<String>) null));
        assertThrows(NullPointerException.class, () -> builder.addParameter(null, values));
        assertThrows(IllegalArgumentException.class, () -> builder.addParameter(name, null, "some", "value"));
        assertThrows(IllegalArgumentException.class, () -> builder.addParameter(name, "some", null, "value"));
        assertThrows(IllegalArgumentException.class, () -> builder.addParameter(name, "some", "value", null));
        assertDoesNotThrow(() -> builder.addParameter(name, (Collection<String>) null));
        then(delegate).shouldHaveNoInteractions();
        builder.addParameter(name, values);
        if (size > 0) {
            for (final String value : values) {
                then(delegate).should().addParameter(name, value);
            }
        }
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#clearParameters()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testClearParameters()
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        builder.clearParameters();
        then(delegate).should().removeQuery();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#removeParameter(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testRemoveParameter()
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        final String name = Generators.randomValue(String.class);
        assertThrows(NullPointerException.class, () -> builder.removeParameter(null));
        builder.removeParameter(name);
        then(delegate).should().removeParameter(name);
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#getFragment()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetFragment()
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        final String value = Generators.randomValue(String.class);
        given(delegate.getFragment()).willReturn(value);
        assertEquals(value, builder.getFragment());
        then(delegate).should().getFragment();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#setFragment(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSetFragment()
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        final String value = Generators.randomValue(String.class);
        builder.setFragment(value);
        then(delegate).should().setFragment(value);
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder#build()}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void testBuild(
            final boolean fail)
    throws Throwable {
        final ApacheUriBuilder builder = new ApacheUriBuilder(delegate);
        if (fail) {
            final URISyntaxException exception = new URISyntaxException("input", "reason");
            given(delegate.build()).willThrow(exception);
            final URISyntaxException result = assertThrows(URISyntaxException.class, () -> builder.build());
            assertSame(exception, result);
        } else {
            final URI value = Generators.randomValue(URI.class);
            given(delegate.build()).willReturn(value);
            assertEquals(value, builder.build());
        }
        then(delegate).should().build();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheUriBuilder.Factory#create()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFactoryCreate()
    throws Throwable {
        final ApacheUriBuilder.Factory factory = new ApacheUriBuilder.Factory();
        final ApacheUriBuilder builder = factory.create();
        final URIBuilder delegate = builder.getDelegate();
        assertNotNull(delegate);
        assertEquals(URI.create(""), delegate.build());
    }

    /**
     * Test for {@link ApacheUriBuilder.Factory#create(String, Charset)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFactoryCreateStringCharset()
    throws Throwable {
        final ApacheUriBuilder.Factory factory = new ApacheUriBuilder.Factory();
        final URI uri = Generators.randomValue(URI.class);
        final Charset charset = Generators.randomValue(Charset.class);
        assertThrows(NullPointerException.class, () -> factory.create((String) null, null));
        assertThrows(NullPointerException.class, () -> factory.create((String) null, charset));
        assertThrows(NullPointerException.class, () -> factory.create(uri.toASCIIString(), null));
        final ApacheUriBuilder builder = factory.create(uri.toASCIIString(), charset);
        final URIBuilder delegate = builder.getDelegate();
        assertNotNull(delegate);
        assertEquals(uri.toASCIIString(), delegate.build().toASCIIString());
    }

    /**
     * Test for {@link ApacheUriBuilder.Factory#create(URI, Charset)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFactoryCreateUriCharset()
    throws Throwable {
        final ApacheUriBuilder.Factory factory = new ApacheUriBuilder.Factory();
        final URI uri = Generators.randomValue(URI.class);
        final Charset charset = Generators.randomValue(Charset.class);
        assertThrows(NullPointerException.class, () -> factory.create((URI) null, null));
        assertThrows(NullPointerException.class, () -> factory.create((URI) null, charset));
        assertThrows(NullPointerException.class, () -> factory.create(uri, null));
        final ApacheUriBuilder builder = factory.create(uri, charset);
        final URIBuilder delegate = builder.getDelegate();
        assertNotNull(delegate);
        assertEquals(uri, delegate.build());
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

    private void assertPairEquals(
            final @NotNull NameValuePair expected,
            final @NotNull Pair<String, String> pair) {
        assertEquals(expected.getName(), pair.getKey());
        assertEquals(expected.getValue(), pair.getValue());
    }
}
