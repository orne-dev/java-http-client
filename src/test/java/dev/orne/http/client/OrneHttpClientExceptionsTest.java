package dev.orne.http.client;

/*-
 * #%L
 * Orne HTTP Client
 * %%
 * Copyright (C) 2020 - 2023 Orne Developments
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

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for library exceptions.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 * @see HttpClientException
 * @see HttpRequestBodyGenerationException
 * @see HttpResponseHandlingException
 * @see HttpResponseStatusException
 * @see HttpResponseBodyParsingException
 * @see UnsupportedContentTypeException
 * @see AuthenticationException
 * @see AuthenticationRequiredException
 * @see AuthenticationFailedException
 * @see AuthenticationExpiredException
 * @see CredentialsInvalidException
 * @see CredentialsNotStoredException
 */
@Tag("ut")
class OrneHttpClientExceptionsTest {

    /** Message for exception testing. */
    private static final String TEST_MESSAGE = "Test message";
    /** Cause for exception testing. */
    private static final Throwable TEST_CAUSE = new Exception();
    /** Status code for exception testing. */
    private static final int TEST_STATUS_CODE = 500;
    /** Status code reason phrase for exception testing. */
    private static final String TEST_STATUS_REASON = "Test reason";
    /** Default message for status code exception testing. */
    private static final String TEST_DEFAULT_STATUS_CODE_MESSAGE =
            String.format(
                    HttpResponseStatusException.DEFAULT_MSG_TMPL,
                    TEST_STATUS_CODE,
                    TEST_STATUS_REASON);

    /**
     * Test for {@link HttpClientException}.
     */
    @Test
    void testHttpClientException() {
        assertEmptyException(new HttpClientException());
        assertMessageException(new HttpClientException(TEST_MESSAGE));
        assertCauseException(new HttpClientException(TEST_CAUSE));
        assertFullException(new HttpClientException(TEST_MESSAGE, TEST_CAUSE));
        assertFullException(new HttpClientException(TEST_MESSAGE, TEST_CAUSE, false, false));
    }

    /**
     * Test for {@link HttpClientException#unwrapFutureException(Throwable)}.
     */
    @Test
    void testUnwrapFutureHttpClientException() {
        final HttpClientException exception = new HttpClientException();
        final Exception other = new Exception();
        final RuntimeException runtime = new RuntimeException();
        final Error error = new Error();
        HttpClientException result = HttpClientException.unwrapFutureException(exception);
        assertSame(exception, result);
        result = HttpClientException.unwrapFutureException(other);
        assertNotNull(result);
        assertSame(other, result.getCause());
        result = HttpClientException.unwrapFutureException(
                new CompletionException(exception));
        assertSame(exception, result);
        result = HttpClientException.unwrapFutureException(
                new CompletionException(new CompletionException(exception)));
        assertSame(exception, result);
        result = HttpClientException.unwrapFutureException(
                new ExecutionException(exception));
        assertSame(exception, result);
        result = HttpClientException.unwrapFutureException(
                new ExecutionException(new ExecutionException(exception)));
        assertSame(exception, result);
        Throwable thrown = assertThrows(Throwable.class, () -> HttpClientException.unwrapFutureException(runtime));
        assertSame(runtime, thrown);
        thrown = assertThrows(Throwable.class, () -> HttpClientException.unwrapFutureException(
                new ExecutionException(runtime)));
        assertSame(runtime, thrown);
        thrown = assertThrows(Throwable.class, () -> HttpClientException.unwrapFutureException(error));
        assertSame(error, thrown);
    }

    /**
     * Test for {@link HttpRequestBodyGenerationException}.
     */
    @Test
    void testHttpRequestBodyGenerationException() {
        assertEmptyException(new HttpRequestBodyGenerationException());
        assertMessageException(new HttpRequestBodyGenerationException(TEST_MESSAGE));
        assertCauseException(new HttpRequestBodyGenerationException(TEST_CAUSE));
        assertFullException(new HttpRequestBodyGenerationException(TEST_MESSAGE, TEST_CAUSE));
        assertFullException(new HttpRequestBodyGenerationException(TEST_MESSAGE, TEST_CAUSE, false, false));
    }

    /**
     * Test for {@link HttpResponseHandlingException}.
     */
    @Test
    void testHttpResponseHandlingException() {
        assertEmptyException(new HttpResponseHandlingException());
        assertMessageException(new HttpResponseHandlingException(TEST_MESSAGE));
        assertCauseException(new HttpResponseHandlingException(TEST_CAUSE));
        assertFullException(new HttpResponseHandlingException(TEST_MESSAGE, TEST_CAUSE));
        assertFullException(new HttpResponseHandlingException(TEST_MESSAGE, TEST_CAUSE, false, false));
    }

