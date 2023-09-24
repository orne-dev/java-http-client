package dev.orne.http;

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
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.apiguardian.api.API;

/**
 * Interface for builders of URIs.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-09
 * @since 0.1
 */
public interface UriBuilder {

    /**
     * Creates a new empty instance.
     * 
     * @return The created URI builder.
     */
    static @NotNull UriBuilder create() {
        return FactoryLoader.getFactory().create();
    }

    /**
     * Creates a new instance from the string which must be a valid URI.
     *
     * @param uri A valid URI in string form.
     * @return The created URI builder.
     * @throws URISyntaxException If the input is not a valid URI.
     */
    static @NotNull UriBuilder create(
            @NotNull String uri)
    throws URISyntaxException {
        return create(uri, StandardCharsets.UTF_8);
    }

    /**
     * Creates a new instance from the string which must be a valid URI.
     *
     * @param uri A valid URI in string form.
     * @param charset The encoding of the encoded parts of the URI.
     * @return The created URI builder.
     * @throws URISyntaxException If the input is not a valid URI.
     */
    static @NotNull UriBuilder create(
            @NotNull String uri,
            @NotNull Charset charset)
    throws URISyntaxException {
        return FactoryLoader.getFactory().create(
                Validate.notNull(uri),
                Validate.notNull(charset));
    }

    /**
     * Creates a new instance from the provided URI.
     * 
     * @param uri A starting URI.
     * @return The created URI builder.
     */
    static @NotNull UriBuilder create(
            @NotNull URI uri) {
        return create(uri, StandardCharsets.UTF_8);
    }

    /**
     * Creates a new instance from the provided URI.
     * 
     * @param uri A starting URI.
     * @param charset The encoding of the encoded parts of the URI.
     * @return The created URI builder.
     */
    static @NotNull UriBuilder create(
            @NotNull URI uri,
            @NotNull Charset charset) {
        return FactoryLoader.getFactory().create(
                Validate.notNull(uri),
                Validate.notNull(charset));
    }

    /**
     * Returns the scheme of the URI.
     *
     * @return The scheme.
     */
    String getScheme();

    /**
     * Sets the scheme of the URI.
     * 
     * @param scheme The scheme.
     * @return This instance, for method chaining.
     */
    @NotNull UriBuilder setScheme(
            String scheme);

    /**
     * Returns the user info of the URI.
     *
     * @return The user info.
     */
    String getUserInfo();

    /**
     * Sets the user info of the URI.
     * The value is expected to be unescaped and may contain non ASCII characters.
     * 
     * @param userInfo The URI user info.
     * @return This instance, for method chaining.
     */
    @NotNull UriBuilder setUserInfo(
            String userInfo);

    /**
     * Returns the host of the URI.
     *
     * @return The host portion of the URI.
     */
    String getHost();

    /**
     * Sets the host of the URI with the specified IP address.
     * 
     * @param ipAddress The IP address.
     * @return This instance, for method chaining.
     */
    @NotNull UriBuilder setHost(
            InetAddress ipAddress);

    /**
     * Sets the host of the URI.
     * 
     * @param host The host.
     * @return This instance, for method chaining.
     */
    @NotNull UriBuilder setHost(
            String host);

    /**
     * Returns the port of the URI.
     *
     * @return The port. Returns {@code -1} for scheme default.
     */
    int getPort();

    /**
     * Set the port of the URI.
     * 
     * @param port The port. Use {@code -1} for scheme default.
     * @return This instance, for method chaining.
     */
    @NotNull UriBuilder setPort(
            int port);

    /**
     * Returns {@code true} if the path of the URI is empty.
     *
     * @return If the path is empty.
     */
    default boolean isPathEmpty() {
        final String path = getPath();
        return path == null || path.isEmpty();
    }

    /**
     * Returns the path of the URI.
     *
     * @return The path.
     */
    String getPath();

    /**
     * Sets the path of the URI.
     * <p>
     * If multiple paths are provided are joined with the path segment
     * separator.
     * <p>
     * If no path is provided removes the path of the URI.
     * 
     * @param path The path.
     * @return This instance, for method chaining.
     */
    default @NotNull UriBuilder setPath(
            @NotNull String... path) {
        return setPath(Arrays.asList(Validate.notNull(path)));
    }

