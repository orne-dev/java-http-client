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

import java.time.Instant;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.RandomUtils;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apiguardian.api.API;

import dev.orne.http.client.cookie.DefaultCookieGenerator;
import dev.orne.test.rnd.AbstractTypedGenerator;
import dev.orne.test.rnd.Generators;
import dev.orne.test.rnd.generators.URIGenerator;

/**
 * Generator of random {@code BasicClientCookie} instances.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 */
@API(status = API.Status.INTERNAL, since = "0.1")
public class ApacheCookieGenerator
extends AbstractTypedGenerator<BasicClientCookie> {

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
    public @NotNull BasicClientCookie defaultValue() {
        return new BasicClientCookie(
                DefaultCookieGenerator.randomCookieName(),
                DefaultCookieGenerator.randomCookieValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull BasicClientCookie randomValue() {
        final BasicClientCookie result = new BasicClientCookie(
                DefaultCookieGenerator.randomCookieName(),
                DefaultCookieGenerator.randomCookieValue());
        if (RandomUtils.nextBoolean()) {
            result.setDomain(URIGenerator.randomHostName());
        }
        if (RandomUtils.nextBoolean()) {
            result.setPath(URIGenerator.randomAbsolutePath());
        }
        result.setCreationDate(Generators.nullableRandomValue(Instant.class));
        result.setExpiryDate(Generators.nullableRandomValue(Instant.class));
        result.setSecure(RandomUtils.nextBoolean());
        result.setHttpOnly(RandomUtils.nextBoolean());
        return result;
    }
}
