package dev.orne.http.client;

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

/**
 * Interface for HTTP cookies according to RFC 6265.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public interface Cookie {

    /**
     * Returns the name of the cookie.
     * 
     * @return The name of the cookie.
     */
    @NotNull String getName();

    /**
     * Returns the value of the cookie.
     * 
     * @return The value of the cookie.
     */
    @NotNull String getValue();

    /**
     * Returns the domain the cookie belongs to.
     * 
     * @return The domain of the cookie.
     */
    String getDomain();

    /**
     * Returns {@code true} if the cookie must be send only to exact host
     * of the domain it belongs to.
     * 
     * @return If the cookie must be send only to the host it was received
     * from.
     */
    boolean isHostOnly();

    /**
     * Returns the path the cookie belongs to.
     * 
     * @return The path of the cookie.
     */
    String getPath();

    /**
     * Returns the instant the cookie was created.
     * 
     * @return The cookie's creation instant.
     */
    Instant getCreationTime();

    /**
     * Returns the instant the cookie was accessed last time.
     * 
     * @return The cookie's last access instant.
     */
    Instant getLastAccessTime();

    /**
     * Returns the instant the cookie expires, if any.
     * 
     * @return The cookie's expiration instant.
     */
    Instant getExpiryTime();

    /**
     * Returns {@code true} if the cookie has expired.
     * 
     * @return If the cookie has expired.
     */
    default boolean isExpired() {
        return isExpired(Instant.now());
    }

    /**
     * Returns {@code true} if the cookie would be expired in the specified time.
     * 
     * @param instant The time to check the cookie expiration time against.
     * @return If the cookie would be expired in the specified time.
     */
    default boolean isExpired(
            final @NotNull Instant instant) {
        final Instant expiryTime = getExpiryTime();
        if (expiryTime == null) {
            return false;
        } else {
            return instant.isAfter(expiryTime);
        }
    }

    /**
     * Returns {@code true} if the cookie is persistent.
     * 
     * @return If the cookie is persistent.
     */
    boolean isPersistent();

    /**
     * Returns {@code true} if the cookie must be send only through secure
     * protocols.
     * 
     * @return If the cookie must be send only through secure protocols.
     */
    boolean isSecureOnly();

    /**
     * Returns {code true} if the cookie must be send only through HTTP
     * protocols.
     * 
     * @return If the cookie must be send only through HTTP protocols.
     */
    boolean isHttpOnly();
}
