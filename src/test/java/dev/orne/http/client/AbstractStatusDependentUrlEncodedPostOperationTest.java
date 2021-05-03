package dev.orne.http.client;

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
 * Unit tests for {@code AbstractStatusDependentUrlEncodedPostOperation}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see AbstractStatusDependentUrlEncodedPostOperation
 */
@Tag("ut")
class AbstractStatusDependentUrlEncodedPostOperationTest
extends AbstractStatusDependentPostOperationTest {

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected AbstractStatusDependentUrlEncodedPostOperation<Object, Object, Object, Object> createOperation() {
        return spy(AbstractStatusDependentUrlEncodedPostOperation.class);
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
     * Test for {@link AbstractStatusDependentUrlEncodedPostOperation#createEntity(Object, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testCreateEntity()
    throws Throwable {
        final AbstractStatusDependentUrlEncodedPostOperation<Object, Object, Object, Object> operation =
                createOperation();
        final Object params = new Object();
        final Object status = new Object();
        final List<NameValuePair> entityParams = Arrays.asList(
                new BasicNameValuePair("oneEntityParam", "oneValue"),
                new BasicNameValuePair("anotherEntityParam", "anotherValue"));
        final Charset entityCharset = StandardCharsets.US_ASCII;
        final ContentType expectedContentType = ContentType.create(
                URLEncodedUtils.CONTENT_TYPE,
                entityCharset);
        final int expectedContentLength = URLEncodedUtils.format(entityParams, entityCharset).length();
        willReturn(entityParams).given(operation).createEntityParams(params, status);
        willReturn(entityCharset).given(operation).getEntityCharset(params, status);
        final UrlEncodedFormEntity result = operation.createEntity(params, status);
        assertNotNull(result);
        assertNotNull(result.getContentType());
        assertEquals(expectedContentType.toString(), result.getContentType().getValue());
        assertEquals(expectedContentLength, result.getContentLength());
        then(operation).should(times(1)).createEntityParams(params, status);
        then(operation).should(times(1)).getEntityCharset(params, status);
    }

    /**
     * Test for {@link AbstractStatusDependentUrlEncodedPostOperation#createEntity(Object, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testCreateEntityCreateEntityParamsFail()
    throws Throwable {
        final AbstractStatusDependentUrlEncodedPostOperation<Object, Object, Object, Object> operation =
                createOperation();
        final Object params = new Object();
        final Object status = new Object();
        final Charset entityCharset = StandardCharsets.US_ASCII;
        final HttpClientException mockException = new HttpClientException();
        willThrow(mockException).given(operation).createEntityParams(params, status);
        willReturn(entityCharset).given(operation).getEntityCharset(params, status);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.createEntity(params, status);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).createEntityParams(params, status);
    }

    /**
     * Test for {@link AbstractStatusDependentUrlEncodedPostOperation#createEntity(Object, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testCreateEntityGetEntityCharsetFail()
    throws Throwable {
        final AbstractStatusDependentUrlEncodedPostOperation<Object, Object, Object, Object> operation =
                createOperation();
        final Object params = new Object();
        final Object status = new Object();
        final List<NameValuePair> entityParams = Arrays.asList(
                new BasicNameValuePair("oneEntityParam", "oneValue"),
                new BasicNameValuePair("anotherEntityParam", "anotherValue"));
        final HttpClientException mockException = new HttpClientException();
        willReturn(entityParams).given(operation).createEntityParams(params, status);
        willThrow(mockException).given(operation).getEntityCharset(params, status);
        final HttpClientException result = assertThrows(HttpClientException.class, () -> {
            operation.createEntity(params, status);
        });
        assertNotNull(result);
        assertSame(mockException, result);
        then(operation).should(times(1)).getEntityCharset(params, status);
    }
    /**
     * Test for {@link AbstractStatusDependentUrlEncodedPostOperation#getEntityCharset(Object, Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    void testGetEntityCharset()
    throws Throwable {
        final AbstractStatusDependentUrlEncodedPostOperation<Object, Object, Object, Object> operation =
                createOperation();
        final Object params = new Object();
        final Object status = new Object();
        final Charset expectedResult = StandardCharsets.UTF_8;
        final Charset result = operation.getEntityCharset(params, status);
        assertNotNull(result);
        assertSame(expectedResult, result);
    }
}
