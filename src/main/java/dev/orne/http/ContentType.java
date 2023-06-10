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
import java.nio.charset.UnsupportedCharsetException;

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

    /** The separator between the media type and the optional parameter. */
    private static final String PARAMETER_SEPARATOR = ";";
    /** The content encoding parameter prefix. */
    private static final String CHARSET_PREFIX = "charset=";
    /** The parts boundary parameter prefix. */
    private static final String BOUNDARY_PREFIX = "boundary=";

    /** The content media type. */
    private String mediaType;
    /** The content encoding. */
    private Charset charset;
    /** The parts boundary. */
    private String boundary;

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
        Validate.isTrue(MediaTypes.isText(mediaType));
        final ContentType result = new ContentType();
        result.mediaType = mediaType;
        result.charset = charset;
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
        result.boundary = boundary;
        return result;
    }

    public static ContentType parse(
            final @NotNull String header)
    throws UnsupportedCharsetException {
        final ContentType result = new ContentType();
        if (header.contains(PARAMETER_SEPARATOR)) {
            final String[] parts = header.split(PARAMETER_SEPARATOR, 2);
            result.mediaType = parts[0].trim();
            final String param = parts[1].trim();
            if (param.startsWith(CHARSET_PREFIX)) {
                result.charset = Charset.forName(param.substring(8));
            } else if (param.startsWith(BOUNDARY_PREFIX)) {
                result.boundary = param.substring(9);
            } else {
                result.mediaType = header;
            }
        } else {
            result.mediaType = header;
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
     * Returns the content encoding.
     * 
     * @return The content encoding
     */
    public Charset getCharset() {
        return this.charset;
    }

    /**
     * Returns the parts boundary.
     * 
     * @return The parts boundary.
     */
    public String getBoundary() {
        return this.boundary;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.mediaType)
                .append(this.charset)
                .append(this.boundary)
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
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(this.mediaType);
        if (this.charset != null || this.boundary != null) {
            builder.append(PARAMETER_SEPARATOR).append(" ");
            if (this.charset != null) {
                builder.append(CHARSET_PREFIX).append(charset.name().toLowerCase());
            } else {
                builder.append(BOUNDARY_PREFIX).append(boundary);
            }
        }
        return builder.toString();
    }
}
