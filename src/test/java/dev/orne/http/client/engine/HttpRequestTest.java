package dev.orne.http.client.engine;

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

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.http.ContentType;
import dev.orne.http.client.body.HttpRequestBodyProducer;
import dev.orne.http.client.engine.HttpRequest.BodyProducer;

/**
 * Unit tests for {@code HttpRequest}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 * @see HttpRequest
 */
@Tag("ut")
class HttpRequestTest {

    /**
     * Test for {@link HttpRequest#setBody(HttpRequestBodyProducer)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSetBody_Producer()
    throws Throwable {
        final HttpRequestBodyProducer producer = mock(HttpRequestBodyProducer.class);
        final HttpRequest request = mock(HttpRequest.class);
        willCallRealMethod().given(request).setBody(producer);
        request.setBody(producer);
        then(producer).should().generate(request);
        then(producer).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link HttpRequest#setBody(ContentType, BodyProducer)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testSetBody_Content_Producer()
    throws Throwable {
        final ContentType contentType = mock(ContentType.class);
        final BodyProducer producer = mock(BodyProducer.class);
        final HttpRequest request = mock(HttpRequest.class);
        willCallRealMethod().given(request).setBody(contentType, producer);
        request.setBody(contentType, producer);
        then(request).should().setBody(contentType, producer);
        then(request).should().setBody(contentType, -1, producer);
        then(request).shouldHaveNoMoreInteractions();
    }
}
