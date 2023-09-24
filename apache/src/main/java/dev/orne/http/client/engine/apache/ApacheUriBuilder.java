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

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URIBuilder;

import dev.orne.http.UriBuilder;

/**
 * Implementation of {@code UriBuilder} based on
 * Apache HTTP Client 5.x {@code URIBuilder}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-09
 * @since 0.1
 * @see URIBuilder
 */
public class ApacheUriBuilder
implements UriBuilder {

    /** Error message for null single parameter name. */
    private static final String NAME_REQUIRED_ERR = "The parameter name is required";
    /** Error message for null batch parameter name. */
    private static final String NO_NULL_NAMES_ERR = "No null parameter names allowed";
    /** Error message for null parameter value. */
    private static final String NO_NULL_VALUES_ERR = "No null parameter values allowed";

    /** The delegate Apache URI builder.  */
    private final @NotNull URIBuilder delegate;

    /**
     * Creates a new instance.
     * 
     * @param delegate The delegate Apache URI builder.
     */
    public ApacheUriBuilder(
            final @NotNull URIBuilder delegate) {
        super();
        this.delegate = Validate.notNull(delegate);
    }

    /**
     * Returns the delegate Apache URI builder.
     * 
     * @return The delegate Apache URI builder.
     */
    protected @NotNull URIBuilder getDelegate() {
        return this.delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getScheme() {
        return this.delegate.getScheme();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull UriBuilder setScheme(
            final String scheme) {
        this.delegate.setScheme(scheme);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserInfo() {
        return this.delegate.getUserInfo();
    }

    @Override
    public @NotNull UriBuilder setUserInfo(
            final String userInfo) {
        this.delegate.setUserInfo(userInfo);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHost() {
        return this.delegate.getHost();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull UriBuilder setHost(
            final InetAddress ipAddress) {
        this.delegate.setHost(ipAddress);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull UriBuilder setHost(
            final String host) {
        this.delegate.setHost(host);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPort() {
        return this.delegate.getPort();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull UriBuilder setPort(
            final int port) {
        this.delegate.setPort(port);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPathEmpty() {
        return this.delegate.isPathEmpty();
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
    public @NotNull UriBuilder setPath(
            final Collection<@NotNull String> paths) {
        this.delegate.setPath(null);
        if (paths != null && !paths.isEmpty()) {
            appendPath(paths);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull UriBuilder appendPath(
            final Collection<@NotNull String> paths) {
        if (paths != null) {
            Validate.noNullElements(paths);
            for (final String path : paths) {
                this.delegate.appendPath(path);
            }
        }
        return this;
    }

    @Override
    public @NotNull UriBuilder clearPath() {
        this.delegate.setPath(null);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isQueryEmpty() {
        return this.delegate.isQueryEmpty();
    }

    /**
     * {@inheritDoc}
     */
   @Override
    public @NotNull List<@NotNull Pair<@NotNull String, @NotNull String>> getParameters() {
        return this.delegate.getQueryParams().stream()
                .map(nv -> Pair.of(nv.getName(), nv.getValue()))
                .collect(Collectors.toList());
    }

   /**
    * {@inheritDoc}
    */
    @Override
    public @NotNull List<@NotNull String> getParameterValues(
            final @NotNull String name) {
        Validate.notNull(name, NAME_REQUIRED_ERR);
        return this.delegate.getQueryParams().parallelStream()
                .filter(nv -> name.equals(nv.getName()))
                .map(nv -> nv.getValue())
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getParameterValue(
            final @NotNull String name) {
        Validate.notNull(name, NAME_REQUIRED_ERR);
        final NameValuePair nv = this.delegate.getFirstQueryParam(name);
        return nv == null ? null : nv.getValue();
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public @NotNull UriBuilder setParameters(
            final Collection<@NotNull Pair<@NotNull String, @NotNull String>> parameters) {
        this.delegate.removeQuery();
        addParameters(parameters);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull UriBuilder setParameter(
            final @NotNull String name,
            final Collection<@NotNull String> values) {
        Validate.notNull(name, NAME_REQUIRED_ERR);
        if (values != null) {
            Validate.noNullElements(values, NO_NULL_VALUES_ERR);
        }
        this.delegate.removeParameter(name);
        addParameter(name, values);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull UriBuilder addParameters(
            final Collection<@NotNull Pair<@NotNull String, @NotNull String>> parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            for (final Pair<String, String> pair : parameters) {
                this.delegate.addParameter(
                        Validate.notNull(pair.getKey(), NO_NULL_NAMES_ERR),
                        Validate.notNull(pair.getValue(), NO_NULL_VALUES_ERR));
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull UriBuilder addParameter(
            final @NotNull String name,
            final Collection<@NotNull String> values) {
        Validate.notNull(name, NAME_REQUIRED_ERR);
        if (values != null && !values.isEmpty()) {
            Validate.noNullElements(values, NO_NULL_VALUES_ERR);
            for (final String value : values) {
                this.delegate.addParameter(name, value);
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull UriBuilder clearParameters() {
        this.delegate.removeQuery();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull UriBuilder removeParameter(
            final @NotNull String name) {
        this.delegate.removeParameter(
                Validate.notNull(name, NAME_REQUIRED_ERR));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFragment() {
        return this.delegate.getFragment();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull UriBuilder setFragment(
            final String fragment) {
        this.delegate.setFragment(fragment);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull URI build()
    throws URISyntaxException {
        return this.delegate.build();
    }

    /**
     * Implementation of {@code UriBuilder.Factory} that creates instances of
     * {@code ApacheUriBuilder}.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-09
     * @since 0.1
     */
    public static class Factory
    implements UriBuilder.Factory {

        /**
         * Creates a new instance.
         */
        public Factory() {
            super();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull ApacheUriBuilder create() {
            return new ApacheUriBuilder(new URIBuilder());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull ApacheUriBuilder create(
                final @NotNull String uri,
                final @NotNull Charset charset)
        throws URISyntaxException {
            return new ApacheUriBuilder(new URIBuilder(
                    Validate.notNull(uri),
                    Validate.notNull(charset)));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull ApacheUriBuilder create(
                final @NotNull URI uri,
                final @NotNull Charset charset) {
            return new ApacheUriBuilder(new URIBuilder(
                    Validate.notNull(uri),
                    Validate.notNull(charset)));
        }
    }
}
