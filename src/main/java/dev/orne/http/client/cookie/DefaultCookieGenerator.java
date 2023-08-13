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

import java.time.Instant;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apiguardian.api.API;

import dev.orne.test.rnd.AbstractTypedGenerator;
import dev.orne.test.rnd.Generators;
import dev.orne.test.rnd.generators.URIGenerator;

/**
 * Generator of random {@code DefaultCookie} instances.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 */
@API(status = API.Status.INTERNAL, since = "0.1")
public class DefaultCookieGenerator
extends AbstractTypedGenerator<DefaultCookie>  {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(
            final @NotNull Class<?> type) {
        return super.supports(type) || Cookie.class.equals(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull DefaultCookie defaultValue() {
        return new DefaultCookie(
                randomCookieName(),
                randomCookieValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull DefaultCookie randomValue() {
        final DefaultCookie result = new DefaultCookie(
                randomCookieName(),
                randomCookieValue());
        if (RandomUtils.nextBoolean()) {
            result.setDomain(URIGenerator.randomHostName());
        }
        result.setHostOnly(RandomUtils.nextBoolean());
        if (RandomUtils.nextBoolean()) {
            result.setPath(URIGenerator.randomAbsolutePath());
        }
        result.setCreationTime(Generators.nullableRandomValue(Instant.class));
        result.setLastAccessTime(Generators.nullableRandomValue(Instant.class));
        result.setExpiryTime(Generators.nullableRandomValue(Instant.class));
        result.setPersistent(RandomUtils.nextBoolean());
        result.setSecureOnly(RandomUtils.nextBoolean());
        result.setHttpOnly(RandomUtils.nextBoolean());
        return result;
    }

    /**
     * Generates a random cookie name.
     * 
     * @return The cookie name.
     */
    public static @NotNull String randomCookieName() {
        return RandomStringUtils.randomAscii(10).replaceAll(
                "[" + Cookie.CookieName.INVALID_CHARACTER + "]",
                "");
    }

    /**
     * Generates a random cookie value.
     * 
     * @return The cookie value.
     */
    public static @NotNull String randomCookieValue() {
        return RandomStringUtils.randomAscii(10).replaceAll(
                "[" + Cookie.CookieValue.INVALID_CHARACTER + "]",
                "");
    }
}