    /**
     * Sets the path of the URI.
     * <p>
     * If multiple paths are provided are joined with the path segment
     * separator.
     * <p>
     * If no path is provided removes the path of the URI.
     * 
     * @param path The path.
     * @return This instance, for method chaining.
     */
    @NotNull UriBuilder setPath(
            @NotNull Collection<@NotNull String> path);

    /**
     * Appends the specified path to the current path of the URI.
     * <p>
     * If multiple paths are provided are joined with the path segment
     * separator.
     * 
     * @param path The path segments.
     * @return This instance, for method chaining.
     */
    default @NotNull UriBuilder appendPath(
            @NotNull String... path) {
        return appendPath(Arrays.asList(Validate.notNull(path)));
    }

    /**
     * Appends the specified path to the current path of the URI.
     * <p>
     * If multiple paths are provided are joined with the path segment
     * separator.
     * 
     * @param path The path segments.
     * @return This instance, for method chaining.
     */
    @NotNull UriBuilder appendPath(
            @NotNull Collection<@NotNull String> path);

    /**
     * Removes the path of the URI.
     * 
     * @return This instance, for method chaining.
     */
    @NotNull UriBuilder clearPath();

    /**
     * Returns {@code true} if the query of the URI is empty.
     *
     * @return If the query is empty.
     */
    boolean isQueryEmpty();

    /**
     * Returns the parameters of the URI.
     *
     * @return The parameters.
     */
    @NotNull List<@NotNull Pair<@NotNull String, @NotNull String>> getParameters();

    /**
     * Returns the values for the specified parameter name of the query
     * parameters of the URI.
     *
     * @param name The parameter name.
     * @return The parameter values.
     */
    @NotNull List<@NotNull String> getParameterValues(
            @NotNull String name);

    /**
     * Returns the value for the specified parameter name of the query
     * parameters of the URI.
     * <p>
     * If the parameter has multiple values returns the first one.
     *
     * @param name The parameter name.
     * @return The parameter value.
     */
    String getParameterValue(
            @NotNull String name);

    /**
     * Sets the specified query parameters of the URI, replacing existing ones.
     * <p>
     * If no parameters are provided removes all the query parameters of the URI.
     * 
     * @param parameters The query parameters.
     * @return This instance, for method chaining.
     */
    default @NotNull UriBuilder setParameters(
            @SuppressWarnings("unchecked")
            @NotNull Pair<@NotNull String, @NotNull String>... parameters) {
        return setParameters(Arrays.asList(Validate.notNull(parameters)));
    }

    /**
     * Sets the specified query parameters of the URI, replacing existing ones.
     * 
     * @param parameters The query parameters.
     * @return This instance, for method chaining.
     */
    @NotNull UriBuilder setParameters(
            @NotNull Collection<@NotNull Pair<@NotNull String, @NotNull String>> parameters);

    /**
     * Sets the values of the specified query parameter of the URI,
     * replacing existing ones.
     * <p>
     * If the no value is provided removes the parameter.
     * 
     * @param name The parameter name.
     * @param values The parameter values. 
     * @return This instance, for method chaining.
     */
    default @NotNull UriBuilder setParameter(
            @NotNull String name,
            @NotNull String... values) {
        return setParameter(
                Validate.notNull(name),
                Arrays.asList(Validate.notNull(values)));
    }

    /**
     * Sets the values of the specified query parameter of the URI,
     * replacing existing ones.
     * <p>
     * If the no value is provided removes the parameter.
     * 
     * @param name The parameter name.
     * @param values The parameter values. 
     * @return This instance, for method chaining.
     */
    @NotNull UriBuilder setParameter(
            @NotNull String name,
            @NotNull Collection<@NotNull String> values);

    /**
     * Adds the specified values to the query parameters of the URI.
     * 
     * @param parameters The query parameters.
     * @return This instance, for method chaining.
     */
    default @NotNull UriBuilder addParameters(
            @SuppressWarnings("unchecked")
            @NotNull Pair<@NotNull String, @NotNull String>... parameters) {
        return addParameters(Arrays.asList(Validate.notNull(parameters)));
    }

