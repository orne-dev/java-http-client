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

import org.apache.http.HttpEntity;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code VoidResponseHandler}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see VoidResponseHandler
 */
@Tag("ut")
public class VoidResponseHandlerTest {

    /**
     * Test for {@link VoidResponseHandler#handleEntity(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testHandleEntityNullEntity()
    throws Throwable {
        final VoidResponseHandler handler = new VoidResponseHandler();
        final Object result = handler.handleEntity(null);
        assertNull(result);
    }

    /**
     * Test for {@link VoidResponseHandler#handleEntity(HttpEntity)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testHandleEntity()
    throws Throwable {
        final VoidResponseHandler handler = new VoidResponseHandler();
        final HttpEntity mockEntity = mock(HttpEntity.class);
        final Object result = handler.handleEntity(mockEntity);
        assertNull(result);
        then(mockEntity).shouldHaveNoInteractions();
    }
}
