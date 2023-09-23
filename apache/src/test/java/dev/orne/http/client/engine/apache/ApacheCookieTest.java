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

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;

import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.test.rnd.Generators;

/**
 * Unit tests for {@code ApacheCookie}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 * @see ApacheCookie
 */
@Tag("ut")
class ApacheCookieTest {

    private @Mock Cookie delegate;
    private AutoCloseable mocks;

    @BeforeEach public void openMocks() {
        this.mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach public void releaseMocks() throws Exception {
        this.mocks.close();
    }

    /**
     * Test for {@link ApacheCookie#ApacheCookie(Cookie)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testConstructor()
    throws Throwable {
        assertThrows(NullPointerException.class, () -> new ApacheCookie(null));
        final ApacheCookie cookie = new ApacheCookie(delegate);
        assertEquals(delegate, cookie.getDelegate());
        then(delegate).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link ApacheCookie#getName()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetName()
    throws Throwable {
        final ApacheCookie cookie = new ApacheCookie(delegate);
        final String name = Generators.randomValue(String.class);
        given(delegate.getName()).willReturn(name);
        assertEquals(name, cookie.getName());
        then(delegate).should().getName();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheCookie#getValue()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetValue()
    throws Throwable {
        final ApacheCookie cookie = new ApacheCookie(delegate);
        final String value = Generators.randomValue(String.class);
        given(delegate.getValue()).willReturn(value);
        assertEquals(value, cookie.getValue());
        then(delegate).should().getValue();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheCookie#getDomain()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetDomain()
    throws Throwable {
        final ApacheCookie cookie = new ApacheCookie(delegate);
        final String domain = Generators.randomValue(String.class);
        given(delegate.getDomain()).willReturn(domain);
        assertEquals(domain, cookie.getDomain());
        then(delegate).should().getDomain();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheCookie#isHostOnly()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testIsHostOnly()
    throws Throwable {
        final ApacheCookie cookie = new ApacheCookie(delegate);
        assertFalse(cookie.isHostOnly());
        then(delegate).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link ApacheCookie#getPath()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetPath()
    throws Throwable {
        final ApacheCookie cookie = new ApacheCookie(delegate);
        final String path = Generators.randomValue(String.class);
        given(delegate.getPath()).willReturn(path);
        assertEquals(path, cookie.getPath());
        then(delegate).should().getPath();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheCookie#getCreationTime()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetCreationTime()
    throws Throwable {
        ApacheCookie cookie = new ApacheCookie(delegate);
        assertNull(cookie.getCreationTime());
        then(delegate).shouldHaveNoInteractions();
        final BasicClientCookie basic = new BasicClientCookie("name", "value");
        final Instant time = Generators.randomValue(Instant.class);
        basic.setCreationDate(time);
        cookie = new ApacheCookie(basic);
        assertEquals(time, cookie.getCreationTime());
    }

    /**
     * Test for {@link ApacheCookie#getLastAccessTime()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetLastAccessTime()
    throws Throwable {
        final ApacheCookie cookie = new ApacheCookie(delegate);
        assertNull(cookie.getLastAccessTime());
        then(delegate).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link ApacheCookie#getExpiryTime()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetExpiryTime()
    throws Throwable {
        final ApacheCookie cookie = new ApacheCookie(delegate);
        final Instant time = Generators.randomValue(Instant.class);
        given(delegate.getExpiryInstant()).willReturn(time);
        assertEquals(time, cookie.getExpiryTime());
        then(delegate).should().getExpiryInstant();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheCookie#isPersistent()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testIsPersistent()
    throws Throwable {
        final ApacheCookie cookie = new ApacheCookie(delegate);
        final Boolean result = Generators.randomValue(Boolean.class);
        given(delegate.isPersistent()).willReturn(result);
        assertEquals(result, cookie.isPersistent());
        then(delegate).should().isPersistent();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheCookie#isSecureOnly()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testIsSecureOnly()
    throws Throwable {
        final ApacheCookie cookie = new ApacheCookie(delegate);
        final Boolean result = Generators.randomValue(Boolean.class);
        given(delegate.isSecure()).willReturn(result);
        assertEquals(result, cookie.isSecureOnly());
        then(delegate).should().isSecure();
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link ApacheCookie#isHttpOnly()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testIsHttpOnly()
    throws Throwable {
        final ApacheCookie cookie = new ApacheCookie(delegate);
        assertFalse(cookie.isHttpOnly());
        then(delegate).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link ApacheCookie#hashCode()}, {@link ApacheCookie#equals(Object)}
     * and {@link ApacheCookie#toString()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testHashCodeEqualToString() {
        final ApacheCookie cookie = new ApacheCookie(delegate);
        assertNotEquals(cookie, null);
        assertEquals(cookie, cookie);
        assertNotEquals(cookie, new Object());
        assertNotNull(cookie.toString());
        final Cookie otherDelegate = mock(Cookie.class);
        ApacheCookie other = new ApacheCookie(delegate);
        assertEquals(cookie.hashCode(), other.hashCode());
        assertEquals(cookie, other);
        assertEquals(cookie.toString(), other.toString());
        other = new ApacheCookie(otherDelegate);
        assertNotEquals(cookie, other);
        assertNotEquals(cookie.toString(), other.toString());
    }

    /**
     * Test for {@link ApacheCookieGenerator#defaultValue()}.
     */
    @Test
    void testDefaultValueGeneration() {
        final BasicClientCookie cookie = Generators.defaultValue(BasicClientCookie.class);
        assertNotNull(cookie.getName());
        assertNotNull(cookie.getValue());
        assertNull(cookie.getDomain());
        assertNull(cookie.getPath());
        assertNull(cookie.getCreationInstant());
        assertFalse(cookie.isPersistent());
        assertFalse(cookie.isSecure());
        assertFalse(cookie.isHttpOnly());
    }

    /**
     * Test for {@link ApacheCookieGenerator#defaultValue()}.
     */
    @Test
    void testRandomValueGeneration() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            final HashSet<String> name = new HashSet<>();
            final HashSet<String> value = new HashSet<>();
            final HashSet<String> domain = new HashSet<>();
            final HashSet<String> path = new HashSet<>();
            final HashSet<Instant> creation = new HashSet<>();
            final HashSet<Boolean> persistent = new HashSet<>();
            final HashSet<Boolean> secure = new HashSet<>();
            final HashSet<Boolean> http = new HashSet<>();
            while (name.size() < 10 ||
                    value.size() < 10 ||
                    domain.size() < 10 || !domain.contains(null) ||
                    path.size() < 10 || !path.contains(null) ||
                    creation.size() < 10 || !creation.contains(null) ||
                    persistent.size() < 2 ||
                    secure.size() < 2 ||
                    http.size() < 2) {
                final BasicClientCookie cookie = Generators.randomValue(BasicClientCookie.class);
                name.add(cookie.getName());
                value.add(cookie.getValue());
                domain.add(cookie.getDomain());
                path.add(cookie.getPath());
                creation.add(cookie.getCreationInstant());
                persistent.add(cookie.isPersistent());
                secure.add(cookie.isSecure());
                http.add(cookie.isHttpOnly());
            }
        });
    }

    /**
     * Test for {@link ApacheCookieGenerator#defaultValue()}.
     */
    @Test
    void testDefaultValueGeneration_Generic() {
        final Cookie cookie = Generators.defaultValue(Cookie.class);
        assertNotNull(cookie.getName());
        assertNotNull(cookie.getValue());
        assertNull(cookie.getDomain());
        assertNull(cookie.getPath());
        assertFalse(cookie.isPersistent());
        assertFalse(cookie.isSecure());
        assertFalse(cookie.isHttpOnly());
    }

    /**
     * Test for {@link ApacheCookieGenerator#defaultValue()}.
     */
    @Test
    void testRandomValueGeneration_Generic() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            final HashSet<String> name = new HashSet<>();
            final HashSet<String> value = new HashSet<>();
            final HashSet<String> domain = new HashSet<>();
            final HashSet<String> path = new HashSet<>();
            final HashSet<Boolean> persistent = new HashSet<>();
            final HashSet<Boolean> secure = new HashSet<>();
            final HashSet<Boolean> http = new HashSet<>();
            while (name.size() < 10 ||
                    value.size() < 10 ||
                    domain.size() < 10 || !domain.contains(null) ||
                    path.size() < 10 || !path.contains(null) ||
                    persistent.size() < 2 ||
                    secure.size() < 2 ||
                    http.size() < 2) {
                final Cookie cookie = Generators.randomValue(Cookie.class);
                name.add(cookie.getName());
                value.add(cookie.getValue());
                domain.add(cookie.getDomain());
                path.add(cookie.getPath());
                persistent.add(cookie.isPersistent());
                secure.add(cookie.isSecure());
                http.add(cookie.isHttpOnly());
            }
        });
    }
}
