package dev.orne.http.client.cookie;

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

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.http.client.HttpClientException;

/**
 * Unit tests for {@code CookieStore}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 * @see CookieStore
 */
@Tag("ut")
class CookieStoreTest {

    /**
     * Test for {@link CookieStore#getCookie(String)}.
     */
    @Test
    void testGetCookie()
    throws HttpClientException {
        final List<Cookie> cookies = Arrays.asList(
                new DefaultCookie("existant1", "value1"),
                new DefaultCookie("existant2", "value2"),
                new DefaultCookie("existant2", "value3"));
        final CookieStore cookie = mock(CookieStore.class);
        given(cookie.getCookies()).willReturn(cookies);
        given(cookie.getCookie(any())).willCallRealMethod();
        assertNotNull(cookie.getCookie("existant2"));
        assertSame(cookies.get(1), cookie.getCookie("existant2"));
        assertNull(cookie.getCookie("missing"));
    }

    /**
     * Test for {@link CookieStore#getCookie(String)}.
     */
    @Test
    void testGetCookie_Error()
    throws HttpClientException {
        final CookieStore cookie = mock(CookieStore.class);
        final HttpClientException exception = new HttpClientException();
        given(cookie.getCookies()).willThrow(exception);
        given(cookie.getCookie(any())).willCallRealMethod();
        assertThrows(NullPointerException.class, () -> cookie.getCookie(null));
        final HttpClientException result = assertThrows(HttpClientException.class, () -> cookie.getCookie("somename"));
        assertSame(exception, result);
    }
}
