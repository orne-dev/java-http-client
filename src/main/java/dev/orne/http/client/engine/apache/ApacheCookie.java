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

import org.apache.commons.lang3.Validate;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;

import dev.orne.http.client.cookie.Cookie;

/**
 * Implementation of {@code Cookie} that delegates on Apache HTTP Client 5
 * cookies.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public class ApacheCookie
implements Cookie {

    /** The Apache HTTP Client cookie. */
    private final @NotNull org.apache.hc.client5.http.cookie.Cookie delegate;

    /**
     * Creates a new instance.
     * 
     * @param delegate The Apache HTTP Client cookie.
     */
    public ApacheCookie(
            final @NotNull org.apache.hc.client5.http.cookie.Cookie delegate) {
        super();
        this.delegate = Validate.notNull(delegate, "The delegated cookie is required");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getName() {
        return this.delegate.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getValue() {
        return this.delegate.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDomain() {
        return this.delegate.getDomain();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Cookies host only flag is not available with Apacha HTTP client engine.
     */
    @Override
    public boolean isHostOnly() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath() {
        return this.delegate.getPath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant getCreationTime() {
        if (this.delegate instanceof BasicClientCookie) {
            return ((BasicClientCookie) this.delegate).getCreationInstant();
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Cookies last access time is not available with Apacha HTTP client engine.
     */
    @Override
    public Instant getLastAccessTime() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant getExpiryTime() {
        return this.delegate.getExpiryInstant();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPersistent() {
        return this.delegate.isPersistent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSecureOnly() {
        return this.delegate.isSecure();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Cookies HTTP only flag is not available with Apacha HTTP client engine.
     */
    @Override
    public boolean isHttpOnly() {
        return false;
    }
}
