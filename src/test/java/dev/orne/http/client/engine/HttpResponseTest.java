package dev.orne.http.client.engine;

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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for {@code HttpResponse}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 * @see HttpResponse
 */
@Tag("ut")
class HttpResponseTest {

    /**
     * Test for {@link HttpResponse#containsHeader(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testContainsHeader(
            final boolean contains)
    throws Throwable {
        final String[] headerValue;
        if (contains) {
            headerValue = new String[] { "some", "header", "values" };
        } else {
            headerValue = new String[0];
        }
        final HttpResponse response = mock(HttpResponse.class);
        given(response.getHeader(any())).willReturn(headerValue);
        willCallRealMethod().given(response).containsHeader(anyString());
        final boolean result = response.containsHeader("some-header");
        assertEquals(contains, result);
        then(response).should().containsHeader("some-header");
        then(response).should().getHeader("some-header");
        then(response).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link HttpResponse#getHeaderValue(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    void testGetHeaderValue(
            final int valueCount)
    throws Throwable {
        final String[] headerValue = new String[valueCount];
        for (int i = 0; i < valueCount; i++) {
            headerValue[i] = "value" + i;
        }
        final HttpResponse response = mock(HttpResponse.class);
        given(response.getHeader(any())).willReturn(headerValue);
        willCallRealMethod().given(response).getHeaderValue(anyString());
        switch (valueCount) {
            case 0:
                assertNull(response.getHeaderValue("some-header"));
                break;
            case 1:
                assertEquals(headerValue[0], response.getHeaderValue("some-header"));
                break;
            default:
                assertThrows(IllegalStateException.class, () -> response.getHeaderValue("some-header"));
                break;
        }
        then(response).should().getHeaderValue("some-header");
        then(response).should().getHeader("some-header");
        then(response).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link HttpResponse#getFirstHeaderValue(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    void testGetFirstHeaderValue(
            final int valueCount)
    throws Throwable {
        final String[] headerValue = new String[valueCount];
        for (int i = 0; i < valueCount; i++) {
            headerValue[i] = "value" + i;
        }
        final HttpResponse response = mock(HttpResponse.class);
        given(response.getHeader(any())).willReturn(headerValue);
        willCallRealMethod().given(response).getFirstHeaderValue(anyString());
        switch (valueCount) {
            case 0:
                assertNull(response.getFirstHeaderValue("some-header"));
                break;
            default:
                assertEquals(headerValue[0], response.getFirstHeaderValue("some-header"));
                break;
        }
        then(response).should().getFirstHeaderValue("some-header");
        then(response).should().getHeader("some-header");
        then(response).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link HttpResponse#getLastHeaderValue(String)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    void testGetLastHeaderValue(
            final int valueCount)
    throws Throwable {
        final String[] headerValue = new String[valueCount];
        for (int i = 0; i < valueCount; i++) {
            headerValue[i] = "value" + i;
        }
        final HttpResponse response = mock(HttpResponse.class);
        given(response.getHeader(any())).willReturn(headerValue);
        willCallRealMethod().given(response).getLastHeaderValue(anyString());
        switch (valueCount) {
            case 0:
                assertNull(response.getLastHeaderValue("some-header"));
                break;
            default:
                assertEquals(headerValue[valueCount - 1], response.getLastHeaderValue("some-header"));
                break;
        }
        then(response).should().getLastHeaderValue("some-header");
        then(response).should().getHeader("some-header");
        then(response).shouldHaveNoMoreInteractions();
    }
}
