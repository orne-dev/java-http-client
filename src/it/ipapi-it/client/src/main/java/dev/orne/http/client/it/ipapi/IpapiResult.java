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

import java.util.Arrays;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Bean for {@code IpAPI} API result IP information.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-08
 * @since 0.1
 * @see {@linkplain https://ipapi.com/documentation#api_response_objects}
 */
@JsonDeserialize(using = JsonDeserializer.None.class)
@XmlRootElement(name = "result", namespace = "")
public class IpapiResult {

    /** The IP address. */
    private String ip;
    /** The resolved host name associated with the IP address. */
    private String hostname;
    /** The IP type. */
    private IpType type;
    /** The 2-letter continent code associated with the IP address. */
    @JsonProperty("continent_code")
    @XmlElement(name = "continent_code")
    private String continentCode;
    /** The continent name associated with the IP address. */
    @JsonProperty("continent_name")
    @XmlElement(name = "continent_name")
    private String continentName;
    /** The 2-letter country code associated with the IP address. */
    @JsonProperty("country_code")
    @XmlElement(name = "country_code")
    private String countryCode;
    /** The country name associated with the IP address. */
    @JsonProperty("country_name")
    @XmlElement(name = "country_name")
    private String countryName;
    /** The region code associated with the IP address. */
    @JsonProperty("region_code")
    @XmlElement(name = "region_code")
    private String regionCode;
    /** The region name associated with the IP address. */
    @JsonProperty("region_name")
    @XmlElement(name = "region_name")
    private String regionName;
    /** The city associated with the IP address. */
    private String city;
    /** The ZIP code associated with the IP address. */
    private String zip;
    /** The latitude associated with the IP address. */
    private Double latitude;
    /** The longitude associated with the IP address. */
    private Double longitude;
    /** The location of the IP address. */
    private Location location;
    /** The data related to time zone. */
    @JsonProperty("time_zone")
    @XmlElement(name = "time_zone")
    private TimeZone timeZone;
    /** The data related to currency. */
    private Currency currency;
    /** The data related to connection. */
    private Connection connection;
    /** The data related to security. */
    private Security security;

    public IpapiResult() {
        super();
    }
    public IpapiResult(
            final @NotNull IpapiResult copy) {
        super();
        this.ip = copy.ip;
        this.hostname = copy.hostname;
        this.type = copy.type;
        this.continentCode = copy.continentCode;
        this.continentName = copy.continentName;
        this.countryName = copy.countryName;
        this.countryName = copy.countryName;
        this.regionName = copy.regionName;
        this.regionName = copy.regionName;
        this.zip = copy.zip;
        this.latitude = copy.latitude;
        this.longitude = copy.longitude;
        this.location = copy.location;
        this.timeZone = copy.timeZone;
        this.currency = copy.currency;
        this.connection = copy.connection;
        this.security = copy.security;
    }
    /**
     * Returns the IP address.
     * 
     * @return The IP address. 
     */
    public String getIp() {
        return this.ip;
    }
    /**
     * Sets the IP address.
     * 
     * @param ip The IP address.
     */
    public void setIp(
            final String ip) {
        this.ip = ip;
    }
    /**
     * Returns the resolved host name associated with the IP address.
     * 
     * @return The resolved host name.
     */
    public String getHostname() {
        return this.hostname;
    }
    /**
     * Sets the resolved host name associated with the IP address.
     * 
     * @param hostname The resolved host name.
     */
    public void setHostname(
            final String hostname) {
        this.hostname = hostname;
    }
    /**
     * Returns the IP type.
     * 
     * @return The IP type.
     */
    public IpType getType() {
        return this.type;
    }
    /**
     * Sets the IP type.
     * 
     * @param type The IP type.
     */
    public void setType(
            final IpType type) {
        this.type = type;
    }
    /**
     * Returns the 2-letter continent code associated with the IP address.
     * 
     * @return The 2-letter continent code.
     */
    public String getContinentCode() {
        return this.continentCode;
    }
    /**
     * Sets the 2-letter continent code associated with the IP address.
     * 
     * @param continentCode The 2-letter continent code.
     */
    public void setContinentCode(
            final String continentCode) {
        this.continentCode = continentCode;
    }
    /**
     * Returns the continent name associated with the IP address.
     * 
     * @return The continent name.
     */
    public String getContinentName() {
        return this.continentName;
    }
    /**
     * Sets the continent name associated with the IP address.
     * 
     * @param continentName The continent name.
     */
    public void setContinentName(
            final String continentName) {
        this.continentName = continentName;
    }
    /**
     * Returns the 2-letter country code associated with the IP address.
     * 
     * @return The 2-letter country code.
     */
    public String getCountryCode() {
        return this.countryCode;
    }
    /**
     * Sets the 2-letter country code associated with the IP address.
     * 
     * @param countryCode The 2-letter country code.
     */
    public void setCountryCode(
            final String countryCode) {
        this.countryCode = countryCode;
    }
    /**
     * Returns the country name associated with the IP address.
     * 
     * @return The country name.
     */
    public String getCountryName() {
        return this.countryName;
    }
    /**
     * Sets the country name associated with the IP address.
     * 
     * @param countryName The country name.
     */
    public void setCountryName(
            final String countryName) {
        this.countryName = countryName;
    }
    /**
     * Returns the region code associated with the IP address.
     * 
     * @return The region code.
     */
    public String getRegionCode() {
        return this.regionCode;
    }
    /**
     * Sets the region code associated with the IP address.
     * 
     * @param regionCode The region code.
     */
    public void setRegionCode(
            final String regionCode) {
        this.regionCode = regionCode;
    }
    /**
     * Returns the region name associated with the IP address.
     * 
     * @return The region name.
     */
    public String getRegionName() {
        return this.regionName;
    }
    /**
     * Sets the region name associated with the IP address.
     * 
     * @param regionName The region name.
     */
    public void setRegionName(
            final String regionName) {
        this.regionName = regionName;
    }
    /**
     * Returns the city associated with the IP address.
     * 
     * @return The city.
     */
    public String getCity() {
        return this.city;
    }
    
    /**
     * Sets the city associated with the IP address.
     * 
     * @param city The city.
     */
    public void setCity(
            final String city) {
        this.city = city;
    }
    /**
     * Returns the ZIP code associated with the IP address.
     * 
     * @return The ZIP code.
     */
    public String getZip() {
        return this.zip;
    }
    /**
     * Sets the ZIP code associated with the IP address.
     * 
     * @param zip The ZIP code.
     */
    public void setZip(
            final String zip) {
        this.zip = zip;
    }
    /**
     * Returns the latitude associated with the IP address.
     * 
     * @return The latitude.
     */
    public Double getLatitude() {
        return this.latitude;
    }
    /**
     * Sets the latitude associated with the IP address.
     * 
     * @param latitude The latitude.
     */
    public void setLatitude(
            final Double latitude) {
        this.latitude = latitude;
    }
    /**
     * Returns the longitude associated with the IP address.
     * 
     * @return The longitude.
     */
    public Double getLongitude() {
        return this.longitude;
    }
    /**
     * Sets the longitude associated with the IP address.
     * 
     * @param longitude The longitude.
     */
    public void setLongitude(
            final Double longitude) {
        this.longitude = longitude;
    }
    /**
     * Returns the locations of the IP address.
     * 
     * @return The locations of the IP address.
     */
    public Location getLocation() {
        return this.location;
    }
    /**
     * Sets the locations of the IP address.
     * 
     * @param location The locations.
     */
    public void setLocation(
            final Location location) {
        this.location = location;
    }
    /**
     * Returns the data related to time zone.
     * 
     * @return The data related to time zone.
     */
    public TimeZone getTimeZone() {
        return this.timeZone;
    }
    /**
     * Sets the data related to time zone.
     * 
     * @param timeZone The data related to time zone.
     */
    public void setTimeZone(
            final TimeZone timeZone) {
        this.timeZone = timeZone;
    }
    /**
     * Returns the data related to currency.
     * 
     * @return The data related to currency.
     */
    public Currency getCurrency() {
        return this.currency;
    }
    /**
     * Sets the data related to currency.
     * 
     * @param currency The data related to currency.
     */
    public void setCurrency(
            final Currency currency) {
        this.currency = currency;
    }
    /**
     * Returns the data related to connection.
     * 
     * @return The data related to connection.
     */
    public Connection getConnection() {
        return this.connection;
    }
    /**
     * Sets the data related to connection.
     * 
     * @param connection The data related to connection.
     */
    public void setConnection(
            final Connection connection) {
        this.connection = connection;
    }
    /**
     * Returns the data related to security.
     * 
     * @return The data related to security.
     */
    public Security getSecurity() {
        return this.security;
    }
    /**
     * Sets the data related to security.
     * 
     * @param security The data related to security.
     */
    public void setSecurity(
            final Security security) {
        this.security = security;
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
     * The IP types.
     */
    public enum IpType {
        /** IP4 address. */
        IPv4("ipv4"),
        /** IP6 address. */
        IPv6("ipv6"),
        ;
        private final String code;
        private IpType(
                final String code) {
            this.code = code;
        }
        @JsonValue
        public String getCode() {
            return code;
        }
    }

    /**
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     * @see {@linkplain https://ipapi.com/documentation#api_response_objects}
     */
    public static class Location {

        /** The geoname identifier in accordance with the Geonames Registry. */
        @JsonProperty("geoname_id")
        @XmlElement(name = "geoname_id")
        private Long id;
        /** The capital city of the country associated with the IP address. */
        private String capital;
        /** The languages associated with the IP address. */
        private Language[] languages;
        /** An HTTP URL to an SVG country flag icon associated with the IP address. */
        @JsonProperty("country_flag")
        @XmlElement(name = "country_flag")
        private String countryFlag;
        /** The emoji icon for the country flag associated with the IP address. */
        @JsonProperty("country_flag_emoji")
        @XmlElement(name = "country_flag_emoji")
        private String countryFlagEmoji;
        /** The unicode value of the emoji icon for the country flag associated with the IP address. */
        @JsonProperty("country_flag_emoji_unicode")
        @XmlElement(name = "country_flag_emoji_unicode")
        private String countryFlagEmojiUnicode;
        /** The calling/dial code associated with the IP address. */
        @JsonProperty("calling_code")
        @XmlElement(name = "calling_code")
        private String callingCode;
        /** If the given country is part of the EU. */
        @JsonProperty("is_eu")
        @XmlElement(name = "is_eu")
        private boolean eu;

        public Location() {
            super();
        }
        public Location(
                final @NotNull Location copy) {
            super();
            this.id = copy.id;
            this.capital = copy.capital;
            this.languages = copy.languages == null ? null : Arrays.copyOf(copy.languages, copy.languages.length);
            this.countryFlag = copy.countryFlag;
            this.countryFlagEmoji = copy.countryFlagEmoji;
            this.countryFlagEmojiUnicode = copy.countryFlagEmojiUnicode;
            this.callingCode = copy.callingCode;
            this.eu = copy.eu;
        }
        /**
         * Returns the geoname identifier in accordance with the Geonames Registry.
         * 
         * @return The geoname identifier.
         */
        public Long getId() {
            return this.id;
        }
        /**
         * Sets the geoname identifier in accordance with the Geonames Registry.
         * 
         * @param id The geoname identifier.
         */
        public void setId(
                final Long id) {
            this.id = id;
        }
        public String getCapital() {
            return this.capital;
        }
        public void setCapital(
                final String capital) {
            this.capital = capital;
        }
        public Language[] getLanguages() {
            return this.languages == null ? null : Arrays.copyOf(this.languages, this.languages.length);
        }
        public void setLanguages(
                final Language[] languages) {
            this.languages = languages == null ? null : Arrays.copyOf(languages, languages.length);;
        }
        public String getCountryFlag() {
            return this.countryFlag;
        }
        public void setCountryFlag(
                final String countryFlag) {
            this.countryFlag = countryFlag;
        }
        public String getCountryFlagEmoji() {
            return this.countryFlagEmoji;
        }
        public void setCountryFlagEmoji(
                final String countryFlagEmoji) {
            this.countryFlagEmoji = countryFlagEmoji;
        }
        public String getCountryFlagEmojiUnicode() {
            return this.countryFlagEmojiUnicode;
        }
        public void setCountryFlagEmojiUnicode(
                final String countryFlagEmojiUnicode) {
            this.countryFlagEmojiUnicode = countryFlagEmojiUnicode;
        }
        public String getCallingCode() {
            return this.callingCode;
        }
        public void setCallingCode(
                final String callingCode) {
            this.callingCode = callingCode;
        }
        public boolean isEu() {
            return this.eu;
        }
        public void setEu(
                final boolean eu) {
            this.eu = eu;
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
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     * @see {@linkplain https://ipapi.com/documentation#api_response_objects}
     */
    public static class Language {

        /** The 2-letter language code for the given language. */
        private String code;
        /** The language name (in the request's language). */
        private String name;
        /** The native language name. */
        @JsonProperty("native")
        @XmlElement(name = "native")
        private String nativeName;

        public Language() {
            super();
        }
        public Language(
                final @NotNull Language copy) {
            super();
            this.code = copy.code;
            this.name = copy.name;
            this.nativeName = copy.nativeName;
        }
        public String getCode() {
            return this.code;
        }
        public void setCode(
                final String code) {
            this.code = code;
        }
        public String getName() {
            return this.name;
        }
        public void setName(
                final String name) {
            this.name = name;
        }
        public String getNativeName() {
            return this.nativeName;
        }
        public void setNativeName(
                final String nativeName) {
            this.nativeName = nativeName;
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
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     * @see {@linkplain https://ipapi.com/documentation#api_response_objects}
     */
    public static class TimeZone {

        /** The 2-letter language code for the given language. */
        private String id;
        /** The current date and time associated with the IP address. */
        @JsonProperty("current_time")
        @XmlElement(name = "current_time")
        private String currentTime;
        /** The offset to GMT time of the given time zone in seconds. */
        @JsonProperty("gmt_offset")
        @XmlElement(name = "gmt_offset")
        private Long gmtOffset;
        /** The universal code of the given time zone. */
        private String code;
        /** If the given time zone is daylight saving time. */
        @JsonProperty("is_daylight_saving")
        @XmlElement(name = "is_daylight_saving")
        private boolean daylightSaving;

        public TimeZone() {
            super();
        }
        public TimeZone(
                final @NotNull TimeZone copy) {
            super();
            this.id = copy.id;
            this.currentTime = copy.currentTime;
            this.gmtOffset = copy.gmtOffset;
            this.code = copy.code;
            this.daylightSaving = copy.daylightSaving;
        }
        public String getId() {
            return this.id;
        }
        public void setId(
                final String id) {
            this.id = id;
        }
        public String getCurrentTime() {
            return this.currentTime;
        }
        public void setCurrentTime(
                final String currentTime) {
            this.currentTime = currentTime;
        }
        public Long getGmtOffset() {
            return this.gmtOffset;
        }
        public void setGmtOffset(
                final Long gmtOffset) {
            this.gmtOffset = gmtOffset;
        }
        public String getCode() {
            return this.code;
        }
        public void setCode(
                final String code) {
            this.code = code;
        }
        public boolean isDaylightSaving() {
            return this.daylightSaving;
        }
        public void setDaylightSaving(
                final boolean daylightSaving) {
            this.daylightSaving = daylightSaving;
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
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     * @see {@linkplain https://ipapi.com/documentation#api_response_objects}
     */
    public static class Currency {

        /** The 3-letter code of the main currency associated with the IP address. */
        private String code;
        /** The name of the given currency. */
        private String name;
        /** The the name of the given currency in plural. */
        @JsonProperty("plural")
        @XmlElement(name = "plural")
        private String pluralName;
        /** The symbol of the given currency. */
        private String symbol;
        /** The the native symbol of the given currency. */
        @JsonProperty("symbol_native")
        @XmlElement(name = "symbol_native")
        private String nativeSymbol;

        public Currency() {
            super();
        }
        public Currency(
                final @NotNull Currency copy) {
            super();
            this.code = copy.code;
            this.name = copy.name;
            this.pluralName = copy.pluralName;
            this.symbol = copy.symbol;
            this.nativeSymbol = copy.nativeSymbol;
        }
        public String getCode() {
            return this.code;
        }
        public void setCode(
                final String code) {
            this.code = code;
        }
        public String getName() {
            return this.name;
        }
        public void setName(
                final String name) {
            this.name = name;
        }
        public String getPluralName() {
            return this.pluralName;
        }
        public void setPluralName(
                final String pluralName) {
            this.pluralName = pluralName;
        }
        public String getSymbol() {
            return this.symbol;
        }
        public void setSymbol(
                final String symbol) {
            this.symbol = symbol;
        }
        public String getNativeSymbol() {
            return this.nativeSymbol;
        }
        public void setNativeSymbol(
                final String nativeSymbol) {
            this.nativeSymbol = nativeSymbol;
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
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     * @see {@linkplain https://ipapi.com/documentation#api_response_objects}
     */
    public static class Connection {

        /** The "Autonomous System Number" associated with the IP address. */
        private Integer asn;
        /** The ISP associated with the IP address. */
        private String isp;

        public Connection() {
            super();
        }
        public Connection(
                final @NotNull Connection copy) {
            super();
            this.asn = copy.asn;
            this.isp = copy.isp;
        }
        /**
         * Returns the "Autonomous System Number" associated with the IP address.
         * 
         * @return The "Autonomous System Number".
         */
        public Integer getAsn() {
            return this.asn;
        }
        /**
         * Sets the "Autonomous System Number" associated with the IP address.
         * 
         * @param asn The "Autonomous System Number".
         */
        public void setAsn(
                final Integer asn) {
            this.asn = asn;
        }
        /**
         * Returns the ISP associated with the IP address.
         * 
         * @return The ISP associated with the IP address.
         */
        public String getIsp() {
            return this.isp;
        }
        /**
         * Sets the ISP associated with the IP address.
         * 
         * @param isp The ISP.
         */
        public void setIsp(
                final String isp) {
            this.isp = isp;
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
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-08
     * @since 0.1
     * @see {@linkplain https://ipapi.com/documentation#api_response_objects}
     */
    public static class Security {

        /** If the given IP address is associated with a proxy. */
        @JsonProperty("is_proxy")
        @XmlElement(name = "is_proxy")
        private boolean proxy;
        /** The type of proxy the IP address is associated with. */
        @JsonProperty("proxy_type")
        @XmlElement(name = "proxy_type")
        private String proxyType;
        /** If the given IP address is associated with a crawler. */
        @JsonProperty("is_crawler")
        @XmlElement(name = "is_crawler")
        private boolean crawler;
        /** The name of the crawler the IP address is associated with. */
        @JsonProperty("crawler_name")
        @XmlElement(name = "crawler_name")
        private String crawlerName;
        /** The type of crawler the IP address is associated with. */
        @JsonProperty("crawler_type")
        @XmlElement(name = "crawler_type")
        private String crawlerType;
        /** If if the given IP address is associated with the anonymous Tor system. */
        @JsonProperty("is_tor")
        @XmlElement(name = "is_tor")
        private boolean tor;
        /** The type of crawler the IP address is associated with. */
        @JsonProperty("threat_level")
        @XmlElement(name = "threat_level")
        private String threatLevel;
        /** The type of crawler the IP address is associated with. */
        @JsonProperty("threat_types")
        @XmlElement(name = "threat_types")
        private String[] threatTypes;

        public Security() {
            super();
        }
        public Security(
                final @NotNull Security copy) {
            super();
            this.proxy = copy.proxy;
            this.proxyType = copy.proxyType;
            this.crawler = copy.crawler;
            this.crawlerName = copy.crawlerName;
            this.crawlerType = copy.crawlerType;
            this.tor = copy.tor;
            this.threatLevel = copy.threatLevel;
            this.threatTypes = copy.threatTypes == null ? null : Arrays.copyOf(copy.threatTypes, copy.threatTypes.length);
        }
        public boolean isProxy() {
            return this.proxy;
        }
        public void setProxy(
                final boolean proxy) {
            this.proxy = proxy;
        }
        public String getProxyType() {
            return this.proxyType;
        }
        public void setProxyType(
                final String proxyType) {
            this.proxyType = proxyType;
        }
        public boolean isCrawler() {
            return this.crawler;
        }
        public void setCrawler(
                final boolean crawler) {
            this.crawler = crawler;
        }
        public String getCrawlerName() {
            return this.crawlerName;
        }
        public void setCrawlerName(
                final String crawlerName) {
            this.crawlerName = crawlerName;
        }
        public String getCrawlerType() {
            return this.crawlerType;
        }
        public void setCrawlerType(
                final String crawlerType) {
            this.crawlerType = crawlerType;
        }
        public boolean isTor() {
            return this.tor;
        }
        public void setTor(
                final boolean tor) {
            this.tor = tor;
        }
        public String getThreatLevel() {
            return this.threatLevel;
        }
        public void setThreatLevel(
                final String threatLevel) {
            this.threatLevel = threatLevel;
        }
        public String[] getThreatTypes() {
            return this.threatTypes == null ? null : Arrays.copyOf(this.threatTypes, this.threatTypes.length);
        }
        public void setThreatTypes(
                final String[] threatTypes) {
            this.threatTypes = threatTypes == null ? null : Arrays.copyOf(threatTypes, threatTypes.length);
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
}
