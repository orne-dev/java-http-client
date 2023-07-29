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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.http.client.cookie.DefaultCookie;
import dev.orne.test.rnd.Generators;
import dev.orne.test.rnd.params.GenerationParameters;

/**
 * Unit tests for {@code ApacheCookieStore}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 * @see ApacheCookieStore
 */
@Tag("ut")
class ApacheCookieStoreTest {

    private @Mock CookieStore delegate;
    private @Captor ArgumentCaptor<Cookie> apacheCookieCaptor;
    private AutoCloseable mocks;

    @BeforeEach public void openMocks() {
        this.mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach public void releaseMocks() throws Exception {
        this.mocks.close();
    }

    /**
     * Test for {@link ApacheCookieStore#ApacheCookieStore()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testConstructor()
    throws Throwable {
        final ApacheCookieStore store = new ApacheCookieStore();
        assertNotNull(store.getDelegate());
    }

    /**
     * Test for {@link ApacheCookieStore#ApacheCookieStore(CookieStore)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testStoreConstructor()
    throws Throwable {
        assertThrows(NullPointerException.class, () -> new ApacheCookie(null));
        final ApacheCookieStore store = new ApacheCookieStore(delegate);
        assertEquals(delegate, store.getDelegate());
        then(delegate).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link ApacheCookieStore#addCookie(dev.orne.http.client.cookie.Cookie)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testAddCookie()
    throws Throwable {
        final ApacheCookieStore store = new ApacheCookieStore(delegate);
        assertThrows(NullPointerException.class, () -> store.addCookie(null));
        final DefaultCookie cookie = Generators.randomValue(DefaultCookie.class);
        store.addCookie(cookie);
        then(delegate).should().addCookie(
                apacheCookieCaptor.capture());
        then(delegate).shouldHaveNoMoreInteractions();
        final Cookie apacheCookie = apacheCookieCaptor.getValue();
        assertNotNull(apacheCookie);
        BasicClientCookie baseApacheCookie = assertInstanceOf(BasicClientCookie.class, apacheCookie);
        assertEquals(cookie.getName(), baseApacheCookie.getName());
        assertEquals(cookie.getValue(), baseApacheCookie.getValue());
        if (cookie.getDomain() == null) {
            assertNull(baseApacheCookie.getDomain());
        } else {
            assertEquals(cookie.getDomain().toLowerCase(), baseApacheCookie.getDomain());
        }
        assertEquals(cookie.getPath(), baseApacheCookie.getPath());
        assertEquals(cookie.isSecureOnly(), baseApacheCookie.isSecure());
        assertEquals(cookie.getCreationTime(), baseApacheCookie.getCreationInstant());
        assertEquals(cookie.getExpiryTime(), baseApacheCookie.getExpiryInstant());
    }

    /**
     * Test for {@link ApacheCookieStore#getCookies()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetCookies()
    throws Throwable {
        final ApacheCookieStore store = new ApacheCookieStore(delegate);
        @SuppressWarnings("unchecked")
        final List<Cookie> apacheCookies = Generators.randomValue(
                List.class,
                GenerationParameters.forSimpleGenerics()
                    .withElementsType(Cookie.class),
                GenerationParameters.forSizes()
                    .withMinSize(0)
                    .withMaxSize(10));
        given(delegate.getCookies()).willReturn(apacheCookies);
        final List<dev.orne.http.client.cookie.Cookie> result = store.getCookies();
        assertEquals(apacheCookies.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            final ApacheCookie cookie = assertInstanceOf(ApacheCookie.class, result.get(i));
            assertSame(apacheCookies.get(i), cookie.getDelegate());
        }
        then(delegate).should().getCookies();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheCookieStore#getCookie(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetCookie()
    throws Throwable {
        final ApacheCookieStore store = new ApacheCookieStore(delegate);
        @SuppressWarnings("unchecked")
        final List<Cookie> apacheCookies = Generators.randomValue(
                List.class,
                GenerationParameters.forSimpleGenerics()
                    .withElementsType(Cookie.class),
                GenerationParameters.forSizes()
                    .withMinSize(2)
                    .withMaxSize(10));
        final Cookie expected = apacheCookies.get(RandomUtils.nextInt(0, apacheCookies.size()));
        given(delegate.getCookies()).willReturn(apacheCookies);
        final ApacheCookie cookie = assertInstanceOf(
                ApacheCookie.class,
                store.getCookie(expected.getName()));
        assertSame(expected, cookie.getDelegate());
        then(delegate).should().getCookies();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheCookieStore#getCookie(String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetCookie_Missing()
    throws Throwable {
        final ApacheCookieStore store = new ApacheCookieStore(delegate);
        @SuppressWarnings("unchecked")
        final List<Cookie> apacheCookies = new ArrayList<>(Generators.randomValue(
                List.class,
                GenerationParameters.forSimpleGenerics()
                    .withElementsType(Cookie.class)));
        given(delegate.getCookies()).willReturn(apacheCookies);
        final dev.orne.http.client.cookie.Cookie result = store.getCookie("missing");
        assertNull(result);
        then(delegate).should().getCookies();
        then(delegate).shouldHaveNoMoreInteractions();
    }
}
