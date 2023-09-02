package dev.orne.http.client.example.ipapi;

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

import java.util.Arrays;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Bean for {@code IpAPI} API common parameters.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-08
 * @since 0.1
 * @see {@linkplain https://ipapi.com/documentation}
 */
public class IpapiParams {

    /** The desired response format. */
    private String format;
    /** The desired response fields. */
    private String[] fields;
    /** The desired response language. */
    private String language;
    /** If host name lookup should be performed. */
    private boolean lookupHostName;
    /** If security data retrieval should be performed. */
    private boolean retrieveSecurityData;

    public IpapiParams() {
        super();
    }
    public IpapiParams(
            final @NotNull IpapiParams copy) {
        super();
        this.format = copy.format;
        this.fields = copy.fields == null ? null : Arrays.copyOf(copy.fields, copy.fields.length);
        this.language = copy.language;
        this.lookupHostName = copy.lookupHostName;
        this.retrieveSecurityData = copy.retrieveSecurityData;
    }
    /**
     * Returns the desired response format.
     * 
     * @return The desired response format.
     * @see {@linkplain https://ipapi.com/documentation#specify_response_format}
     */
    public String getFormat() {
        return this.format;
    }
    /**
     * Sets the desired response format.
     * 
     * @param format The desired response format.
     * @see {@linkplain https://ipapi.com/documentation#specify_response_format}
     */
    public void setFormat(
            final String format) {
        this.format = format;
    }
    /**
     * Sets the desired response format.
     * 
     * @param format The desired response format.
     * @see {@linkplain https://ipapi.com/documentation#specify_response_format}
     */
    public void setFormat(
            final Format format) {
        this.format = format == null ? null : format.getCode();
    }
    /**
     * Returns the desired response fields.
     * 
     * @return The desired response fields.
     * @see {@linkplain https://ipapi.com/documentation#specify_response_fields}
     */
    public String[] getFields() {
        return this.fields == null ? null : Arrays.copyOf(this.fields, this.fields.length);
    }
    /**
     * Sets the desired response fields.
     * 
     * @param fields The desired response fields.
     * @see {@linkplain https://ipapi.com/documentation#specify_response_fields}
     */
    public void setFields(
            final String... fields) {
        if (fields == null) {
            this.fields = null;
        } else {
            Validate.noNullElements(fields);
            this.fields = Arrays.copyOf(fields, fields.length);
        }
    }
    /**
     * Returns the desired response language.
     * 
     * @return The desired response language.
     * @see {@linkplain https://ipapi.com/documentation#specify_response_language}
     */
    public String getLanguage() {
        return this.language;
    }
    /**
     * Sets the desired response language.
     * 
     * @param language The desired response language.
     * @see {@linkplain https://ipapi.com/documentation#specify_response_language}
     */
    public void setLanguage(
            final String language) {
        this.language = language;
    }
    /**
     * Sets the desired response language.
     * 
     * @param language The desired response language.
     * @see {@linkplain https://ipapi.com/documentation#specify_response_language}
     */
    public void setLanguage(
            final Language language) {
        this.language = language == null ? null : language.getCode();
    }
    /**
     * Returns {@code true} if host name lookup should be performed.
     * 
     * @return If host name lookup should be performed.
     * @see {@linkplain https://ipapi.com/documentation#enable_hostname_lookup}
     */
    public boolean isLookupHostName() {
        return this.lookupHostName;
    }
    /**
     * Sets if host name lookup should be performed.
     * 
     * @param lookup If host name lookup should be performed.
     * @see {@linkplain https://ipapi.com/documentation#enable_hostname_lookup}
     */
    public void setLookupHostName(
            final boolean lookup) {
        this.lookupHostName = lookup;
    }
    /**
     * Returns {@code true} if security data retrieval should be performed.
     * 
     * @return If security data retrieval should be performed.
     * @see https://ipapi.com/documentation#enable_security_data
     */
    public boolean isRetrieveSecurityData() {
        return this.retrieveSecurityData;
    }
    /**
     * Sets if security data retrieval should be performed.
     * 
     * @param retrieve If security data retrieval should be performed.
     * @see https://ipapi.com/documentation#enable_security_data
     */
    public void setRetrieveSecurityData(
            final boolean retrieve) {
        this.retrieveSecurityData = retrieve;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    /**
     * Enumeration for formats known to be supported by {@code IpAPI}.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     * @see {@linkplain https://ipapi.com/documentation#specify_response_format}
     */
    public enum Format {
        JSON("json"),
        XML("xml");
        /** The format API code. */
        private final @NotNull String code;
        /**
         * Constant constructor.
         * 
         * @param code The format API code.
         */
        private Format(
                final @NotNull String code) {
            this.code = code;
        }
        /**
         * Returns the format API code.
         * 
         * @return The format API code.
         */
        public @NotNull String getCode() {
            return this.code;
        }
    }

    /**
     * Enumeration for languages known to be supported by {@code IpAPI}.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     * @see {@linkplain https://ipapi.com/documentation#specify_response_language}
     */
    public enum Language {
        ENGLISH("en"),
        GERMAN("de"),
        SPANISH("es"),
        FRENCH("fr"),
        JAPANESE("ja"),
        PORTUGUESE("pt-br"),
        RUSSIAN("ru"),
        CHINESE("zh");
        /** The language API code. */
        private final @NotNull String code;
        /**
         * Constant constructor.
         * 
         * @param code The language API code.
         */
        private Language(
                final @NotNull String code) {
            this.code = code;
        }
        /**
         * Returns the language API code.
         * 
         * @return The language API code.
         */
        public @NotNull String getCode() {
            return this.code;
        }
    }
}
