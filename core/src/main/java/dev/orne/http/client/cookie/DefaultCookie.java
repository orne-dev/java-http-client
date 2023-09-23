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

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Default implementation of {@code Cookie}.
 * <p>
 * Useful for cookie creation and restoration.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public class DefaultCookie
implements Cookie, Serializable {

    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The name of the cookie. */
    private final @NotNull String name;
    /** The value of the cookie. */
    private final @NotNull String value;
    /** The domain of the cookie. */
    private String domain;
    /** If the cookie must be send only to the domain it was received from. */
    private boolean hostOnly;
    /** The path of the cookie. */
    private String path;
    /** The cookie's creation instant. */
    private Instant creationTime;
    /** The cookie's last access instant. */
    private Instant lastAccessTime;
    /** The cookie's expiration instant. */
    private Instant expiryTime;
    /** If the cookie is persistent. */
    private boolean persistent;
    /** If the cookie must be send only through secure protocols. */
    private boolean secureOnly;
    /** If the cookie must be send only through HTTP protocols. */
    private boolean httpOnly;

    /**
     * Creates a new instance.
     * 
     * @param name
     * @param value
     */
    public DefaultCookie(
            final @NotNull String name,
            final @NotNull String value) {
        super();
        this.name = Validate.notNull(name, "The cookie name is required");
        this.value = Validate.notNull(value, "The cookie value is required");
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    public DefaultCookie(
            final @NotNull Cookie copy) {
        super();
        Validate.notNull(copy, "The instance to copy is required");
        this.name = Validate.notNull(copy.getName(), "The cookie name is required");
        this.value = Validate.notNull(copy.getValue(), "The cookie value is required");
        this.expiryTime = copy.getExpiryTime();
        this.domain = copy.getDomain();
        this.path = copy.getPath();
        this.creationTime = copy.getCreationTime();
        this.lastAccessTime = copy.getLastAccessTime();
        this.persistent = copy.isPersistent();
        this.hostOnly = copy.isHostOnly();
        this.secureOnly = copy.isSecureOnly();
        this.httpOnly = copy.isHttpOnly();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getValue() {
        return this.value;
    }

    /**
     * Sets the max age, in seconds, of the cookie.
     * 
     * @param expiryTime The cookie's expiration instant.
     */
    public void setMaxAge(
            final long maxAge) {
        this.expiryTime = Instant.now().plus(maxAge, ChronoUnit.SECONDS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDomain() {
        return this.domain;
    }

    /**
     * Sets the domain the cookie belongs to.
     * 
     * @param domain The domain of the cookie.
     */
    public void setDomain(
            final String domain) {
        this.domain = domain;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHostOnly() {
        return this.hostOnly;
    }

    /**
     * Sets if the cookie must be send only to exact host of the domain it
     * belongs to.
     * 
     * 
     * @param hostOnly If the cookie must be send only to the host it was
     * received from.
     */
    public void setHostOnly(
            final boolean hostOnly) {
        this.hostOnly = hostOnly;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath() {
        return this.path;
    }

    /**
     * Sets the path the cookie belongs to.
     * 
     * @param path The path of the cookie.
     */
    public void setPath(
            final String path) {
        this.path = path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant getCreationTime() {
        return this.creationTime;
    }

    /**
     * Sets the instant the cookie was created.
     * 
     * @param creationTime The cookie's creation instant.
     */
    public void setCreationTime(
            final Instant creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant getLastAccessTime() {
        return this.lastAccessTime;
    }

    /**
     * Returns the instant the cookie was accessed last time.
     * 
     * @param lastAccessTime The cookie's last access instant.
     */
    public void setLastAccessTime(
            final Instant lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant getExpiryTime() {
        return this.expiryTime;
    }

    /**
     * Sets the instant the cookie expires, if any.
     * 
     * @param expiryTime The cookie's expiration instant.
     */
    public void setExpiryTime(
            final Instant expiryTime) {
        this.expiryTime = expiryTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPersistent() {
        return this.persistent;
    }

    /**
     * Sets if the cookie is persistent.
     * 
     * @param persistent If the cookie is persistent.
     */
    public void setPersistent(
            final boolean persistent) {
        this.persistent = persistent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSecureOnly() {
        return this.secureOnly;
    }

    /**
     * Sets if the cookie must be send only through secure protocols.
     * 
     * @param secureOnly If the cookie must be send only through secure
     * protocols.
     */
    public void setSecureOnly(
            final boolean secureOnly) {
        this.secureOnly = secureOnly;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHttpOnly() {
        return this.httpOnly;
    }

    /**
     * Sets if the cookie must be send only through HTTP protocols.
     * 
     * @param httpOnly If the cookie must be send only through HTTP protocols.
     */
    public void setHttpOnly(
            final boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getClass())
                .append(this.name)
                .append(this.value)
                .append(this.domain)
                .append(this.hostOnly)
                .append(this.path)
                .append(this.creationTime)
                .append(this.lastAccessTime)
                .append(this.expiryTime)
                .append(this.persistent)
                .append(this.secureOnly)
                .append(this.httpOnly)
                .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(
            final Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        final DefaultCookie other = (DefaultCookie) obj;
        return new EqualsBuilder()
                .append(this.name, other.name)
                .append(this.value, other.value)
                .append(this.domain, other.domain)
                .append(this.hostOnly, other.hostOnly)
                .append(this.path, other.path)
                .append(this.creationTime, other.creationTime)
                .append(this.lastAccessTime, other.lastAccessTime)
                .append(this.expiryTime, other.expiryTime)
                .append(this.persistent, other.persistent)
                .append(this.secureOnly, other.secureOnly)
                .append(this.httpOnly, other.httpOnly)
                .isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
