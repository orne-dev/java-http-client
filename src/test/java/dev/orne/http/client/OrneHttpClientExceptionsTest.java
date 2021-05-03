package dev.orne.http.client;

import static org.junit.jupiter.api.Assertions.*;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for library exceptions.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see HttpClientException
 * @see AuthenticationException
 * @see AuthenticationFailedException
 * @see CredentialsInvalidException
 * @see AuthenticationRequiredException
 * @see AuthenticationExpiredException
 */
@Tag("ut")
class OrneHttpClientExceptionsTest {

    /** Message for exception testing. */
    private static final String TEST_MESSAGE = "Test message";
    /** Cause for exception testing. */
    private static final Throwable TEST_CAUSE = new Exception();

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
     * @param exception The exception to test
     */
    private void assertEmptyException(
            final @NotNull HttpClientException exception) {
        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    /**
     * Asserts that exception has message but no cause.
     * 
     * @param exception The exception to test
     */
    private void assertMessageException(
            final @NotNull HttpClientException exception) {
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals(TEST_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
    }

    /**
     * Asserts that exception has cause but no message.
     * 
     * @param exception The exception to test
     */
    private void assertCauseException(
            final @NotNull HttpClientException exception) {
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals(TEST_CAUSE.toString(), exception.getMessage());
        assertNotNull(exception.getCause());
        assertSame(TEST_CAUSE, exception.getCause());
    }

    /**
     * Asserts that exception has message and cause.
     * 
     * @param exception The exception to test
     */
    private void assertFullException(
            final @NotNull HttpClientException exception) {
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals(TEST_MESSAGE, exception.getMessage());
        assertNotNull(exception.getCause());
        assertSame(TEST_CAUSE, exception.getCause());
    }
}
