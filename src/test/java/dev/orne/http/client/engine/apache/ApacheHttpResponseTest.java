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

import org.apache.commons.lang3.RandomUtils;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpEntityContainer;
import org.apache.hc.core5.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.http.client.engine.HttpResponseBody;
import dev.orne.test.rnd.Generators;

/**
 * Unit tests for {@code ApacheHttpResponse}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 * @see ApacheHttpResponse
 */
@Tag("ut")
class ApacheHttpResponseTest {

    private @Mock HttpResponse delegate;
    private @Mock HttpResponseWithEntity entityDelegate;
    private AutoCloseable mocks;

    @BeforeEach public void openMocks() {
        this.mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach public void releaseMocks() throws Exception {
        this.mocks.close();
    }

    /**
     * Test for {@link ApacheHttpResponse#ApacheHttpResponse(HttpResponse)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testConstructor()
    throws Throwable {
        assertThrows(NullPointerException.class, () -> new ApacheHttpResponse(null));
        final ApacheHttpResponse response = new ApacheHttpResponse(delegate);
        assertEquals(delegate, response.getDelegate());
        then(delegate).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponse#getStatusCode()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetStatusCode()
    throws Throwable {
        final ApacheHttpResponse response = new ApacheHttpResponse(delegate);
        final int statusCode = RandomUtils.nextInt(100, 600);
        given(delegate.getCode()).willReturn(statusCode);
        assertEquals(statusCode, response.getStatusCode());
        then(delegate).should().getCode();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponse#getStatusReason()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetStatusReason()
    throws Throwable {
        final ApacheHttpResponse response = new ApacheHttpResponse(delegate);
        final String reason = Generators.randomValue(String.class);
        given(delegate.getReasonPhrase()).willReturn(reason);
        assertEquals(reason, response.getStatusReason());
        then(delegate).should().getReasonPhrase();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponse#getStatusReason()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testContainsHeader()
    throws Throwable {
        final ApacheHttpResponse response = new ApacheHttpResponse(delegate);
        assertThrows(NullPointerException.class, () -> response.containsHeader(null));
        final String name = Generators.randomValue(String.class);
        final boolean expected = RandomUtils.nextBoolean();
        given(delegate.containsHeader(name)).willReturn(expected);
        assertEquals(expected, response.containsHeader(name));
        then(delegate).should().containsHeader(name);
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponse#getHeader(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetHeader()
    throws Throwable {
        final ApacheHttpResponse response = new ApacheHttpResponse(delegate);
        assertThrows(NullPointerException.class, () -> response.getHeader(null));
        final String name = Generators.randomValue(String.class);
        final String value0 = Generators.randomValue(String.class);
        final Header header0 = mock(Header.class);
        given(header0.getName()).willReturn(name);
        given(header0.getValue()).willReturn(value0);
        final String value1 = Generators.randomValue(String.class);
        final Header header1 = mock(Header.class);
        given(header1.getName()).willReturn(name);
        given(header1.getValue()).willReturn(value1);
        final String value2 = Generators.randomValue(String.class);
        final Header header2 = mock(Header.class);
        given(header2.getName()).willReturn(name);
        given(header2.getValue()).willReturn(value2);
        final Header[] headers = { header0, header1, header2 };
        given(delegate.getHeaders(name)).willReturn(headers);
        assertArrayEquals(new String[] { value0, value1, value2 }, response.getHeader(name));
        then(delegate).should().getHeaders(name);
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponse#getFirstHeaderValue(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetFirstHeaderValue()
    throws Throwable {
        final ApacheHttpResponse response = new ApacheHttpResponse(delegate);
        assertThrows(NullPointerException.class, () -> response.getFirstHeaderValue(null));
        final String name = Generators.randomValue(String.class);
        final String value0 = Generators.randomValue(String.class);
        final Header header0 = mock(Header.class);
        given(header0.getName()).willReturn(name);
        given(header0.getValue()).willReturn(value0);
        given(delegate.getFirstHeader(name)).willReturn(header0);
        assertEquals(value0, response.getFirstHeaderValue(name));
        then(delegate).should().getFirstHeader(name);
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponse#getFirstHeaderValue(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetFirstHeaderValue_Missing()
    throws Throwable {
        final ApacheHttpResponse response = new ApacheHttpResponse(delegate);
        final String name = Generators.randomValue(String.class);
        given(delegate.getFirstHeader(name)).willReturn(null);
        assertNull(response.getFirstHeaderValue(name));
        then(delegate).should().getFirstHeader(name);
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponse#getLastHeaderValue()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetLastHeaderValue()
    throws Throwable {
        final ApacheHttpResponse response = new ApacheHttpResponse(delegate);
        assertThrows(NullPointerException.class, () -> response.getLastHeaderValue(null));
        final String name = Generators.randomValue(String.class);
        final String value2 = Generators.randomValue(String.class);
        final Header header2 = mock(Header.class);
        given(header2.getName()).willReturn(name);
        given(header2.getValue()).willReturn(value2);
        given(delegate.getLastHeader(name)).willReturn(header2);
        assertEquals(value2, response.getLastHeaderValue(name));
        then(delegate).should().getLastHeader(name);
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponse#getLastHeaderValue()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetLastHeaderValue_Missing()
    throws Throwable {
        final ApacheHttpResponse response = new ApacheHttpResponse(delegate);
        final String name = Generators.randomValue(String.class);
        given(delegate.getLastHeader(name)).willReturn(null);
        assertNull(response.getLastHeaderValue(name));
        then(delegate).should().getLastHeader(name);
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponse#getBody()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetBody()
    throws Throwable {
        final ApacheHttpResponse response = new ApacheHttpResponse(entityDelegate);
        final HttpEntity entity = mock(HttpEntity.class);
        given(entityDelegate.getEntity()).willReturn(entity);
        final HttpResponseBody result = response.getBody();
        assertNotNull(result);
        final ApacheHttpResponseBody apacheResult = assertInstanceOf(ApacheHttpResponseBody.class, result);
        assertSame(entity, apacheResult.getDelegate());
        then(entityDelegate).should().getEntity();
        then(entityDelegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponse#getBody()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetBody_NoEntity()
    throws Throwable {
        final ApacheHttpResponse response = new ApacheHttpResponse(entityDelegate);
        given(entityDelegate.getEntity()).willReturn(null);
        final HttpResponseBody result = response.getBody();
        assertNull(result);
        then(entityDelegate).should().getEntity();
        then(entityDelegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheHttpResponse#getBody()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetBody_ResponseWithoutBody()
    throws Throwable {
        final ApacheHttpResponse response = new ApacheHttpResponse(delegate);
        final HttpResponseBody result = response.getBody();
        assertNull(result);
        then(delegate).shouldHaveNoInteractions();
    }

    private interface HttpResponseWithEntity
    extends HttpResponse, HttpEntityContainer {}
}
