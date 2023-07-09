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

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@code StatusCodes}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-07
 * @since 0.1
 * @see StatusCodes
 */
@Tag("ut")
class StatusCodesTest {

    /**
     * Test for {@link StatusCodes#isInformational(int)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("validStatusCodes")
    void testIsInformational(
            final int statusCode)
    throws Throwable {
        boolean result = StatusCodes.isInformational(statusCode);
        assertEquals(statusCode >= 100 && statusCode < 200, result);
    }

    /**
     * Test for {@link StatusCodes#isSuccess(int)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("validStatusCodes")
    void testIsSuccess(
            final int statusCode)
    throws Throwable {
        boolean result = StatusCodes.isSuccess(statusCode);
        assertEquals(statusCode >= 200 && statusCode < 300, result);
    }

    /**
     * Test for {@link StatusCodes#isRedirection(int)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("validStatusCodes")
    void testIsRedirection(
            final int statusCode)
    throws Throwable {
        boolean result = StatusCodes.isRedirection(statusCode);
        assertEquals(statusCode >= 300 && statusCode < 400, result);
    }

    /**
     * Test for {@link StatusCodes#isClientError(int)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("validStatusCodes")
    void testIsClientError(
            final int statusCode)
    throws Throwable {
        boolean result = StatusCodes.isClientError(statusCode);
        assertEquals(statusCode >= 400 && statusCode < 500, result);
    }

    /**
     * Test for {@link StatusCodes#isServerError(int)}.
     * @throws Throwable Should not happen
     */
    @ParameterizedTest
    @MethodSource("validStatusCodes")
    void testIsServerError(
            final int statusCode)
    throws Throwable {
        boolean result = StatusCodes.isServerError(statusCode);
        assertEquals(statusCode >= 500 && statusCode < 600, result);
    }

    private static IntStream validStatusCodes() {
        return IntStream.range(0, 700);
    }
}
