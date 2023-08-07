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

import static org.junit.jupiter.api.Assertions.*;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.http.client.HttpRequestBodyGenerationException;
import dev.orne.http.client.HttpResponseBodyParsingException;
import dev.orne.test.rnd.Generators;

/**
 * Unit tests for {@code UrlEncodedUtils}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-08
 * @since 0.1
 * @see UrlEncodedUtils
 */
@Tag("ut")

class UrlEncodedUtilsTest {

    /**
     * Test for {@link UrlEncodedUtils#format(String, Pair...)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFormat_Varargs()
    throws Throwable {
        final Charset charset = randomEncodingCharset();
        final String encoding = charset.name();
        final List<Pair<String, String>> pairs = generatePairs();
        @SuppressWarnings("unchecked")
        final Pair<String, String>[] args = (Pair<String, String>[]) pairs.toArray(new Pair[0]);
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.format((String) null, args);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.format(encoding, (Pair<String, String>[]) null);
        });
        final StringBuilder expected = new StringBuilder()
                .append(URLEncoder.encode(pairs.get(0).getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pairs.get(0).getValue(), encoding));
        for (int i = 1; i < pairs.size(); i++) {
            expected.append(UrlEncodedUtils.PAIR_SEPARATOR)
                .append(URLEncoder.encode(pairs.get(i).getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pairs.get(i).getValue(), encoding));
        }
        final String result = UrlEncodedUtils.format(encoding, args);
        assertEquals(expected.toString(), result);
    }

    /**
     * Test for {@link UrlEncodedUtils#format(Charset, Pair...)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFormat_Varargs_Charset()
    throws Throwable {
        final Charset charset = randomEncodingCharset();
        final String encoding = charset.name();
        final List<Pair<String, String>> pairs = generatePairs();
        @SuppressWarnings("unchecked")
        final Pair<String, String>[] args = (Pair<String, String>[]) pairs.toArray(new Pair[0]);
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.format((Charset) null, pairs);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.format(charset, (Pair<String, String>[]) null);
        });
        final StringBuilder expected = new StringBuilder()
                .append(URLEncoder.encode(pairs.get(0).getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pairs.get(0).getValue(), encoding));
        for (int i = 1; i < pairs.size(); i++) {
            expected.append(UrlEncodedUtils.PAIR_SEPARATOR)
                .append(URLEncoder.encode(pairs.get(i).getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pairs.get(i).getValue(), encoding));
        }
        final String result = UrlEncodedUtils.format(charset, args);
        assertEquals(expected.toString(), result);
    }

    /**
     * Test for {@link UrlEncodedUtils#format(String, Collection)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFormat_Collection()
    throws Throwable {
        final Charset charset = randomEncodingCharset();
        final String encoding = charset.name();
        final List<Pair<String, String>> pairs = generatePairs();
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.format((String) null, pairs);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.format(encoding, (Collection<Pair<String, String>>) null);
        });
        final StringBuilder expected = new StringBuilder()
                .append(URLEncoder.encode(pairs.get(0).getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pairs.get(0).getValue(), encoding));
        for (int i = 1; i < pairs.size(); i++) {
            expected.append(UrlEncodedUtils.PAIR_SEPARATOR)
                .append(URLEncoder.encode(pairs.get(i).getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pairs.get(i).getValue(), encoding));
        }
        final String result = UrlEncodedUtils.format(encoding, pairs);
        assertEquals(expected.toString(), result);
    }

    /**
     * Test for {@link UrlEncodedUtils#format(Charset, Collection)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFormat_Collection_Charset()
    throws Throwable {
        final Charset charset = randomEncodingCharset();
        final String encoding = charset.name();
        final List<Pair<String, String>> pairs = generatePairs();
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.format((Charset) null, pairs);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.format(charset, (Collection<Pair<String, String>>) null);
        });
        final StringBuilder expected = new StringBuilder()
                .append(URLEncoder.encode(pairs.get(0).getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pairs.get(0).getValue(), encoding));
        for (int i = 1; i < pairs.size(); i++) {
            expected.append(UrlEncodedUtils.PAIR_SEPARATOR)
                .append(URLEncoder.encode(pairs.get(i).getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pairs.get(i).getValue(), encoding));
        }
        final String result = UrlEncodedUtils.format(charset, pairs);
        assertEquals(expected.toString(), result);
    }

    /**
     * Test for {@link UrlEncodedUtils#format(String, Map)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFormat_Map()
    throws Throwable {
        final Charset charset = randomEncodingCharset();
        final String encoding = charset.name();
        final List<Pair<String, String>> pairs = generatePairs();
        final Map<String, String> map = generatePairsMap(pairs);
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.format((String) null, map);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.format(encoding, (Map<String, String>) null);
        });
        final StringBuilder expected = new StringBuilder()
                .append(URLEncoder.encode(pairs.get(0).getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pairs.get(0).getValue(), encoding));
        for (int i = 1; i < pairs.size(); i++) {
            expected.append(UrlEncodedUtils.PAIR_SEPARATOR)
                .append(URLEncoder.encode(pairs.get(i).getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pairs.get(i).getValue(), encoding));
        }
        final String result = UrlEncodedUtils.format(encoding, map);
        assertEquals(expected.toString(), result);
    }

    /**
     * Test for {@link UrlEncodedUtils#format(Charset, Map)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFormat_Map_Charset()
    throws Throwable {
        final Charset charset = randomEncodingCharset();
        final String encoding = charset.name();
        final List<Pair<String, String>> pairs = generatePairs();
        final Map<String, String> map = generatePairsMap(pairs);
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.format((Charset) null, map);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.format(charset, (Map<String, String>) null);
        });
        final StringBuilder expected = new StringBuilder()
                .append(URLEncoder.encode(pairs.get(0).getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pairs.get(0).getValue(), encoding));
        for (int i = 1; i < pairs.size(); i++) {
            expected.append(UrlEncodedUtils.PAIR_SEPARATOR)
                .append(URLEncoder.encode(pairs.get(i).getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pairs.get(i).getValue(), encoding));
        }
        final String result = UrlEncodedUtils.format(charset, map);
        assertEquals(expected.toString(), result);
    }

    /**
     * Test for {@link UrlEncodedUtils#formatPair(String, Pair)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFormatPair()
    throws Throwable {
        final Charset charset = randomEncodingCharset();
        final String encoding = charset.name();
        final Pair<String, String> pair = generatePair();
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair((String) null, pair);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(encoding, (Pair<String, String>) null);
        });
        final String expected = new StringBuilder()
                .append(URLEncoder.encode(pair.getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pair.getValue(), encoding))
                .toString();
        final String result = UrlEncodedUtils.formatPair(encoding, pair);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link UrlEncodedUtils#formatPair(Charset, Pair)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFormatPair_Charset()
    throws Throwable {
        final Charset charset = randomEncodingCharset();
        final String encoding = charset.name();
        final Pair<String, String> pair = generatePair();
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair((Charset) null, pair);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(charset, (Pair<String, String>) null);
        });
        final String expected = new StringBuilder()
                .append(URLEncoder.encode(pair.getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pair.getValue(), encoding))
                .toString();
        final String result = UrlEncodedUtils.formatPair(charset, pair);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link UrlEncodedUtils#formatPair(String, String, String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFormatPair_Parts()
    throws Throwable {
        final Charset charset = randomEncodingCharset();
        final String encoding = charset.name();
        final Pair<String, String> pair = generatePair();
        final String key = pair.getKey();
        final String value = pair.getValue();
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair((String) null, key, value);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(encoding, null, value);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(encoding, key, null);
        });
        assertThrows(HttpRequestBodyGenerationException.class, () -> {
            UrlEncodedUtils.formatPair("", key, value);
        });
        final Charset decodeOnlyCharset = findDecodeOnlyCharset();
        assertThrows(HttpRequestBodyGenerationException.class, () -> {
            UrlEncodedUtils.formatPair(decodeOnlyCharset.name(), key, value);
        });
        final String expected = new StringBuilder()
                .append(URLEncoder.encode(pair.getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pair.getValue(), encoding))
                .toString();
        final String result = UrlEncodedUtils.formatPair(encoding, key, value);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link UrlEncodedUtils#formatPair(Charset, String, String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFormatPair_Parts_Charset()
    throws Throwable {
        final Charset charset = randomEncodingCharset();
        final String encoding = charset.name();
        final Pair<String, String> pair = generatePair();
        final String key = pair.getKey();
        final String value = pair.getValue();
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair((Charset) null, key, value);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(charset, null, value);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(charset, key, null);
        });
        final Charset decodeOnlyCharset = findDecodeOnlyCharset();
        assertThrows(HttpRequestBodyGenerationException.class, () -> {
            UrlEncodedUtils.formatPair(decodeOnlyCharset.name(), key, value);
        });
        final String expected = new StringBuilder()
                .append(URLEncoder.encode(pair.getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pair.getValue(), encoding))
                .toString();
        final String result = UrlEncodedUtils.formatPair(charset, key, value);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link UrlEncodedUtils#formatPair(String, Map.Entry)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFormatPair_MapEntry()
    throws Throwable {
        final Charset charset = randomEncodingCharset();
        final String encoding = charset.name();
        final Pair<String, String> pair = generatePair();
        final Map.Entry<String, String> entry = Collections.singletonMap(pair.getKey(), pair.getValue())
                .entrySet().iterator().next();
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair((String) null, entry);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(encoding, (Map.Entry<String, String>) null);
        });
        final String expected = new StringBuilder()
                .append(URLEncoder.encode(pair.getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pair.getValue(), encoding))
                .toString();
        final String result = UrlEncodedUtils.formatPair(encoding, entry);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link UrlEncodedUtils#formatPair(Charset, Map.Entry)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFormatPair_MapEntry_Charset()
    throws Throwable {
        final Charset charset = randomEncodingCharset();
        final String encoding = charset.name();
        final Pair<String, String> pair = generatePair();
        final Map.Entry<String, String> entry = Collections.singletonMap(pair.getKey(), pair.getValue())
                .entrySet().iterator().next();
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair((Charset) null, entry);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(charset, (Map.Entry<String, String>) null);
        });
        final String expected = new StringBuilder()
                .append(URLEncoder.encode(pair.getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pair.getValue(), encoding))
                .toString();
        final String result = UrlEncodedUtils.formatPair(charset, entry);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link UrlEncodedUtils#formatPair(StringBuilder, String, Pair)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFormatPair_Buffer()
    throws Throwable {
        final Charset charset = randomEncodingCharset();
        final String encoding = charset.name();
        final StringBuilder buffer = new StringBuilder();
        final Pair<String, String> pair = generatePair();
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(null, encoding, pair);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(buffer, (String) null, pair);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(buffer, encoding, (Pair<String, String>) null);
        });
        final String expected = new StringBuilder()
                .append(URLEncoder.encode(pair.getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pair.getValue(), encoding))
                .toString();
        UrlEncodedUtils.formatPair(buffer, encoding, pair);
        assertEquals(expected, buffer.toString());
    }

    /**
     * Test for {@link UrlEncodedUtils#formatPair(StringBuilder, Charset, Pair)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFormatPair_Charset_Buffer()
    throws Throwable {
        final Charset charset = randomEncodingCharset();
        final String encoding = charset.name();
        final StringBuilder buffer = new StringBuilder();
        final Pair<String, String> pair = generatePair();
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(null, charset, pair);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(buffer, (Charset) null, pair);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(buffer, charset, (Pair<String, String>) null);
        });
        final String expected = new StringBuilder()
                .append(URLEncoder.encode(pair.getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pair.getValue(), encoding))
                .toString();
        UrlEncodedUtils.formatPair(buffer, charset, pair);
        assertEquals(expected, buffer.toString());
    }

    /**
     * Test for {@link UrlEncodedUtils#formatPair(StringBuilder, String, String, String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFormatPair_Parts_Buffer()
    throws Throwable {
        final Charset charset = randomEncodingCharset();
        final String encoding = charset.name();
        final StringBuilder buffer = new StringBuilder();
        final Pair<String, String> pair = generatePair();
        final String key = pair.getKey();
        final String value = pair.getValue();
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(null, encoding, key, value);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(buffer, (String) null, key, value);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(buffer, encoding, null, value);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(buffer, encoding, key, null);
        });
        final String expected = new StringBuilder()
                .append(URLEncoder.encode(pair.getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pair.getValue(), encoding))
                .toString();
        UrlEncodedUtils.formatPair(buffer, encoding, key, pair.getValue());
        assertEquals(expected, buffer.toString());
    }

    /**
     * Test for {@link UrlEncodedUtils#formatPair(StringBuilder, Charset, String, String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFormatPair_Parts_Charset_Buffer()
    throws Throwable {
        final Charset charset = randomEncodingCharset();
        final String encoding = charset.name();
        final StringBuilder buffer = new StringBuilder();
        final Pair<String, String> pair = generatePair();
        final String key = pair.getKey();
        final String value = pair.getValue();
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(null, charset, key, value);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(buffer, (Charset) null, key, value);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(buffer, charset, null, value);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(buffer, charset, key, null);
        });
        final String expected = new StringBuilder()
                .append(URLEncoder.encode(pair.getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pair.getValue(), encoding))
                .toString();
        UrlEncodedUtils.formatPair(buffer, charset, pair.getKey(), pair.getValue());
        assertEquals(expected, buffer.toString());
    }

    /**
     * Test for {@link UrlEncodedUtils#formatPair(StringBuilder, String, Map.Entry)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFormatPair_MapEntry_Buffer()
    throws Throwable {
        final Charset charset = randomEncodingCharset();
        final String encoding = charset.name();
        final StringBuilder buffer = new StringBuilder();
        final Pair<String, String> pair = generatePair();
        final Map.Entry<String, String> entry = Collections.singletonMap(pair.getKey(), pair.getValue())
                .entrySet().iterator().next();
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(null, encoding, entry);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(buffer, (String) null, entry);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(buffer, encoding, (Map.Entry<String, String>) null);
        });
        final String expected = new StringBuilder()
                .append(URLEncoder.encode(pair.getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pair.getValue(), encoding))
                .toString();
        UrlEncodedUtils.formatPair(buffer, encoding, entry);
        assertEquals(expected, buffer.toString());
    }

    /**
     * Test for {@link UrlEncodedUtils#formatPair(StringBuilder, Charset, Map.Entry)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testFormatPair_MapEntry_Charset_Buffer()
    throws Throwable {
        final Charset charset = randomEncodingCharset();
        final String encoding = charset.name();
        final StringBuilder buffer = new StringBuilder();
        final Pair<String, String> pair = generatePair();
        final Map.Entry<String, String> entry = Collections.singletonMap(pair.getKey(), pair.getValue())
                .entrySet().iterator().next();
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(null, charset, entry);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(buffer, (Charset) null, entry);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.formatPair(buffer, charset, (Map.Entry<String, String>) null);
        });
        final String expected = new StringBuilder()
                .append(URLEncoder.encode(pair.getKey(), encoding))
                .append(UrlEncodedUtils.VALUE_SEPARATOR)
                .append(URLEncoder.encode(pair.getValue(), encoding))
                .toString();
        UrlEncodedUtils.formatPair(buffer, charset, entry);
        assertEquals(expected, buffer.toString());
    }

    /**
     * Test for {@link UrlEncodedUtils#parse(String, String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParse()
    throws Throwable {
        final Charset charset = StandardCharsets.UTF_8;
        final String encoding = charset.name();
        final List<Pair<String, String>> pairs = generatePairs();
        final String encoded = UrlEncodedUtils.format(encoding, pairs);
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.parsePair((String) null, encoded);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.parsePair(encoding, null);
        });
        final Collection<Pair<String, String>> result = UrlEncodedUtils.parse(encoding, encoded);
        assertEquals(pairs, result);
    }

    /**
     * Test for {@link UrlEncodedUtils#parse(Charset, String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParse_Charset()
    throws Throwable {
        final Charset charset = StandardCharsets.UTF_8;
        final String encoding = charset.name();
        final List<Pair<String, String>> pairs = generatePairs();
        final String encoded = UrlEncodedUtils.format(encoding, pairs);
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.parsePair((Charset) null, encoded);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.parsePair(charset, null);
        });
        final Collection<Pair<String, String>> result = UrlEncodedUtils.parse(charset, encoded);
        assertEquals(pairs, result);
    }

    /**
     * Test for {@link UrlEncodedUtils#parsePair(String, String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParsePair()
    throws Throwable {
        final Charset charset = StandardCharsets.UTF_8;
        final String encoding = charset.name();
        final Pair<String, String> pair = generatePair();
        final String encoded = UrlEncodedUtils.formatPair(encoding, pair);
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.parsePair((String) null, encoded);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.parsePair(encoding, null);
        });
        assertThrows(HttpResponseBodyParsingException.class, () -> {
            UrlEncodedUtils.parsePair("", "valid=value");
        });
        assertThrows(HttpResponseBodyParsingException.class, () -> {
            UrlEncodedUtils.parsePair(encoding, "when");
        });
        assertThrows(HttpResponseBodyParsingException.class, () -> {
            UrlEncodedUtils.parsePair(encoding, "when=invalid=value");
        });
        final Pair<String, String> result = UrlEncodedUtils.parsePair(encoding, encoded);
        assertEquals(pair, result);
    }

    /**
     * Test for {@link UrlEncodedUtils#parsePair(Charset, String)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testParsePair_Charset()
    throws Throwable {
        final Charset charset = StandardCharsets.UTF_8;
        final String encoding = charset.name();
        final Pair<String, String> pair = generatePair();
        final String encoded = UrlEncodedUtils.formatPair(encoding, pair);
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.parsePair((Charset) null, encoded);
        });
        assertThrows(NullPointerException.class, () -> {
            UrlEncodedUtils.parsePair(charset, null);
        });
        assertThrows(HttpResponseBodyParsingException.class, () -> {
            UrlEncodedUtils.parsePair("", "valid=value");
        });
        assertThrows(HttpResponseBodyParsingException.class, () -> {
            UrlEncodedUtils.parsePair(charset, "when");
        });
        assertThrows(HttpResponseBodyParsingException.class, () -> {
            UrlEncodedUtils.parsePair(charset, "when=invalid=value");
        });
        final Pair<String, String> result = UrlEncodedUtils.parsePair(charset, encoded);
        assertEquals(pair, result);
    }

    static Map<String, String> generatePairsMap(
            final List<Pair<String, String>> pairs) {
        final Map<String, String> result = new LinkedHashMap<>(pairs.size());
        for (final Pair<String, String> pair : pairs) {
            result.put(pair.getKey(), pair.getValue());
        }
        return result;
    }
    static List<Pair<String, String>> generatePairs() {
        int count = RandomUtils.nextInt(1, 10);
        final List<Pair<String, String>> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(generatePair());
        }
        return result;
    }
    static Pair<String, String> generatePair() {
        return Pair.of(
                RandomStringUtils.random(10),
                RandomStringUtils.random(10));
    }
    static Charset randomEncodingCharset() {
        Charset candidate = Generators.randomValue(Charset.class);
        while (!candidate.canEncode()) {
            candidate = Generators.randomValue(Charset.class);
        }
        return candidate;
    }
    static Charset findDecodeOnlyCharset() {
        Map<String, Charset> charsets = Charset.availableCharsets();
        for (Charset candidate : charsets.values()) {
            if (!candidate.canEncode()) {
                return candidate;
            }
        }
        return null;
    }
}
