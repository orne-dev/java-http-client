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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import dev.orne.http.client.Cookie;
import dev.orne.http.client.CookieStore;
import dev.orne.http.client.HttpClientException;

/**
 * Implementation of {@code CookieJar} that delegates on Apache HTTP Client's
 * cookie stores.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public class ApacheCookieStore
implements CookieStore {

    /** The Apache HTTP Client cookie store. */
    private final @NotNull org.apache.http.client.CookieStore delegate;

    /**
     * Creates a new instance with a clean {@code BasicCookieStore}.
     */
    public ApacheCookieStore() {
        this(new BasicCookieStore());
    }

    /**
     * Creates a new instance.
     * 
     * @param delegate The Apache HTTP Client cookie store.
     */
    public ApacheCookieStore(
            final @NotNull org.apache.http.client.CookieStore delegate) {
        super();
        this.delegate = Validate.notNull(delegate, "The delegated cookie store is required");
    }

    /**
     * Returns the delegated Apache HTTP Client cookie store.
     * 
     * @return The Apache HTTP Client cookie store.
     */
    public @NotNull org.apache.http.client.CookieStore getDelegate() {
        return this.delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addCookie(
            final @NotNull Cookie cookie) {
        Validate.notNull(cookie, "The cookie is required");
        final BasicClientCookie bean = new BasicClientCookie(
                Validate.notNull(cookie.getName(), "The cookie name is required"),
                Validate.notNull(cookie.getValue(), "The cookie value is required"));
        bean.setDomain(cookie.getDomain());
        bean.setPath(cookie.getPath());
        bean.setSecure(cookie.isSecureOnly());
        final Instant creationTime = cookie.getCreationTime();
        if (creationTime != null) {
            bean.setCreationDate(Date.from(creationTime));
        }
        final Instant expiryTime = cookie.getExpiryTime();
        if (expiryTime != null) {
            bean.setExpiryDate(Date.from(expiryTime));
        }
        this.delegate.addCookie(bean);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull List<Cookie> getCookies() {
        return this.delegate.getCookies().parallelStream()
                .map(ApacheCookie::new)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cookie getCookie(
            final @NotNull String name)
    throws HttpClientException {
        for (final org.apache.http.cookie.Cookie cookie : this.delegate.getCookies()) {
            if (name.equals(cookie.getName())) {
                return new ApacheCookie(cookie);
            }
        }
        return null;
    }
}
