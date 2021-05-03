package dev.orne.http.client;

/*-
 * #%L
 * Orne HTTP Client
 * %%
 * Copyright (C) 2020 - 2021 Orne Developments
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
import static org.mockito.BDDMockito.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code AbstractStatusIndependentUrlEncodedPostOperation}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see AbstractStatusIndependentUrlEncodedPostOperation
 */
@Tag("ut")
class AbstractStatusIndependentUrlEncodedPostOperationTest
extends AbstractStatusIndependentPostOperationTest {

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected AbstractStatusIndependentUrlEncodedPostOperation<Object, Object, Object> createOperation() {
        return spy(AbstractStatusIndependentUrlEncodedPostOperation.class);
    }

    /**
     * Creates a mock {@code UrlEncodedFormEntity} valid for tested operation.
     * 
     * @return The created mock {@code UrlEncodedFormEntity}
     */
    protected UrlEncodedFormEntity createMockEntity() {
        return mock(UrlEncodedFormEntity.class);
    }

    /**
     * Test for {@link AbstractStatusIndependentUrlEncodedPostOperation#createEntity(Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testCreateEntity()
    throws Throwable {
        final AbstractStatusIndependentUrlEncodedPostOperation<Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final List<NameValuePair> entityParams = Arrays.asList(
                new BasicNameValuePair("oneEntityParam", "oneValue"),
                new BasicNameValuePair("anotherEntityParam", "anotherValue"));
        final Charset entityCharset = StandardCharsets.US_ASCII;
        final ContentType expectedContentType = ContentType.create(
                URLEncodedUtils.CONTENT_TYPE,
                entityCharset);
        final int expectedContentLength = URLEncodedUtils.format(entityParams, entityCharset).length();
        willReturn(entityParams).given(operation).createEntityParams(params);
        willReturn(entityCharset).given(operation).getEntityCharset(params);
        final UrlEncodedFormEntity result = operation.createEntity(params);
        assertNotNull(result);
        assertNotNull(result.getContentType());
        assertEquals(expectedContentType.toString(), result.getContentType().getValue());
        assertEquals(expectedContentLength, result.getContentLength());
        then(operation).should(times(1)).createEntityParams(params);
        then(operation).should(times(1)).getEntityCharset(params);
    }

    /**
     * Test for {@link AbstractStatusIndependentUrlEncodedPostOperation#createEntity(Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testCreateEntityCreateEntityParamsFail()
    throws Throwable {
        final AbstractStatusIndependentUrlEncodedPostOperation<Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final Charset entityCharset = StandardCharsets.US_ASCII;
        final HttpClientException mockException = new HttpClientException();
        willThrow(mockException).given(operation).createEntityParams(params);
        willReturn(entityCharset).given(operation).getEntityCharset(params);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.createEntity(params);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).createEntityParams(params);
    }

    /**
     * Test for {@link AbstractStatusIndependentUrlEncodedPostOperation#createEntity(Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testCreateEntityGetEntityCharsetFail()
    throws Throwable {
        final AbstractStatusIndependentUrlEncodedPostOperation<Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final List<NameValuePair> entityParams = Arrays.asList(
                new BasicNameValuePair("oneEntityParam", "oneValue"),
                new BasicNameValuePair("anotherEntityParam", "anotherValue"));
        final HttpClientException mockException = new HttpClientException();
        willReturn(entityParams).given(operation).createEntityParams(params);
        willThrow(mockException).given(operation).getEntityCharset(params);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.createEntity(params);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).getEntityCharset(params);
    }

    /**
     * Test for {@link AbstractStatusIndependentUrlEncodedPostOperation#getEntityCharset(Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetEntityCharset()
    throws Throwable {
        final AbstractStatusIndependentUrlEncodedPostOperation<Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final Charset expectedResult = StandardCharsets.UTF_8;
        final Charset result = operation.getEntityCharset(params);
        assertNotNull(result);
        assertSame(expectedResult, result);
    }
}
