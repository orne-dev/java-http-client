package dev.orne.http.client.body;

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
import static org.mockito.Mockito.spy;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.http.ContentType;
import dev.orne.http.client.UnsupportedContentTypeException;
import dev.orne.test.rnd.Generators;

/**
 * Unit tests for {@code DelegatedHttpRequestBodyParser}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-08
 * @since 0.1
 * @see DelegatedHttpRequestBodyParser
 */
@Tag("ut")
class DelegatedHttpRequestBodyParserTest
extends HttpResponseBodyMediaTypeParserTest {

    private @Mock HttpResponseBodyMediaTypeParser<TestBean> delegate0;
    private @Mock HttpResponseBodyMediaTypeParser<ExtTestBean> delegate1;
    private @Mock HttpResponseBodyMediaTypeParser<ExtTestBean> delegate2;
    private @Mock InputStream input;
    private AutoCloseable mocks;

    @BeforeEach public void openMocks() {
        this.mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach public void releaseMocks() throws Exception {
        this.mocks.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull DelegatedHttpRequestBodyParser<?> createParser() {
        return spy(new DelegatedHttpRequestBodyParser<Object>(
                Generators.randomValue(ContentType.class),
                delegate0,
                delegate1));
    }

    /**
     * Test for {@link DelegatedHttpRequestBodyParser#DelegatedHttpRequestBodyParser(ContentType, HttpResponseBodyMediaTypeParser...)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testConstructor_Varargs()
    throws Throwable {
        final ContentType contentType = Generators.randomValue(ContentType.class);
        assertThrows(NullPointerException.class, () -> {
            new DelegatedHttpRequestBodyParser<>(
                    null, delegate0, delegate1);
        });
        assertThrows(NullPointerException.class, () -> {
            new DelegatedHttpRequestBodyParser<>(
                    contentType, (HttpResponseBodyMediaTypeParser<? extends TestBean>[]) null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new DelegatedHttpRequestBodyParser<>(
                    contentType, null, delegate0, delegate1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new DelegatedHttpRequestBodyParser<>(
                    contentType, delegate0, null, delegate1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new DelegatedHttpRequestBodyParser<>(
                    contentType, delegate0, delegate1, null);
        });
        final DelegatedHttpRequestBodyParser<TestBean> parser = new DelegatedHttpRequestBodyParser<>(
                contentType,
                delegate0,
                delegate1);
        assertSame(contentType, parser.getDefaultContentType());
        assertNotNull(parser.getParsers());
        final ArrayList<HttpResponseBodyMediaTypeParser<? extends TestBean>> delegates =
                new ArrayList<>(parser.getParsers());
        assertSame(delegate0, delegates.get(0));
        assertSame(delegate1, delegates.get(1));
        then(delegate0).shouldHaveNoInteractions();
        then(delegate1).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link DelegatedHttpRequestBodyParser#DelegatedHttpRequestBodyParser(ContentType, Collection)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testConstructor_Collection()
    throws Throwable {
        final ContentType contentType = Generators.randomValue(ContentType.class);
        assertThrows(NullPointerException.class, () -> {
            new DelegatedHttpRequestBodyParser<>(
                    null, Arrays.asList(delegate0, delegate1));
        });
        assertThrows(NullPointerException.class, () -> {
            new DelegatedHttpRequestBodyParser<>(
                    contentType, (Collection<HttpResponseBodyMediaTypeParser<? extends TestBean>>) null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new DelegatedHttpRequestBodyParser<>(
                    contentType, Arrays.asList(null, delegate0, delegate1));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new DelegatedHttpRequestBodyParser<>(
                    contentType, Arrays.asList(delegate0, null, delegate1));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new DelegatedHttpRequestBodyParser<>(
                    contentType, Arrays.asList(delegate0, delegate1, null));
        });
        final DelegatedHttpRequestBodyParser<TestBean> parser = new DelegatedHttpRequestBodyParser<>(
                contentType,
                Arrays.asList(delegate0, delegate1));
        assertSame(contentType, parser.getDefaultContentType());
        assertNotNull(parser.getParsers());
        final ArrayList<HttpResponseBodyMediaTypeParser<? extends TestBean>> delegates =
                new ArrayList<>(parser.getParsers());
        assertEquals(2, delegates.size());
        assertSame(delegate0, delegates.get(0));
        assertSame(delegate1, delegates.get(1));
        then(delegate0).shouldHaveNoInteractions();
        then(delegate1).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link DelegatedHttpRequestBodyParser#getDefaultContentType()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetDefaultContentType()
    throws Throwable {
        final ContentType contentType = Generators.randomValue(ContentType.class);
        final DelegatedHttpRequestBodyParser<TestBean> parser = new DelegatedHttpRequestBodyParser<>(
                contentType,
                delegate0,
                delegate1);
        assertSame(contentType, parser.getDefaultContentType());
    }

    /**
     * Test for {@link DelegatedHttpRequestBodyParser#getParsers()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetParsers()
    throws Throwable {
        final ContentType contentType = Generators.randomValue(ContentType.class);
        final ArrayList<HttpResponseBodyMediaTypeParser<? extends TestBean>> parsers =
                new ArrayList<>();
        parsers.add(delegate0);
        parsers.add(delegate1);
        final ArrayList<HttpResponseBodyMediaTypeParser<? extends TestBean>> parsersBackup =
                new ArrayList<>(parsers);
        final DelegatedHttpRequestBodyParser<TestBean> parser = new DelegatedHttpRequestBodyParser<>(
                contentType,
                parsers);
        final Collection<HttpResponseBodyMediaTypeParser<? extends TestBean>> result = parser.getParsers();
        assertNotSame(parsers, result);
        @SuppressWarnings("unchecked")
        final HttpResponseBodyMediaTypeParser<? extends TestBean> delegate3 = mock(HttpResponseBodyMediaTypeParser.class);
        assertThrows(UnsupportedOperationException.class, () -> result.add(delegate3));
        assertThrows(UnsupportedOperationException.class, () -> result.remove(delegate0));
        assertEquals(parsersBackup, new ArrayList<>(parser.getParsers()));
        parsers.add(delegate3);
        assertEquals(parsersBackup, new ArrayList<>(parser.getParsers()));
    }

    /**
     * Test for {@link DelegatedHttpRequestBodyParser#supportsMediaType(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSupportsMediaType()
    throws Throwable {
        final ContentType contentType = Generators.randomValue(ContentType.class);
        final String mediaType = "test";
        given(delegate0.supportsMediaType(mediaType)).willReturn(true);
        given(delegate1.supportsMediaType(mediaType)).willReturn(false);
        given(delegate2.supportsMediaType(mediaType)).willReturn(false);
        final DelegatedHttpRequestBodyParser<TestBean> parser = new DelegatedHttpRequestBodyParser<>(
                contentType, delegate0, delegate1, delegate2);
        assertEquals(true, parser.supportsMediaType(mediaType));
        then(delegate0).should(atLeastOnce()).supportsMediaType(mediaType);
        then(delegate0).shouldHaveNoMoreInteractions();
        then(delegate0).should(atLeast(0)).supportsMediaType(mediaType);
        then(delegate1).shouldHaveNoMoreInteractions();
        then(delegate0).should(atLeast(0)).supportsMediaType(mediaType);
        then(delegate2).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link DelegatedHttpRequestBodyParser#supportsMediaType(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSupportsMediaType_Last()
    throws Throwable {
        final ContentType contentType = Generators.randomValue(ContentType.class);
        final String mediaType = "test";
        given(delegate0.supportsMediaType(mediaType)).willReturn(false);
        given(delegate1.supportsMediaType(mediaType)).willReturn(false);
        given(delegate2.supportsMediaType(mediaType)).willReturn(true);
        final DelegatedHttpRequestBodyParser<TestBean> parser = new DelegatedHttpRequestBodyParser<>(
                contentType, delegate0, delegate1, delegate2);
        assertEquals(true, parser.supportsMediaType(mediaType));
        then(delegate0).should(atLeast(0)).supportsMediaType(mediaType);
        then(delegate0).shouldHaveNoMoreInteractions();
        then(delegate1).should(atLeast(0)).supportsMediaType(mediaType);
        then(delegate1).shouldHaveNoMoreInteractions();
        then(delegate2).should(atLeastOnce()).supportsMediaType(mediaType);
        then(delegate2).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link DelegatedHttpRequestBodyParser#supportsMediaType(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSupportsMediaType_Unsupported()
    throws Throwable {
        final ContentType contentType = Generators.randomValue(ContentType.class);
        final String mediaType = "test";
        given(delegate0.supportsMediaType(mediaType)).willReturn(false);
        given(delegate1.supportsMediaType(mediaType)).willReturn(false);
        given(delegate2.supportsMediaType(mediaType)).willReturn(false);
        final DelegatedHttpRequestBodyParser<TestBean> parser = new DelegatedHttpRequestBodyParser<>(
                contentType, delegate0, delegate1, delegate2);
        assertEquals(false, parser.supportsMediaType(mediaType));
        then(delegate0).should(atLeastOnce()).supportsMediaType(mediaType);
        then(delegate0).shouldHaveNoMoreInteractions();
        then(delegate1).should(atLeastOnce()).supportsMediaType(mediaType);
        then(delegate1).shouldHaveNoMoreInteractions();
        then(delegate2).should(atLeastOnce()).supportsMediaType(mediaType);
        then(delegate2).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link DelegatedHttpRequestBodyParser#getParser(ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetParser()
    throws Throwable {
        final ContentType defaultContentType = Generators.randomValue(ContentType.class);
        final String mediaType = "test";
        final ContentType contentType = ContentType.of(mediaType);
        given(delegate0.supportsMediaType(mediaType)).willReturn(false);
        given(delegate1.supportsMediaType(mediaType)).willReturn(true);
        given(delegate2.supportsMediaType(mediaType)).willReturn(false);
        final DelegatedHttpRequestBodyParser<TestBean> parser = new DelegatedHttpRequestBodyParser<>(
                defaultContentType, delegate0, delegate1, delegate2);
        assertSame(delegate1, parser.getParser(contentType));
        then(delegate0).should(atLeast(0)).supportsMediaType(mediaType);
        then(delegate0).shouldHaveNoMoreInteractions();
        then(delegate1).should(atLeastOnce()).supportsMediaType(mediaType);
        then(delegate1).shouldHaveNoMoreInteractions();
        then(delegate2).should(atLeast(0)).supportsMediaType(mediaType);
        then(delegate2).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link DelegatedHttpRequestBodyParser#getParser(ContentType)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetParser_Unsupported()
    throws Throwable {
        final ContentType defaultContentType = Generators.randomValue(ContentType.class);
        final String mediaType = "test";
        final ContentType contentType = ContentType.of(mediaType);
        given(delegate0.supportsMediaType(mediaType)).willReturn(false);
        given(delegate1.supportsMediaType(mediaType)).willReturn(false);
        given(delegate2.supportsMediaType(mediaType)).willReturn(false);
        final DelegatedHttpRequestBodyParser<TestBean> parser = new DelegatedHttpRequestBodyParser<>(
                defaultContentType, delegate0, delegate1, delegate2);
        assertThrows(UnsupportedContentTypeException.class, () -> {
            parser.getParser(contentType);
        });
        then(delegate0).should(atLeastOnce()).supportsMediaType(mediaType);
        then(delegate0).shouldHaveNoMoreInteractions();
        then(delegate1).should(atLeastOnce()).supportsMediaType(mediaType);
        then(delegate1).shouldHaveNoMoreInteractions();
        then(delegate2).should(atLeastOnce()).supportsMediaType(mediaType);
        then(delegate2).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link DelegatedHttpRequestBodyParser#parse(ContentType, InputStream, long)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParse()
    throws Throwable {
        final ContentType defaultContentType = Generators.randomValue(ContentType.class);
        final String mediaType = "test";
        final ContentType contentType = ContentType.of(mediaType);
        final DelegatedHttpRequestBodyParser<TestBean> parser =
                spy(new DelegatedHttpRequestBodyParser<>(defaultContentType));
        final long length = RandomUtils.nextLong();
        final TestBean expected = mock(TestBean.class);
        given(delegate0.parse(contentType, input, length)).willReturn(expected);
        willReturn(delegate0).given(parser).getParser(contentType);
        assertSame(expected, parser.parse(contentType, input, length));
        then(delegate0).should().parse(contentType, input, length);
        then(delegate0).shouldHaveNoMoreInteractions();
        then(input).shouldHaveNoInteractions();
    }

    /**
     * Mock bean for testing.
     */
    protected static class TestBean {
        // No extra methods
    }
    /**
     * Mock bean for testing.
     */
    protected static class ExtTestBean
    extends TestBean{
        // No extra methods
    }
}
