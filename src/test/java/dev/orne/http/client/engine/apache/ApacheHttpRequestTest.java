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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.commons.lang3.RandomUtils;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpEntityContainer;
import org.apache.hc.core5.http.HttpRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.http.ContentType;
import dev.orne.http.MediaTypes;
import dev.orne.http.client.HttpRequestBodyGenerationException;
import dev.orne.http.client.engine.HttpRequest.BodyProducer;
import dev.orne.test.rnd.Generators;

/**
 * Unit tests for {@code ApacheHttpRequest}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 * @see ApacheHttpRequest
 */
@Tag("ut")
class ApacheHttpRequestTest {

    private @Mock HttpRequest delegate;
    private @Mock HttpRequestWithEntity entityDelegate;
    private @Captor ArgumentCaptor<HttpEntity> entityCaptor;
    private AutoCloseable mocks;

    @BeforeEach public void openMocks() {
        this.mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach public void releaseMocks() throws Exception {
        this.mocks.close();
    }

    /**
     * Test for {@link ApacheHttpRequest#ApacheHttpRequest(HttpRequest)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testConstructor()
    throws Throwable {
        assertThrows(NullPointerException.class, () -> new ApacheHttpRequest(null));
        final ApacheHttpRequest request = new ApacheHttpRequest(delegate);
        assertEquals(delegate, request.getDelegate());
        then(delegate).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link ApacheHttpRequest#addHeader(String, String...)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testAddHeader()
    throws Throwable {
        final ApacheHttpRequest request = new ApacheHttpRequest(delegate);
        assertThrows(NullPointerException.class, () -> request.addHeader(null));
        final String name = Generators.randomValue(String.class);
        assertThrows(NullPointerException.class, () -> request.addHeader(name, (String[]) null));
        assertThrows(IllegalArgumentException.class, () -> request.addHeader(name, (String) null));
        final String value0 = Generators.randomValue(String.class);
        assertThrows(IllegalArgumentException.class, () -> request.addHeader(name, value0, null));
        final String value1 = Generators.randomValue(String.class);
        assertThrows(IllegalArgumentException.class, () -> request.addHeader(name, value0, null, value1));
        request.addHeader(name, value0, value1);
        then(delegate).should().addHeader(name, value0);
        then(delegate).should().addHeader(name, value1);
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheHttpRequest#setBody(dev.orne.http.ContentType, String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSetBody_String()
    throws Throwable {
        final ApacheHttpRequest request = new ApacheHttpRequest(entityDelegate);
        final dev.orne.http.ContentType contentType =
                ContentType.of(MediaTypes.Text.PLAIN,
                Generators.randomValue(Charset.class));
        final String body = Generators.randomValue(String.class);
        assertThrows(NullPointerException.class, () -> request.setBody(null, (String) null));
        assertThrows(NullPointerException.class, () -> request.setBody(null, body));
        assertThrows(NullPointerException.class, () -> request.setBody(contentType, (String) null));
        request.setBody(contentType, body);
        then(entityDelegate).should().setEntity(
                entityCaptor.capture());
        then(entityDelegate).shouldHaveNoMoreInteractions();
        final HttpEntity entity = entityCaptor.getValue();
        assertEquals(contentType.getHeader(), entity.getContentType());
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            entity.writeTo(out);
            assertArrayEquals(body.getBytes(contentType.getCharset()), out.toByteArray());
        }
    }

    /**
     * Test for {@link ApacheHttpRequest#setBody(dev.orne.http.ContentType, String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSetBody_String_InvalidMethod()
    throws Throwable {
        final ApacheHttpRequest request = new ApacheHttpRequest(delegate);
        final dev.orne.http.ContentType contentType =
                ContentType.of(MediaTypes.Text.PLAIN,
                Generators.randomValue(Charset.class));
        final String body = Generators.randomValue(String.class);
        assertThrows(IllegalStateException.class, () -> request.setBody(contentType, body));
        then(delegate).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link ApacheHttpRequest#setBody(dev.orne.http.ContentType, byte[])}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSetBody_ByteArray()
    throws Throwable {
        final ApacheHttpRequest request = new ApacheHttpRequest(entityDelegate);
        final dev.orne.http.ContentType contentType = Generators.randomValue(
                dev.orne.http.ContentType.class);
        final byte[] body = RandomUtils.nextBytes(50);
        assertThrows(NullPointerException.class, () -> request.setBody(null, (byte[]) null));
        assertThrows(NullPointerException.class, () -> request.setBody(null, body));
        assertThrows(NullPointerException.class, () -> request.setBody(contentType, (byte[]) null));
        request.setBody(contentType, body);
        then(entityDelegate).should().setEntity(
                entityCaptor.capture());
        then(entityDelegate).shouldHaveNoMoreInteractions();
        final HttpEntity entity = entityCaptor.getValue();
        assertEquals(contentType.getHeader(), entity.getContentType());
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            entity.writeTo(out);
            assertArrayEquals(body, out.toByteArray());
        }
    }

    /**
     * Test for {@link ApacheHttpRequest#setBody(dev.orne.http.ContentType, byte[])}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSetBody_ByteArray_InvalidMethod()
    throws Throwable {
        final ApacheHttpRequest request = new ApacheHttpRequest(delegate);
        final dev.orne.http.ContentType contentType = Generators.randomValue(
                dev.orne.http.ContentType.class);
        final byte[] body = RandomUtils.nextBytes(50);
        assertThrows(IllegalStateException.class, () -> request.setBody(contentType, body));
        then(delegate).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link ApacheHttpRequest#setBody(ContentType, BodyProducer)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSetBody_BodyProducer()
    throws Throwable {
        final ApacheHttpRequest request = new ApacheHttpRequest(entityDelegate);
        final dev.orne.http.ContentType contentType = Generators.randomValue(
                dev.orne.http.ContentType.class);
        final byte[] body = RandomUtils.nextBytes(50);
        final BodyProducer producer = output -> output.write(body);
        assertThrows(NullPointerException.class, () -> request.setBody(null, (BodyProducer) null));
        assertThrows(NullPointerException.class, () -> request.setBody(null, producer));
        assertThrows(NullPointerException.class, () -> request.setBody(contentType, (BodyProducer) null));
        request.setBody(contentType, producer);
        then(entityDelegate).should().setEntity(
                entityCaptor.capture());
        then(entityDelegate).shouldHaveNoMoreInteractions();
        final HttpEntity entity = entityCaptor.getValue();
        assertEquals(contentType.getHeader(), entity.getContentType());
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            entity.writeTo(out);
            assertArrayEquals(body, out.toByteArray());
        }
    }

    /**
     * Test for {@link ApacheHttpRequest#setBody(ContentType, BodyProducer)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSetBody_BodyProducer_InvalidMethod()
    throws Throwable {
        final ApacheHttpRequest request = new ApacheHttpRequest(delegate);
        final dev.orne.http.ContentType contentType = Generators.randomValue(
                dev.orne.http.ContentType.class);
        final BodyProducer producer = mock(BodyProducer.class);
        assertThrows(IllegalStateException.class, () -> request.setBody(contentType, producer));
        then(delegate).shouldHaveNoInteractions();
        then(producer).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link ApacheHttpRequest#setBody(ContentType, BodyProducer)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSetBody_BodyProducer_IOException()
    throws Throwable {
        final ApacheHttpRequest request = new ApacheHttpRequest(entityDelegate);
        final dev.orne.http.ContentType contentType = Generators.randomValue(
                dev.orne.http.ContentType.class);
        final IOException exception = new IOException();
        final BodyProducer producer = output -> { throw exception; };
        request.setBody(contentType, producer);
        then(entityDelegate).should().setEntity(
                entityCaptor.capture());
        then(entityDelegate).shouldHaveNoMoreInteractions();
        final HttpEntity entity = entityCaptor.getValue();
        assertEquals(contentType.getHeader(), entity.getContentType());
        final OutputStream stream = mock(OutputStream.class);
        final IOException result = assertThrows(IOException.class, () -> entity.writeTo(stream));
        assertSame(exception, result);
        then(stream).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link ApacheHttpRequest#setBody(ContentType, BodyProducer)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSetBody_BodyProducer_GenerationException()
    throws Throwable {
        final ApacheHttpRequest request = new ApacheHttpRequest(entityDelegate);
        final dev.orne.http.ContentType contentType = Generators.randomValue(
                dev.orne.http.ContentType.class);
        final HttpRequestBodyGenerationException exception =
                new HttpRequestBodyGenerationException();
        final BodyProducer producer = output -> { throw exception; };
        request.setBody(contentType, producer);
        then(entityDelegate).should().setEntity(
                entityCaptor.capture());
        then(entityDelegate).shouldHaveNoMoreInteractions();
        final HttpEntity entity = entityCaptor.getValue();
        assertEquals(contentType.getHeader(), entity.getContentType());
        final OutputStream stream = mock(OutputStream.class);
        final IOException result = assertThrows(IOException.class, () -> entity.writeTo(stream));
        assertSame(exception, result.getCause());
        then(stream).shouldHaveNoInteractions();
    }

    private interface HttpRequestWithEntity
    extends HttpRequest, HttpEntityContainer {}
}