    /**
     * Test for {@link HttpResponseStatusException}.
     */
    @Test
    void testHttpResponseStatusException() {
        assertEmptyStatusCodeException(
            new HttpResponseStatusException(
                TEST_STATUS_CODE,
                TEST_STATUS_REASON));
        assertMessageStatusCodeException(
            new HttpResponseStatusException(
                TEST_STATUS_CODE,
                TEST_STATUS_REASON,
                TEST_MESSAGE));
        assertCauseStatusCodeException(
            new HttpResponseStatusException(
                TEST_STATUS_CODE,
                TEST_STATUS_REASON,
                TEST_CAUSE));
        assertFullException(
            new HttpResponseStatusException(
                TEST_STATUS_CODE,
                TEST_STATUS_REASON,
                TEST_MESSAGE,
                TEST_CAUSE));
        assertFullStatusCodeException(
            new HttpResponseStatusException(
                TEST_STATUS_CODE,
                TEST_STATUS_REASON,
                TEST_MESSAGE,
                TEST_CAUSE,
                false,
                false));
    }

    /**
     * Test for {@link HttpResponseBodyParsingException}.
     */
    @Test
    void testHttpResponseBodyParsingException() {
        assertEmptyException(new HttpResponseBodyParsingException());
        assertMessageException(new HttpResponseBodyParsingException(TEST_MESSAGE));
        assertCauseException(new HttpResponseBodyParsingException(TEST_CAUSE));
        assertFullException(new HttpResponseBodyParsingException(TEST_MESSAGE, TEST_CAUSE));
        assertFullException(new HttpResponseBodyParsingException(TEST_MESSAGE, TEST_CAUSE, false, false));
    }

    /**
     * Test for {@link UnsupportedContentTypeException}.
     */
    @Test
    void testUnsupportedContentTypeException() {
        assertEmptyException(new UnsupportedContentTypeException());
        assertMessageException(new UnsupportedContentTypeException(TEST_MESSAGE));
        assertCauseException(new UnsupportedContentTypeException(TEST_CAUSE));
        assertFullException(new UnsupportedContentTypeException(TEST_MESSAGE, TEST_CAUSE));
        assertFullException(new UnsupportedContentTypeException(TEST_MESSAGE, TEST_CAUSE, false, false));
    }

    /**
     * Test for {@link AuthenticationException}.
     */
    @Test
    void testAuthenticationException() {
        assertEmptyException(new AuthenticationException());
        assertMessageException(new AuthenticationException(TEST_MESSAGE));
        assertCauseException(new AuthenticationException(TEST_CAUSE));
        assertFullException(new AuthenticationException(TEST_MESSAGE, TEST_CAUSE));
        assertFullException(new AuthenticationException(TEST_MESSAGE, TEST_CAUSE, false, false));
        assertTrue(HttpClientException.class.isAssignableFrom(AuthenticationException.class));
    }

    /**
     * Test for {@link AuthenticationRequiredException}.
     */
    @Test
    void testAuthenticationRequiredException() {
        assertEmptyException(new AuthenticationRequiredException());
        assertMessageException(new AuthenticationRequiredException(TEST_MESSAGE));
        assertCauseException(new AuthenticationRequiredException(TEST_CAUSE));
        assertFullException(new AuthenticationRequiredException(TEST_MESSAGE, TEST_CAUSE));
        assertFullException(new AuthenticationRequiredException(TEST_MESSAGE, TEST_CAUSE, false, false));
        assertTrue(AuthenticationException.class.isAssignableFrom(AuthenticationRequiredException.class));
    }

    /**
     * Test for {@link AuthenticationFailedException}.
     */
    @Test
    void testAuthenticationFailedException() {
        assertEmptyException(new AuthenticationFailedException());
        assertMessageException(new AuthenticationFailedException(TEST_MESSAGE));
        assertCauseException(new AuthenticationFailedException(TEST_CAUSE));
        assertFullException(new AuthenticationFailedException(TEST_MESSAGE, TEST_CAUSE));
        assertFullException(new AuthenticationFailedException(TEST_MESSAGE, TEST_CAUSE, false, false));
        assertTrue(AuthenticationException.class.isAssignableFrom(AuthenticationFailedException.class));
    }

    /**
     * Test for {@link AuthenticationExpiredException}.
     */
    @Test
    void testAuthenticationExpiredException() {
        assertEmptyException(new AuthenticationExpiredException());
        assertMessageException(new AuthenticationExpiredException(TEST_MESSAGE));
        assertCauseException(new AuthenticationExpiredException(TEST_CAUSE));
        assertFullException(new AuthenticationExpiredException(TEST_MESSAGE, TEST_CAUSE));
        assertFullException(new AuthenticationExpiredException(TEST_MESSAGE, TEST_CAUSE, false, false));
        assertTrue(AuthenticationRequiredException.class.isAssignableFrom(AuthenticationExpiredException.class));
    }

    /**
     * Test for {@link CredentialsInvalidException}.
     */
    @Test
    void testCredentialsInvalidException() {
        assertEmptyException(new CredentialsInvalidException());
        assertMessageException(new CredentialsInvalidException(TEST_MESSAGE));
        assertCauseException(new CredentialsInvalidException(TEST_CAUSE));
        assertFullException(new CredentialsInvalidException(TEST_MESSAGE, TEST_CAUSE));
        assertFullException(new CredentialsInvalidException(TEST_MESSAGE, TEST_CAUSE, false, false));
        assertTrue(AuthenticationFailedException.class.isAssignableFrom(CredentialsInvalidException.class));
    }

