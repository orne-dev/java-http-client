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

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.test.rnd.Generators;

/**
 * Unit tests for {@code Cookie}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 * @see Cookie
 */
@Tag("ut")
class CookieTest {

    /**
     * Test for {@link Cookie#isExpired()} and {@link Cookie#isExpired(Instant)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testIsExpired() {
        final Cookie cookie = mock(Cookie.class);
        given(cookie.getExpiryTime()).willReturn(
                null,
                null,
                Instant.now().minus(1, ChronoUnit.MINUTES));
        willCallRealMethod().given(cookie).isExpired();
        willCallRealMethod().given(cookie).isExpired(any());
        assertFalse(cookie.isExpired());
        assertFalse(cookie.isExpired(Instant.MAX));
        assertFalse(cookie.isExpired(Instant.MIN));
        assertFalse(cookie.isExpired(Instant.now().minus(2, ChronoUnit.MINUTES)));
        assertTrue(cookie.isExpired());
        assertTrue(cookie.isExpired(Instant.now()));
        assertTrue(cookie.isExpired(Instant.MAX));
    }

    /**
     * Test for {@link DefaultCookieGenerator#defaultValue()}.
     */
    @Test
    void testDefaultValueGeneration() {
        final Cookie cookie = Generators.defaultValue(Cookie.class);
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
                final Cookie cookie = Generators.randomValue(Cookie.class);
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
