package dev.orne.http.client.it.ipapi;

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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Bean for {@code IpAPI} API errors.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-08
 * @since 0.1
 * @see {@linkplain https://ipapi.com/documentation#api_error_codes}
 */
@JsonDeserialize(using = JsonDeserializer.None.class)
public class IpapiError {

    /** If the service call succeeded. Always false? */
    private boolean success;
    /** The API error information. */
    @JsonProperty("error")
    private ErrorInfo info;

    /**
     * Empty constructor.
     */
    public IpapiError() {
        super();
    }
    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    public IpapiError(
            final @NotNull IpapiError copy) {
        super();
        this.success = copy.success;
        this.info = copy.info;
    }
    /**
     * Returns {@code true} if the service call succeeded.
     * 
     * @return If the service call succeeded.
     */
    public boolean isSuccess() {
        return this.success;
    }
    /**
     * Sets if the service call succeeded.
     * 
     * @param success If the service call succeeded.
     */
    public void setSuccess(
            final boolean success) {
        this.success = success;
    }
    /**
     * Returns the API error information.
     * 
     * @return The API error information.
     */
    public ErrorInfo getInfo() {
        return this.info;
    }
    /**
     * Sets the API error information.
     * 
     * @param info The API error information.
     */
    public void setInfo(
            final ErrorInfo info) {
        this.info = info;
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
     * Bean for {@code IpAPI} API error information.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     * @see {@linkplain https://ipapi.com/documentation#api_error_codes}
     */
    public static class ErrorInfo {

        /** The error numeric code. */
        private int code;
        /** The error type. */
        private String type;
        /** The error textual explanation. */
        @JsonProperty("info")
        private String cause;

        /**
         * Empty constructor.
         */
        public ErrorInfo() {
            super();
        }
        /**
         * Copy constructor.
         * 
         * @param copy The instance to copy.
         */
        public ErrorInfo(
                final @NotNull ErrorInfo copy) {
            super();
            this.code = copy.code;
            this.type = copy.type;
            this.cause = copy.cause;
        }
        /**
         * Returns the error numeric code.
         * 
         * @return The error numeric code.
         */
        public int getCode() {
            return this.code;
        }
        /**
         * Sets the error numeric code.
         * 
         * @param code The error numeric code.
         */
        public void setCode(
                final int code) {
            this.code = code;
        }
        /**
         * Returns the error type.
         * 
         * @return The error type.
         */
        public String getType() {
            return this.type;
        }
        /**
         * Sets the error type.
         * 
         * @param type The error type.
         */
        public void setType(
                final String type) {
            this.type = type;
        }
        @JsonIgnore
        public @NotNull ErrorType getTypeEnum() {
            return ErrorType.forCode(this.type);
        }
        /**
         * Returns the error textual explanation.
         * 
         * @return The error textual explanation.
         */
        public String getCause() {
            return this.cause;
        }
        /**
         * Sets the error textual explanation.
         * 
         * @param cause The error textual explanation.
         */
        public void setCause(
                final String cause) {
            this.cause = cause;
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
    }

    /**
     * Enumeration for known {@code IpAPI} error types.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     * @see {@linkplain https://ipapi.com/documentation#api_error_codes}
     */
    public static enum ErrorType {
        NOT_FOUND("404_not_found"),
        MISSING_ACCESS_KEY("missing_access_key"),
        INVALID_ACCESS_KEY("invalid_access_key"),
        INACTIVE_USER("inactive_user"),
        INVALID_API_FUNCTION("invalid_api_function"),
        USAGE_LIMIT_REACHED("usage_limit_reached"),
        FUNCTION_ACCESS_RESTRICTED("function_access_restricted"),
        HTTPS_ACCESS_RESTRICTED("https_access_restricted"),
        INVALID_FIELDS("invalid_fields"),
        TOO_MANY_IPS("too_many_ips"),
        BATCH_NOT_SUPPORTED("batch_not_supported_on_plan"),
        @JsonEnumDefaultValue
        UNKNOWN(null),
        ;
        /** The error type API code. */
        private final @NotNull String code;
        /**
         * Constant constructor.
         * 
         * @param code The error type API code.
         */
        private ErrorType(
                final @NotNull String code) {
            this.code = code;
        }
        /**
         * Returns the error type API code.
         * 
         * @return The error type API code.
         */
        @JsonValue
        public @NotNull String getCode() {
            return this.code;
        }
        /**
         * Returns that constant for the specified error type API code.
         * <p>
         * If the code is {@code null} or no constant exist with such code
         * {@code UNKNOWN} is returned.
         * 
         * @param code The error type API code.
         * @return The constant for the specified API code.
         */
        public static @NotNull ErrorType forCode(
                final String code) {
            ErrorType result = UNKNOWN;
            if (code != null) {
                for (ErrorType type : ErrorType.values()) {
                    if (code.equals(type.code)) {
                        result = type;
                        break;
                    }
                }
            }
            return result;
        }
    }
}
