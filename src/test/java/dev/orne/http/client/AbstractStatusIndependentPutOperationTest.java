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

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPut;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code AbstractStatusIndependentPutOperation}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see AbstractStatusIndependentPutOperation
 */
@Tag("ut")
class AbstractStatusIndependentPutOperationTest
extends AbstractStatusIndependentOperationTest {

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected AbstractStatusIndependentPutOperation<Object, Object, Object> createOperation() {
        return spy(AbstractStatusIndependentPutOperation.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected HttpRequest createMockHttpRequest() {
        return mock(HttpPut.class);
    }

    /**
     * Creates a mock {@code HttpEntity} valid for tested operation.
     * 
     * @return The created mock {@code HttpEntity}
     */
    protected HttpEntity createMockEntity() {
        return mock(HttpEntity.class);
    }

    /**
     * Test for {@link AbstractStatusIndependentPutOperation#createRequest(URI, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testCreateRequest()
    throws Throwable {
        final AbstractStatusIndependentPutOperation<Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final URI requestURI = URI.create("http://example.org/some/path");
        final HttpEntity entity = createMockEntity();
        willReturn(entity).given(operation).createEntity(params);
        final HttpPut result = operation.createRequest(requestURI, params);
        assertNotNull(result);
        assertNotNull(result.getURI());
        assertEquals(requestURI, result.getURI());
        assertNotNull(result.getEntity());
        assertSame(entity, result.getEntity());
        then(operation).should(times(1)).createEntity(params);
    }

    /**
     * Test for {@link AbstractStatusIndependentPutOperation#createRequest(URI, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testCreateRequestCreateEntityFail()
    throws Throwable {
        final AbstractStatusIndependentPutOperation<Object, Object, Object> operation = createOperation();
        final Object params = new Object();
        final URI requestURI = URI.create("http://example.org/some/path");
        final HttpClientException mockException = new HttpClientException();
        willThrow(mockException).given(operation).createEntity(params);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.createRequest(requestURI, params);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).createEntity(params);
    }
}
