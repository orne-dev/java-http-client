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

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;

/**
 * Basic abstract implementation of {@code HtppClientEngine}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public abstract class AbstractHttpClientEngine
implements HttpClientEngine {

    /** The HTTP client's cookie store. */
    private final @NotNull CookieStore cookieStore;

    /**
     * Creates a new instance with a clean {@code BasicCookieStore}.
     */
    protected AbstractHttpClientEngine() {
        super();
        this.cookieStore = new BasicCookieStore();
    }

    /**
     * Creates a new instance with the specified {@code CookieStore}.
     * 
     * @param cookieStore 
     */
    protected AbstractHttpClientEngine(
            final @NotNull CookieStore cookieStore) {
        super();
        this.cookieStore = Validate.notNull(cookieStore, "Cookie store is required");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CookieStore getCookieStore() {
        return this.cookieStore;
    }
}