    /**
     * Adds the specified values to the query parameters of the URI.
     * 
     * @param parameters The query parameters.
     * @return This instance, for method chaining.
     */
    @NotNull UriBuilder addParameters(
            @NotNull Collection<@NotNull Pair<@NotNull String, @NotNull String>> parameters);

    /**
     * Adds the specified values to the query parameters of the URI,
     * in addition to existing ones.
     * 
     * @param name The parameter name.
     * @param values The parameter values. 
     * @return This instance, for method chaining.
     */
    default @NotNull UriBuilder addParameter(
            @NotNull String name,
            @NotNull String... values) {
        return addParameter(
                Validate.notNull(name),
                Arrays.asList(Validate.notNull(values)));
    }

    /**
     * Adds the specified values to query parameters of the URI,
     * in addition to existing ones.
     * 
     * @param name The parameter name.
     * @param values The parameter values. 
     * @return This instance, for method chaining.
     */
    @NotNull UriBuilder addParameter(
            @NotNull String name,
            @NotNull Collection<String> values);

    /**
     * Removes the parameter from the query parameters of the URI.
     * 
     * @param name The parameter name.
     * @return This instance, for method chaining.
     */
    @NotNull UriBuilder removeParameter(
            @NotNull String name);

    /**
     * Removes all the query parameters of the URI.
     * 
     * @return This instance, for method chaining.
     */
    @NotNull UriBuilder clearParameters();

    /**
     * Returns the fragment of the URI.
     *
     * @return The fragment.
     */
    String getFragment();

    /**
     * Sets the fragment of the URI.
     * 
     * @param fragment The fragment.
     * @return This instance, for method chaining.
     */
    @NotNull UriBuilder setFragment(
            String fragment);

    /**
     * Builds a new {@code URI} with this instance's current values.
     * 
     * @return The created URI.
     * @throws URISyntaxException If the URI cannot be build.
     */
    @NotNull URI build()
    throws URISyntaxException;

    /**
     * Interface for {@code UriBuilder} factories.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-09
     * @since 0.1
     */
    interface Factory {

        /**
         * Creates a new empty instance.
         * 
         * @return The created URI builder.
         */
        @NotNull UriBuilder create();

        /**
         * Creates a new instance from the string which must be a valid URI.
         *
         * @param uri A valid URI in string form.
         * @param charset The encoding of the encoded parts of the URI.
         * @return The created URI builder.
         * @throws URISyntaxException If the input is not a valid URI.
         */
        @NotNull UriBuilder create(
                @NotNull String uri,
                @NotNull Charset charset)
        throws URISyntaxException;

        /**
         * Creates a new instance from the provided URI.
         * 
         * @param uri A starting URI.
         * @param charset The encoding of the encoded parts of the URI.
         * @return The created URI builder.
         */
        @NotNull UriBuilder create(
                @NotNull URI uri,
                @NotNull Charset charset);
    }

    /**
     * Internal utility class that loads SPI services of {@code UriBuilder}
     * factories.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-09
     * @since 0.1
     */
    @API(status = API.Status.INTERNAL, since = "0.1")
    final class FactoryLoader {

        /** The first factory cache. */
        private static Factory factory;

        /**
         * Private constructor.
         */
        private FactoryLoader() {
            // Utility class
        }

        /**
         * Returns the first configured SPI {@code UriBuilder} factory.
         * 
         * @return The {@code UriBuilder} factory.
         * @throws IllegalStateException If no factory implementation has been
         * configured.
         */
        public static synchronized @NotNull Factory getFactory() {
            if (factory == null) {
                final Iterator<Factory> it = ServiceLoader.load(Factory.class).iterator();
                if (!it.hasNext()) {
                    throw new IllegalStateException(
                            "No implementations of UriBuilder are available."
                                    + " Please, add a implementation to the classpath.");
                }
                factory = it.next();
            }
            return factory;
        }
    }
}
