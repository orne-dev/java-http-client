package dev.orne.http.client.body;

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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;

import dev.orne.http.client.HttpRequestBodyGenerationException;
import dev.orne.http.client.HttpResponseBodyParsingException;

/**
 * Utility methods for format and parse URL encoded collections of name-value
 * pairs.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 */
public final class UrlEncodedUtils {

    /** The separator between name-value pairs. */
    public static final String PAIR_SEPARATOR = "&";
    /** The separator between name and value. */
    public static final String VALUE_SEPARATOR = "=";

    /**
     * Private constructor.
     */
    private UrlEncodedUtils() {
        // Utility class
    }

    /**
     * URL encodes the specified collection of name-value pairs.
     * 
     * @param charset The encoding to use.
     * @param values The name-value pairs.
     * @return The URL encoded of the name-value pairs.
     * @throws HttpRequestBodyGenerationException If an error occurs formating
     * the pairs.
     */
    public static @NotNull String format(
            final @NotNull Charset charset,
            final @NotNull Map<@NotNull String, @NotNull String> values)
    throws HttpRequestBodyGenerationException {
        return format(charset, values.entrySet().stream()
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList()));
    }

    /**
     * URL encodes the specified collection of name-value pairs.
     * 
     * @param encoding The encoding to use.
     * @param values The name-value pairs.
     * @return The URL encoded of the name-value pairs.
     * @throws HttpRequestBodyGenerationException If an error occurs formating
     * the pairs.
     */
    public static @NotNull String format(
            final @NotNull String encoding,
            final @NotNull Map<@NotNull String, @NotNull String> values)
    throws HttpRequestBodyGenerationException {
        return format(encoding, values.entrySet().stream()
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList()));
    }

    /**
     * URL encodes the specified collection of name-value pairs.
     * 
     * @param charset The encoding to use.
     * @param values The name-value pairs.
     * @return The URL encoded of the name-value pairs.
     * @throws HttpRequestBodyGenerationException If an error occurs formating
     * the pairs.
     */
    @SafeVarargs
    public static @NotNull String format(
            final @NotNull Charset charset,
            final @NotNull Pair<@NotNull String, @NotNull String>... values)
    throws HttpRequestBodyGenerationException {
        return format(charset, Arrays.asList(values));
    }

    /**
     * URL encodes the specified collection of name-value pairs.
     * 
     * @param encoding The encoding to use.
     * @param values The name-value pairs.
     * @return The URL encoded of the name-value pairs.
     * @throws HttpRequestBodyGenerationException If an error occurs formating
     * the pairs.
     */
    @SafeVarargs
    public static @NotNull String format(
            final @NotNull String encoding,
            final @NotNull Pair<@NotNull String, @NotNull String>... values)
    throws HttpRequestBodyGenerationException {
        return format(encoding, Arrays.asList(values));
    }

    /**
     * URL encodes the specified collection of name-value pairs.
     * 
     * @param charset The encoding to use.
     * @param values The name-value pairs.
     * @return The URL encoded of the name-value pairs.
     * @throws HttpRequestBodyGenerationException If an error occurs formating
     * the pairs.
     */
    public static @NotNull String format(
            final @NotNull Charset charset,
            final @NotNull Collection<@NotNull Pair<@NotNull String, @NotNull String>> values)
    throws HttpRequestBodyGenerationException {
        return format(charset.name(), values);
    }

    /**
     * URL encodes the specified collection of name-value pairs.
     * 
     * @param encoding The encoding to use.
     * @param values The name-value pairs.
     * @return The URL encoded of the name-value pairs.
     * @throws HttpRequestBodyGenerationException If an error occurs formating
     * the pairs.
     */
    public static @NotNull String format(
            final @NotNull String encoding,
            final @NotNull Collection<@NotNull Pair<@NotNull String, @NotNull String>> values)
    throws HttpRequestBodyGenerationException {
        final StringBuilder buffer = new StringBuilder(); 
        for (final Pair<String, String> value : values) {
            if (buffer.length() > 0) {
                buffer.append(PAIR_SEPARATOR);
            }
            buffer.append(formatPair(encoding, value));
        }
        return buffer.toString();
    }

    /**
     * Formats a URL encoded pair of name and value.
     * 
     * @param charset The encoding to use.
     * @param value The name-value pair.
     * @return The URL encoded pair.
     * @throws HttpRequestBodyGenerationException If an error occurs formating
     * the pair.
     */
    public static @NotNull String formatPair(
            final @NotNull Charset charset,
            final @NotNull Map.Entry<@NotNull String, @NotNull String> value)
    throws HttpRequestBodyGenerationException {
        return formatPair(charset, value.getKey(), value.getValue());
    }

    /**
     * Formats a URL encoded pair of name and value.
     * 
     * @param encoding The encoding to use.
     * @param value The name-value pair.
     * @return The URL encoded pair.
     * @throws HttpRequestBodyGenerationException If an error occurs formating
     * the pair.
     */
    public static @NotNull String formatPair(
            final @NotNull String encoding,
            final @NotNull Map.Entry<@NotNull String, @NotNull String> value)
    throws HttpRequestBodyGenerationException {
        return formatPair(encoding, value.getKey(), value.getValue());
    }

    /**
     * Formats a URL encoded pair of name and value.
     * 
     * @param charset The encoding to use.
     * @param value The name-value pair.
     * @return The URL encoded pair.
     * @throws HttpRequestBodyGenerationException If an error occurs formating
     * the pair.
     */
    public static @NotNull String formatPair(
            final @NotNull Charset charset,
            final @NotNull Pair<@NotNull String, @NotNull String> value)
    throws HttpRequestBodyGenerationException {
        return formatPair(charset, value.getKey(), value.getValue());
    }

    /**
     * Formats a URL encoded pair of name and value.
     * 
     * @param encoding The encoding to use.
     * @param value The name-value pair.
     * @return The URL encoded pair.
     * @throws HttpRequestBodyGenerationException If an error occurs formating
     * the pair.
     */
    public static @NotNull String formatPair(
            final @NotNull String encoding,
            final @NotNull Pair<@NotNull String, @NotNull String> value)
    throws HttpRequestBodyGenerationException {
        return formatPair(encoding, value.getKey(), value.getValue());
    }

    /**
     * Formats a URL encoded pair of name and value.
     * 
     * @param charset The encoding to use.
     * @param name The parameter name.
     * @param value The parameter value.
     * @return The URL encoded pair.
     * @throws HttpRequestBodyGenerationException If an error occurs formating
     * the pair.
     */
    public static @NotNull String formatPair(
            final @NotNull Charset charset,
            final @NotNull String name,
            final @NotNull String value)
    throws HttpRequestBodyGenerationException {
        final StringBuilder buffer = new StringBuilder();
        formatPair(buffer, charset, name, value);
        return buffer.toString();
    }

    /**
     * Formats a URL encoded pair of name and value.
     * 
     * @param encoding The encoding to use.
     * @param name The parameter name.
     * @param value The parameter value.
     * @return The URL encoded pair.
     * @throws HttpRequestBodyGenerationException If an error occurs formating
     * the pair.
     */
    public static @NotNull String formatPair(
            final @NotNull String encoding,
            final @NotNull String name,
            final @NotNull String value)
    throws HttpRequestBodyGenerationException {
        final StringBuilder buffer = new StringBuilder();
        formatPair(buffer, encoding, name, value);
        return buffer.toString();
    }

    /**
     * Formats a URL encoded pair of name and value.
     * 
     * @param target The buffer to add the formated pair to.
     * @param charset The encoding to use.
     * @param value The name-value pair.
     * @throws HttpRequestBodyGenerationException If an error occurs formating
     * the pair.
     */
    public static void formatPair(
            final @NotNull StringBuilder target,
            final @NotNull Charset charset,
            final @NotNull Map.Entry<String, String> value)
    throws HttpRequestBodyGenerationException {
        formatPair(target, charset, value.getKey(), value.getValue());
    }

    /**
     * Formats a URL encoded pair of name and value.
     * 
     * @param target The buffer to add the formated pair to.
     * @param encoding The encoding to use.
     * @param value The name-value pair.
     * @throws HttpRequestBodyGenerationException If an error occurs formating
     * the pair.
     */
    public static void formatPair(
            final @NotNull StringBuilder target,
            final @NotNull String encoding,
            final @NotNull Map.Entry<String, String> value)
    throws HttpRequestBodyGenerationException {
        formatPair(target, encoding, value.getKey(), value.getValue());
    }

    /**
     * Formats a URL encoded pair of name and value.
     * 
     * @param target The buffer to add the formated pair to.
     * @param charset The encoding to use.
     * @param value The name-value pair.
     * @throws HttpRequestBodyGenerationException If an error occurs formating
     * the pair.
     */
    public static void formatPair(
            final @NotNull StringBuilder target,
            final @NotNull Charset charset,
            final @NotNull Pair<String, String> value)
    throws HttpRequestBodyGenerationException {
        formatPair(target, charset, value.getKey(), value.getValue());
    }

    /**
     * Formats a URL encoded pair of name and value.
     * 
     * @param target The buffer to add the formated pair to.
     * @param encoding The encoding to use.
     * @param value The name-value pair.
     * @throws HttpRequestBodyGenerationException If an error occurs formating
     * the pair.
     */
    public static void formatPair(
            final @NotNull StringBuilder target,
            final @NotNull String encoding,
            final @NotNull Pair<String, String> value)
    throws HttpRequestBodyGenerationException {
        formatPair(target, encoding, value.getKey(), value.getValue());
    }

    /**
     * Formats a URL encoded pair of name and value.
     * 
     * @param target The buffer to add the formated pair to.
     * @param charset The encoding to use.
     * @param name The parameter name.
     * @param value The parameter value.
     * @throws HttpRequestBodyGenerationException If an error occurs formating
     * the pair.
     */
    public static void formatPair(
            final @NotNull StringBuilder target,
            final @NotNull Charset charset,
            final @NotNull String name,
            final String value)
    throws HttpRequestBodyGenerationException {
        formatPair(target, Validate.notNull(charset).name(), name, value);
    }

    /**
     * Formats a URL encoded pair of name and value.
     * 
     * @param target The buffer to add the formated pair to.
     * @param encoding The encoding to use.
     * @param name The parameter name.
     * @param value The parameter value.
     * @throws HttpRequestBodyGenerationException If an error occurs formating
     * the pair.
     */
    public static void formatPair(
            final @NotNull StringBuilder target,
            final @NotNull String encoding,
            final @NotNull String name,
            final @NotNull String value)
    throws HttpRequestBodyGenerationException {
        Validate.notNull(target);
        Validate.notNull(encoding);
        Validate.notNull(name);
        Validate.notNull(value);
        try {
            target.append(URLEncoder.encode(name, encoding))
                .append(VALUE_SEPARATOR)
                .append(URLEncoder.encode(value, encoding));
        } catch (final UnsupportedEncodingException | UnsupportedOperationException e) {
            throw new HttpRequestBodyGenerationException(e);
        }
    }

    /**
     * Parses a URL encoded collection of name and value pairs.
     * 
     * @param charset The encoding to use.
     * @param input The URL encoded pair.
     * @return The parsed name-value pairs.
     * @throws HttpResponseBodyParsingException If an error occurs parsing the
     * pairs.
     */
    public static @NotNull Collection<@NotNull Pair<@NotNull String, @NotNull String>> parse(
            final @NotNull Charset charset,
            final @NotNull String input)
    throws HttpResponseBodyParsingException {
        return parse(charset.name(), input);
    }

    /**
     * Parses a URL encoded collection of name and value pairs.
     * 
     * @param encoding The encoding to use.
     * @param input The URL encoded pair.
     * @return The parsed name-value pairs.
     * @throws HttpResponseBodyParsingException If an error occurs parsing the
     * pairs.
     */
    public static @NotNull Collection<@NotNull Pair<@NotNull String, @NotNull String>> parse(
            final @NotNull String encoding,
            final @NotNull String input)
    throws HttpResponseBodyParsingException {
        final String[] pairs = input.split(PAIR_SEPARATOR);
        final List<Pair<String, String>> result = new ArrayList<>();
        for (final String pair : pairs) {
            result.add(parsePair(encoding, pair));
        }
        return result;
    }

    /**
     * Parses a URL encoded pair of name and value.
     * 
     * @param charset The encoding to use.
     * @param input The URL encoded pair.
     * @return The parsed name-value pair.
     * @throws HttpResponseBodyParsingException If an error occurs parsing the
     * pair.
     */
    public static @NotNull Pair<@NotNull String, @NotNull String> parsePair(
            final @NotNull Charset charset,
            final @NotNull String input)
    throws HttpResponseBodyParsingException {
        return parsePair(Validate.notNull(charset).name(), input);
    }

    /**
     * Parses a URL encoded pair of name and value.
     * 
     * @param encoding The encoding to use.
     * @param input The URL encoded pair.
     * @return The parsed name-value pair.
     * @throws HttpResponseBodyParsingException If an error occurs parsing the
     * pair.
     */
    public static @NotNull Pair<@NotNull String, @NotNull String> parsePair(
            final @NotNull String encoding,
            final @NotNull String input)
    throws HttpResponseBodyParsingException {
        Validate.notNull(encoding);
        Validate.notNull(input);
        final String[] parts = input.split(VALUE_SEPARATOR);
        if (parts.length != 2) {
            throw new HttpResponseBodyParsingException(
                    "Invalid URL encoded name-value pair: " + input);
        }
        try {
            final String name = URLDecoder.decode(parts[0], encoding);
            final String value = URLDecoder.decode(parts[1], encoding);
            return Pair.of(name, value);
        } catch (final UnsupportedEncodingException e) {
            throw new HttpResponseBodyParsingException(e);
        }
    }
}