    /**
     * Test for {@link CredentialsNotStoredException}.
     */
    @Test
    void testCredentialsNotStoredException() {
        assertEmptyException(new CredentialsNotStoredException());
        assertMessageException(new CredentialsNotStoredException(TEST_MESSAGE));
        assertCauseException(new CredentialsNotStoredException(TEST_CAUSE));
        assertFullException(new CredentialsNotStoredException(TEST_MESSAGE, TEST_CAUSE));
        assertFullException(new CredentialsNotStoredException(TEST_MESSAGE, TEST_CAUSE, false, false));
        assertTrue(AuthenticationRequiredException.class.isAssignableFrom(CredentialsNotStoredException.class));
    }

    /**
     * Asserts that exception has no message and no cause.
     * 
     * @param <T> The exception type.
     * @param exception The exception to test.
     * @return The tested exception, for extra verifications.
     */
    private <T extends Exception> @NotNull T assertEmptyException(
            final @NotNull T exception) {
        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
        return exception;
    }

    /**
     * Asserts that exception has message but no cause.
     * 
     * @param <T> The exception type.
     * @param exception The exception to test
     * @return The tested exception, for extra verifications.
     */
    private <T extends Exception> @NotNull T assertMessageException(
            final @NotNull T exception) {
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals(TEST_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
        return exception;
    }

    /**
     * Asserts that exception has cause but no message.
     * 
     * @param <T> The exception type.
     * @param exception The exception to test
     * @return The tested exception, for extra verifications.
     */
    private <T extends Exception> @NotNull T assertCauseException(
            final @NotNull T exception) {
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals(TEST_CAUSE.toString(), exception.getMessage());
        assertNotNull(exception.getCause());
        assertSame(TEST_CAUSE, exception.getCause());
        return exception;
    }

    /**
     * Asserts that exception has message and cause.
     * 
     * @param <T> The exception type.
     * @param exception The exception to test
     * @return The tested exception, for extra verifications.
     */
    private <T extends Exception> @NotNull T assertFullException(
            final @NotNull T exception) {
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals(TEST_MESSAGE, exception.getMessage());
        assertNotNull(exception.getCause());
        assertSame(TEST_CAUSE, exception.getCause());
        return exception;
    }

    /**
     * Asserts that exception has status code, status reason,
     * default message and no cause.
     * 
     * @param <T> The exception type.
     * @param exception The exception to test.
     * @return The tested exception, for extra verifications.
     */
    private <T extends HttpResponseStatusException> @NotNull T assertEmptyStatusCodeException(
            final @NotNull T exception) {
        assertNotNull(exception);
        assertEquals(TEST_STATUS_CODE, exception.getStatusCode());
        assertEquals(TEST_STATUS_REASON, exception.getStatusReason());
        assertNotNull(exception.getMessage());
        assertEquals(TEST_DEFAULT_STATUS_CODE_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
        return exception;
    }

    /**
     * Asserts that exception has status code, status reason,
     * message but no cause.
     * 
     * @param <T> The exception type.
     * @param exception The exception to test
     * @return The tested exception, for extra verifications.
     */
    private <T extends HttpResponseStatusException> @NotNull T assertMessageStatusCodeException(
            final @NotNull T exception) {
        assertNotNull(exception);
        assertEquals(TEST_STATUS_CODE, exception.getStatusCode());
        assertEquals(TEST_STATUS_REASON, exception.getStatusReason());
        assertNotNull(exception.getMessage());
        assertEquals(TEST_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
        return exception;
    }

    /**
     * Asserts that exception has status code, status reason,
     * default message and cause.
     * 
     * @param <T> The exception type.
     * @param exception The exception to test
     * @return The tested exception, for extra verifications.
     */
    private <T extends HttpResponseStatusException> @NotNull T assertCauseStatusCodeException(
            final @NotNull T exception) {
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals(TEST_DEFAULT_STATUS_CODE_MESSAGE, exception.getMessage());
        assertNotNull(exception.getCause());
        assertSame(TEST_CAUSE, exception.getCause());
        return exception;
    }

    /**
     * Asserts that exception has status code, status reason,
     * message and cause.
     * 
     * @param <T> The exception type.
     * @param exception The exception to test
     * @return The tested exception, for extra verifications.
     */
    private <T extends HttpResponseStatusException> @NotNull T assertFullStatusCodeException(
            final @NotNull T exception) {
        assertNotNull(exception);
        assertEquals(TEST_STATUS_CODE, exception.getStatusCode());
        assertEquals(TEST_STATUS_REASON, exception.getStatusReason());
        assertNotNull(exception.getMessage());
        assertEquals(TEST_MESSAGE, exception.getMessage());
        assertNotNull(exception.getCause());
        assertSame(TEST_CAUSE, exception.getCause());
        return exception;
    }
}
