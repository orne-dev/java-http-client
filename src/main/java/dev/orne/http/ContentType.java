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

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Immutable bean for HTTP content type headers.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public class ContentType {

    /** The separator preceding a parameter declaration. */
    public static final String PARAMETER_SEPARATOR = ";";
    /** The separator between parameter name and value. */
    public static final String PARAMETER_VALUE_SEPARATOR = "=";
    /** The content encoding parameter name. */
    public static final String CHARSET_PARAM = "charset";
    /** The parts boundary parameter name. */
    public static final String BOUNDARY_PARAM = "boundary";

    /** The content media type. */
    private String mediaType;
    /** The content type parameters. */
    private Map<String, String> parameters = new LinkedHashMap<>();

    /**
     * Private constructor.
     * Use factory methods instead.
     */
    private ContentType() {
        super();
    }

    /**
     * Creates a new content type of the specified non text and non multipart
     * media type.
     * 
     * @param mediaType The content media type.
     * @return The created instance.
     */
    public static ContentType of(
            final @NotNull String mediaType) {
        Validate.notNull(mediaType);
        Validate.isTrue(!MediaTypes.isMultipart(mediaType));
        Validate.isTrue(!MediaTypes.isText(mediaType));
        final ContentType result = new ContentType();
        result.mediaType = mediaType;
        return result;
    }

    /**
     * Creates a new content type of the specified text based media type.
     * 
     * @param mediaType The content media type.
     * @param charset The content encoding.
     * @return The created instance.
     */
    public static ContentType of(
            final @NotNull String mediaType,
            final @NotNull Charset charset) {
        Validate.notNull(mediaType);
        Validate.isTrue(!MediaTypes.isAudio(mediaType));
        Validate.isTrue(!MediaTypes.isFont(mediaType));
        Validate.isTrue(!MediaTypes.isImage(mediaType));
        Validate.isTrue(!MediaTypes.isMultipart(mediaType));
        Validate.isTrue(!MediaTypes.isVideo(mediaType));
        final ContentType result = new ContentType();
        result.mediaType = mediaType;
        result.parameters.put(CHARSET_PARAM, charset.name());
        return result;
    }

    /**
     * Creates a new content type of the specified meltipart media type.
     * 
     * @param mediaType The content media type.
     * @param boundary The parts boundary.
     * @return The created instance.
     */
    public static ContentType multipart(
            final @NotNull String mediaType,
            final @NotNull String boundary) {
        Validate.notNull(mediaType);
        Validate.isTrue(MediaTypes.isMultipart(mediaType));
        Validate.notNull(boundary);
        final ContentType result = new ContentType();
        result.mediaType = mediaType;
        result.parameters.put(BOUNDARY_PARAM, boundary);
        return result;
    }

    /**
     * Parses the specified HTTP content type header value.
     * 
     * @param header The HTTP content type header value.
     * @return The parsed content type;
     * @throws UnsupportedCharsetException If the parsed content type uses an
     * unsupported charset.
     */
    public static ContentType parse(
            final @NotNull String header)
    throws UnsupportedCharsetException {
        Validate.notNull(header);
        final ContentType result = new ContentType();
        final StringTokenizer tokenizer = new StringTokenizer(
                header,
                PARAMETER_SEPARATOR + PARAMETER_VALUE_SEPARATOR);
        if (!tokenizer.hasMoreTokens()) {
            throw new IllegalArgumentException("Illegal HTTP content type header value. No media type : " + header);
        }
        result.mediaType = tokenizer.nextToken().trim();
        while (tokenizer.hasMoreTokens()) {
            final String name = tokenizer.nextToken().trim().toLowerCase();
            String value = tokenizer.nextToken().trim();
            if (value.length() > 1 &&  value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            result.parameters.put(name, value);
        }
        return result;
    }

    /**
     * Returns the content media type.
     * 
     * @return The content media type
     */
    public @NotNull String getMediaType() {
        return this.mediaType;
    }

    /**
     * Returns {@code true} if the content type consists in multiple parts.
     * 
     * @return {@code true} if the content type consists in multiple parts.
     */
    public boolean isMultipart() {
        return MediaTypes.isMultipart(this.mediaType);
    }

    /**
     * Returns the content charset.
     * 
     * @return The content charset.
     * @throws  IllegalCharsetNameException
     *          If the content type contains a charset parameter but the given
     *          charset name is illegal.
     * @throws  UnsupportedCharsetException
     *          If the content type contains a charset parameter but no support
     *          for the named charset is available in this instance of the Java
     *          virtual machine.
     */
    public Charset getCharset() {
        final String charset = getParameter(CHARSET_PARAM);
        if (charset == null) {
            return null;
        } else {
            return Charset.forName(charset);
        }
    }

    /**
     * Returns the parts boundary.
     * 
     * @return The parts boundary.
     */
    public String getBoundary() {
        return getParameter(BOUNDARY_PARAM);
    }

    /**
     * Returns an unmodifiable copy of this content type parameters.
     *  
     * @return The content type parameters.
     */
    public @NotNull Map<String, String> getParameters() {
        return Collections.unmodifiableMap(this.parameters);
    }

    /**
     * Returns the value of the specified parameter.
     * 
     * @param name The parameter name.
     * @return The parameter value, or {@code null} if not present.
     */
    public String getParameter(
            final @NotNull String name) {
        return this.parameters.get(Validate.notNull(name).toLowerCase());
    }

    /**
     * Sets the value of the specified parameter.
     * If the parameter value is {@code null} the parameter is removed.
     * 
     * @param name The parameter name.
     * @param value The parameter value.
     */
    public void setParameter(
            final @NotNull String name,
            final String value) {
        if (value == null) {
            removeParameter(name);
        } else {
            this.parameters.put(
                    Validate.notNull(name).toLowerCase(),
                    Validate.notNull(value));
        }
    }

    /**
     * Sets the value of the specified parameter.
     * If the parameter value is {@code null} the parameter is removed.
     * 
     * @param name The parameter name.
     * @param value The parameter value.
     */
    public void removeParameter(
            final @NotNull String name) {
        this.parameters.remove(Validate.notNull(name).toLowerCase());
    }

    /**
     * Returns the HTTP content type header value representing this content
     * type.
     * 
     * @return The HTTP content type header value.
     */
    public @NotNull String getHeader() {
        final StringBuilder builder = new StringBuilder(this.mediaType);
        for (final Map.Entry<String, String> param : this.parameters.entrySet()) {
            builder.append(PARAMETER_SEPARATOR)
                .append(param.getKey())
                .append(PARAMETER_VALUE_SEPARATOR)
                .append(param.getValue());
        }
        return builder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.mediaType)
                .append(this.parameters)
                .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }
        final ContentType other = (ContentType) obj;
        return new EqualsBuilder()
                .append(this.mediaType, other.mediaType)
                .append(this.parameters, other.parameters)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String toString() {
        return getHeader();
    }
}
