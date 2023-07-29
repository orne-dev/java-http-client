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

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

import org.apache.commons.lang3.RandomUtils;
import org.apache.hc.core5.http.HttpEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.http.ContentType;
import dev.orne.http.client.HttpResponseHandlingException;
import dev.orne.test.rnd.Generators;

/**
 * Unit tests for {@code ApacheHttpResponseBody}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 * @see ApacheHttpResponseBody
 */
@Tag("ut")
class ApacheHttpResponseBodyTest {

    private @Mock HttpEntity delegate;
    private AutoCloseable mocks;

    @BeforeEach public void openMocks() {
        this.mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach public void releaseMocks() throws Exception {
        this.mocks.close();
    }

    /**
     * Test for {@link ApacheHttpResponseBody#ApacheHttpResponseBody(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testConstructor()
    throws Throwable {
        assertThrows(NullPointerException.class, () -> new ApacheHttpResponseBody(null));
        final ApacheHttpResponseBody body = new ApacheHttpResponseBody(delegate);
        assertEquals(delegate, body.getDelegate());
        then(delegate).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponseBody#getContentType()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetContentType()
    throws Throwable {
        final ApacheHttpResponseBody body = new ApacheHttpResponseBody(delegate);
        final ContentType expected = Generators.randomValue(ContentType.class);
        given(delegate.getContentType()).willReturn(expected.getHeader());
        final ContentType result = body.getContentType();
        assertEquals(expected, result);
        then(delegate).should().getContentType();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponseBody#getContentType()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetContentType_NoHeader()
    throws Throwable {
        final ApacheHttpResponseBody body = new ApacheHttpResponseBody(delegate);
        given(delegate.getContentType()).willReturn(null);
        final ContentType result = body.getContentType();
        assertNull(result);
        then(delegate).should().getContentType();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponseBody#getContentLength()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetContentLength()
    throws Throwable {
        final ApacheHttpResponseBody body = new ApacheHttpResponseBody(delegate);
        final long expected = RandomUtils.nextLong();
        given(delegate.getContentLength()).willReturn(expected);
        final long result = body.getContentLength();
        assertEquals(expected, result);
        then(delegate).should().getContentLength();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponseBody#getContent()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetContent()
    throws Throwable {
        final ApacheHttpResponseBody body = new ApacheHttpResponseBody(delegate);
        final InputStream expected = mock(InputStream.class);
        given(delegate.getContent()).willReturn(expected);
        final InputStream result = body.getContent();
        assertSame(expected, result);
        then(delegate).should().getContent();
        then(delegate).shouldHaveNoMoreInteractions();
        then(expected).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponseBody#getContent()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetContent_Unsupported()
    throws Throwable {
        final ApacheHttpResponseBody body = new ApacheHttpResponseBody(delegate);
        final UnsupportedOperationException expected = new UnsupportedOperationException();
        given(delegate.getContent()).willThrow(expected);
        final HttpResponseHandlingException result = assertThrows(
                HttpResponseHandlingException.class,
                () -> body.getContent());
        assertSame(expected, result.getCause());
        then(delegate).should().getContent();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponseBody#getContent()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetContent_IOException()
    throws Throwable {
        final ApacheHttpResponseBody body = new ApacheHttpResponseBody(delegate);
        final IOException expected = new IOException();
        given(delegate.getContent()).willThrow(expected);
        final HttpResponseHandlingException result = assertThrows(
                HttpResponseHandlingException.class,
                () -> body.getContent());
        assertSame(expected, result.getCause());
        then(delegate).should().getContent();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponseBody#discard()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testDiscard()
    throws Throwable {
        final ApacheHttpResponseBody body = new ApacheHttpResponseBody(delegate);
        final InputStream content = mock(InputStream.class);
        given(delegate.isStreaming()).willReturn(true);
        given(delegate.getContent()).willReturn(content);
        body.discard();
        then(delegate).should().isStreaming();
        then(delegate).should().getContent();
        then(delegate).shouldHaveNoMoreInteractions();
        then(content).should().close();
        then(content).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponseBody#discard()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testDiscard_NoStream()
    throws Throwable {
        final ApacheHttpResponseBody body = new ApacheHttpResponseBody(delegate);
        given(delegate.isStreaming()).willReturn(false);
        body.discard();
        then(delegate).should().isStreaming();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponseBody#discard()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testDiscard_Unsupported()
    throws Throwable {
        final ApacheHttpResponseBody body = new ApacheHttpResponseBody(delegate);
        final UnsupportedOperationException expected = new UnsupportedOperationException();
        given(delegate.isStreaming()).willReturn(true);
        given(delegate.getContent()).willThrow(expected);
        final HttpResponseHandlingException result = assertThrows(
                HttpResponseHandlingException.class,
                () -> body.discard());
        assertSame(expected, result.getCause());
        then(delegate).should().isStreaming();
        then(delegate).should().getContent();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponseBody#discard()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testDiscard_IOException()
    throws Throwable {
        final ApacheHttpResponseBody body = new ApacheHttpResponseBody(delegate);
        final IOException expected = new IOException();
        given(delegate.isStreaming()).willReturn(true);
        given(delegate.getContent()).willThrow(expected);
        final HttpResponseHandlingException result = assertThrows(
                HttpResponseHandlingException.class,
                () -> body.discard());
        assertSame(expected, result.getCause());
        then(delegate).should().isStreaming();
        then(delegate).should().getContent();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponseBody#parseContentType(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParseContentType()
    throws Throwable {
        assertNull(ApacheHttpResponseBody.parseContentType(null));
        assertNull(ApacheHttpResponseBody.parseContentType(""));
        assertThrows(
                HttpResponseHandlingException.class,
                () -> ApacheHttpResponseBody.parseContentType("name=value; name2=value2; name3=value3"));
        assertThrows(
                HttpResponseHandlingException.class,
                () -> ApacheHttpResponseBody.parseContentType("name=value"));
        assertThrows(
                HttpResponseHandlingException.class,
                () -> ApacheHttpResponseBody.parseContentType("name=value,name2=value2"));
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            boolean withoutParameters = false;
            boolean withParameters = false;
            boolean withCharset = false;
            boolean withBoundary = false;
            while (!withoutParameters ||
                    !withParameters ||
                    !withCharset ||
                    !withBoundary) {
                final ContentType expected = Generators.randomValue(ContentType.class);
                final String header = expected.getHeader();
                if (expected.getParameters().isEmpty()) {
                    withoutParameters = true;
                } else {
                    withParameters = true;
                    withCharset = withCharset || expected.getCharset() != null;
                    withBoundary = withBoundary || expected.getBoundary() != null;
                }
                final ContentType result = ApacheHttpResponseBody.parseContentType(header);
                assertEquals(expected, result);
            }
        });
    }
}
