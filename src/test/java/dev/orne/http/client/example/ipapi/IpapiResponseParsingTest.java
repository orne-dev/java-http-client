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

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.orne.http.client.example.ipapi.IpapiStandardOperation.Response;

/**
 * Test for IpAPI standard operation response parsing.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-08
 * @since 0.1
 * @see Response
 * @see {@linkplain https://www.ipify.org/documentation}
 */
@Tag("ipapi.ut")
class IpapiResponseParsingTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Tests the parsing of API MISSING_ACCESS_KEY error.
     * 
     * @throws Throwable Should not happen.
     */
    @Test
    void testParseJsonMissingAccessKeyError()
    throws Throwable {
        try (final InputStream input = getClass().getResourceAsStream("ipapi.error.missing_access_key.json");
                final Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8) ) {
            final Response response = MAPPER.readValue(reader, Response.class);
            final IpapiError error = assertInstanceOf(IpapiError.class, response);
            assertFalse(error.isSuccess());
            assertNotNull(error.getInfo());
            assertEquals(IpapiError.ErrorType.MISSING_ACCESS_KEY, error.getInfo().getTypeEnum());
            assertNotNull(error.getInfo().getCause());
        }
    }

    /**
     * Tests the parsing of API USAGE_LIMIT_REACHED error.
     * 
     * @throws Throwable Should not happen.
     */
    @Test
    void testParseJsonUsageLimitReachedError()
    throws Throwable {
        try (final InputStream input = getClass().getResourceAsStream("ipapi.error.usage_limit_reached.json");
                final Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8) ) {
            final Response response = MAPPER.readValue(reader, Response.class);
            final IpapiError error = assertInstanceOf(IpapiError.class, response);
            assertFalse(error.isSuccess());
            assertNotNull(error.getInfo());
            assertEquals(IpapiError.ErrorType.USAGE_LIMIT_REACHED, error.getInfo().getTypeEnum());
            assertNotNull(error.getInfo().getCause());
        }
    }

    /**
     * Tests the parsing of base standard API call JSON result.
     * 
     * @throws Throwable Should not happen.
     */
    @Test
    void testParseJsonStandardResponse()
    throws Throwable {
        try (final InputStream input = getClass().getResourceAsStream("ipapi.response.standard.json");
                final Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8) ) {
            final Response response = MAPPER.readValue(reader, Response.class);
            final IpapiResult result = assertInstanceOf(IpapiResult.class, response);
            assertBaseResponseContent(result);
            assertNull(result.getHostname());
            assertNull(result.getSecurity());
        }
    }

    /**
     * Tests the parsing of base standard API call JSON result with host name resolution.
     * 
     * @throws Throwable Should not happen.
     */
    @Test
    void testParseJsonStandardHostnameResponse()
    throws Throwable {
        try (final InputStream input = getClass().getResourceAsStream("ipapi.response.standard.hostname.json");
                final Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8) ) {
            final Response response = MAPPER.readValue(reader, Response.class);
            final IpapiResult result = assertInstanceOf(IpapiResult.class, response);
            assertBaseResponseContent(result);
            assertNotNull(result.getHostname());
            assertNull(result.getSecurity());
        }
    }

    /**
     * Tests the parsing of base standard API call JSON result with security information.
     * 
     * @throws Throwable Should not happen.
     */
    @Test
    void testParseJsonStandardSecurityResponse()
    throws Throwable {
        try (final InputStream input = getClass().getResourceAsStream("ipapi.response.standard.security.json");
                final Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8) ) {
            final Response response = MAPPER.readValue(reader, Response.class);
            final IpapiResult result = assertInstanceOf(IpapiResult.class, response);
            assertBaseResponseContent(result);
            assertNull(result.getHostname());
            assertNotNull(result.getSecurity());
            assertSecurityResponseContent(result.getSecurity());
        }
    }

    /**
     * Tests the parsing of base standard API call JSON result with host name resolution and security information.
     * 
     * @throws Throwable Should not happen.
     */
    @Test
    void testParseJsonStandardFullResponse()
    throws Throwable {
        try (final InputStream input = getClass().getResourceAsStream("ipapi.response.standard.full.json");
                final Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8) ) {
            final Response response = MAPPER.readValue(reader, Response.class);
            final IpapiResult result = assertInstanceOf(IpapiResult.class, response);
            assertBaseResponseContent(result);
            assertNotNull(result.getHostname());
            assertNotNull(result.getSecurity());
            assertSecurityResponseContent(result.getSecurity());
        }
    }

    protected void assertBaseResponseContent(
            final IpapiResult result) {
        assertNotNull(result.getIp());
        assertNotNull(result.getType());
        assertNotNull(result.getContinentCode());
        assertNotNull(result.getContinentName());
        assertNotNull(result.getCountryCode());
        assertNotNull(result.getCountryName());
        assertNotNull(result.getRegionCode());
        assertNotNull(result.getRegionName());
        assertNotNull(result.getCity());
        assertNotNull(result.getZip());
        assertNotNull(result.getLatitude());
        assertNotNull(result.getLongitude());
        assertNotNull(result.getLocation());
        assertNotNull(result.getLocation().getId());
        assertNotNull(result.getLocation().getCapital());
        assertNotNull(result.getLocation().getLanguages());
        assertEquals(1, result.getLocation().getLanguages().length);
        assertNotNull(result.getLocation().getLanguages()[0].getCode());
        assertNotNull(result.getLocation().getLanguages()[0].getName());
        assertNotNull(result.getLocation().getLanguages()[0].getNativeName());
        assertNotNull(result.getLocation().getCountryFlag());
        assertNotNull(result.getLocation().getCountryFlagEmoji());
        assertNotNull(result.getLocation().getCountryFlagEmojiUnicode());
        assertNotNull(result.getLocation().getCallingCode());
        assertFalse(result.getLocation().isEu());
        assertNotNull(result.getTimeZone());
        assertNotNull(result.getTimeZone().getId());
        assertNotNull(result.getTimeZone().getCurrentTime());
        assertNotNull(result.getTimeZone().getGmtOffset());
        assertNotNull(result.getTimeZone().getCode());
        assertTrue(result.getTimeZone().isDaylightSaving());
        assertNotNull(result.getCurrency());
        assertNotNull(result.getCurrency().getCode());
        assertNotNull(result.getCurrency().getName());
        assertNotNull(result.getCurrency().getPluralName());
        assertNotNull(result.getCurrency().getSymbol());
        assertNotNull(result.getCurrency().getNativeSymbol());
        assertNotNull(result.getConnection());
        assertNotNull(result.getConnection().getAsn());
        assertNotNull(result.getConnection().getIsp());
    }

    protected void assertSecurityResponseContent(
            final IpapiResult.Security result) {
        assertFalse(result.isProxy());
        assertNull(result.getProxyType());
        assertFalse(result.isCrawler());
        assertNull(result.getCrawlerType());
        assertNull(result.getCrawlerName());
        assertFalse(result.isTor());
        assertNotNull(result.getThreatLevel());
        assertNotNull(result.getThreatTypes());
        assertEquals(2, result.getThreatTypes().length);
        assertNotNull(result.getThreatTypes()[0]);
        assertNotNull(result.getThreatTypes()[1]);
    }
}
