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

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.test.rnd.Generators;

/**
 * Unit tests for {@code DefaultCookie}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 * @see DefaultCookie
 */
@Tag("ut")
class DefaultCookieTest {

    /**
     * Test for {@link DefaultCookie#DefaultCookie(String, String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testConstructor() {
        final String name = DefaultCookieGenerator.randomCookieName();
        final String value = DefaultCookieGenerator.randomCookieValue();
        assertThrows(NullPointerException.class, () -> new DefaultCookie(null, null));
        assertThrows(NullPointerException.class, () -> new DefaultCookie(null, value));
        assertThrows(NullPointerException.class, () -> new DefaultCookie(name, null));
        final DefaultCookie cookie = new DefaultCookie(name, value);
        assertEquals(name, cookie.getName());
        assertEquals(value, cookie.getValue());
        assertNull(cookie.getDomain());
        assertFalse(cookie.isHostOnly());
        assertNull(cookie.getPath());
        assertNull(cookie.getCreationTime());
        assertNull(cookie.getLastAccessTime());
        assertNull(cookie.getExpiryTime());
        assertFalse(cookie.isPersistent());
        assertFalse(cookie.isSecureOnly());
        assertFalse(cookie.isHttpOnly());
    }

    /**
     * Test for {@link DefaultCookie#DefaultCookie(Cookie)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testCopyConstructor() {
        final Cookie copy = Generators.randomValue(Cookie.class);
        final DefaultCookie cookie = new DefaultCookie(copy);
        assertEquals(copy.getName(), cookie.getName());
        assertEquals(copy.getValue(), cookie.getValue());
        assertEquals(copy.getDomain(), cookie.getDomain());
        assertEquals(copy.isHostOnly(), cookie.isHostOnly());
        assertEquals(copy.getPath(), cookie.getPath());
        assertEquals(copy.getCreationTime(), cookie.getCreationTime());
        assertEquals(copy.getLastAccessTime(), cookie.getLastAccessTime());
        assertEquals(copy.getExpiryTime(), cookie.getExpiryTime());
        assertEquals(copy.isPersistent(), cookie.isPersistent());
        assertEquals(copy.isSecureOnly(), cookie.isSecureOnly());
        assertEquals(copy.isHttpOnly(), cookie.isHttpOnly());
    }

    /**
     * Test for {@link DefaultCookie#hashCode()}, {@link DefaultCookie#equals(Object)}
     * and {@link DefaultCookie#toString()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testHashCodeEqualToString() {
        final DefaultCookie cookie = new DefaultCookie(
                DefaultCookieGenerator.randomCookieName(),
                DefaultCookieGenerator.randomCookieValue());
        assertNotEquals(cookie, null);
        assertEquals(cookie, cookie);
        assertNotEquals(cookie, new Object());
        assertNotNull(cookie.toString());
        DefaultCookie other = new DefaultCookie(cookie);
        assertEquals(cookie.hashCode(), other.hashCode());
        assertEquals(cookie, other);
        assertEquals(cookie.toString(), other.toString());
        other = new DefaultCookie(
                cookie.getName() + "NotEqual",
                cookie.getValue());
        assertNotEquals(cookie, other);
        assertNotEquals(cookie.toString(), other.toString());
        other = new DefaultCookie(
                cookie.getName(),
                cookie.getValue() + "NotEqual");
        assertNotEquals(cookie, other);
        assertNotEquals(cookie.toString(), other.toString());
        other = new DefaultCookie(cookie);
        other.setDomain("example.org");
        assertNotEquals(cookie, other);
        assertNotEquals(cookie.toString(), other.toString());
        other = new DefaultCookie(cookie);
        other.setHostOnly(true);
        assertNotEquals(cookie, other);
        assertNotEquals(cookie.toString(), other.toString());
        other = new DefaultCookie(cookie);
        other.setPath("/some/path");
        assertNotEquals(cookie, other);
        assertNotEquals(cookie.toString(), other.toString());
        other = new DefaultCookie(cookie);
        other.setCreationTime(Generators.randomValue(Instant.class));
        assertNotEquals(cookie, other);
        assertNotEquals(cookie.toString(), other.toString());
        other = new DefaultCookie(cookie);
        other.setLastAccessTime(Generators.randomValue(Instant.class));
        assertNotEquals(cookie, other);
        assertNotEquals(cookie.toString(), other.toString());
        other = new DefaultCookie(cookie);
        other.setExpiryTime(Generators.randomValue(Instant.class));
        assertNotEquals(cookie, other);
        assertNotEquals(cookie.toString(), other.toString());
        other = new DefaultCookie(cookie);
        other.setPersistent(true);
        assertNotEquals(cookie, other);
        assertNotEquals(cookie.toString(), other.toString());
        other = new DefaultCookie(cookie);
        other.setSecureOnly(true);
        assertNotEquals(cookie, other);
        assertNotEquals(cookie.toString(), other.toString());
        other = new DefaultCookie(cookie);
        other.setHostOnly(true);
        assertNotEquals(cookie, other);
        assertNotEquals(cookie.toString(), other.toString());
    }

    /**
     * Test for {@link DefaultCookieGenerator#defaultValue()}.
     */
    @Test
    void testDefaultValueGeneration() {
        final DefaultCookie cookie = Generators.defaultValue(DefaultCookie.class);
        assertNotNull(cookie.getName());
        assertNotNull(cookie.getValue());
        assertNull(cookie.getDomain());
        assertFalse(cookie.isHostOnly());
        assertNull(cookie.getPath());
        assertNull(cookie.getCreationTime());
        assertNull(cookie.getLastAccessTime());
        assertNull(cookie.getExpiryTime());
        assertFalse(cookie.isPersistent());
        assertFalse(cookie.isSecureOnly());
        assertFalse(cookie.isHttpOnly());
    }

    /**
     * Test for {@link DefaultCookieGenerator#defaultValue()}.
     */
    @Test
    void testRandomValueGeneration() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            final HashSet<String> name = new HashSet<>();
            final HashSet<String> value = new HashSet<>();
            final HashSet<String> domain = new HashSet<>();
            final HashSet<Boolean> hostOnly = new HashSet<>();
            final HashSet<String> path = new HashSet<>();
            final HashSet<Instant> creation = new HashSet<>();
            final HashSet<Instant> access = new HashSet<>();
            final HashSet<Instant> expirity = new HashSet<>();
            final HashSet<Boolean> persistent = new HashSet<>();
            final HashSet<Boolean> secure = new HashSet<>();
            final HashSet<Boolean> http = new HashSet<>();
            while (name.size() < 10 ||
                    value.size() < 10 ||
                    domain.size() < 10 || !domain.contains(null) ||
                    hostOnly.size() < 2 ||
                    path.size() < 10 || !path.contains(null) ||
                    creation.size() < 10 || !creation.contains(null) ||
                    access.size() < 10 || !access.contains(null) ||
                    expirity.size() < 10 || !expirity.contains(null) ||
                    persistent.size() < 2 ||
                    secure.size() < 2 ||
                    http.size() < 2) {
                final DefaultCookie cookie = Generators.randomValue(DefaultCookie.class);
                name.add(cookie.getName());
                value.add(cookie.getValue());
                domain.add(cookie.getDomain());
                hostOnly.add(cookie.isHostOnly());
                path.add(cookie.getPath());
                creation.add(cookie.getCreationTime());
                access.add(cookie.getLastAccessTime());
                expirity.add(cookie.getExpiryTime());
                persistent.add(cookie.isPersistent());
                secure.add(cookie.isSecureOnly());
                http.add(cookie.isHttpOnly());
            }
        });
    }
}
