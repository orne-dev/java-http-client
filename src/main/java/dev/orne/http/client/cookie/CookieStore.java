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

import java.util.Collection;

import javax.validation.constraints.NotNull;

import dev.orne.http.client.HttpClientException;

/**
 * Interface for HTTP cookie storages.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public interface CookieStore {

    /**
     * Adds an Cookie, replacing any existing equivalent cookies.
     * If the given cookie has already expired it will not be added, but
     * existing values will still be removed.
     * 
     * @param The cookie to be added.
     * @throws If an error occurs storing the cookie.
     */
    void addCookie(
            @NotNull Cookie cookie)
    throws HttpClientException;

    /**
     * Returns all cookies contained in this store.
     * 
     * @return All cookies contained in this store.
     * @throws If an error occurs retrieving the cookies.
     */
    @NotNull Collection<Cookie> getCookies()
    throws HttpClientException;

    /**
     * Returns the stored cookie with the specified name, if any.
     * 
     * @return The stored cookie, or {@code null} if not found.
     * @throws If an error occurs retrieving the cookies.
     */
    default Cookie getCookie(
            @NotNull String name)
    throws HttpClientException {
        for (final Cookie cookie : getCookies()) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }
}
