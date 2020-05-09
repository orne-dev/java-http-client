package dev.orne.http.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code AbstractStatusIndependentUrlEncodedPutOperation}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @since 0.1
 * @see AbstractStatusIndependentUrlEncodedPutOperation
 */
@Tag("ut")
public class AbstractStatusIndependentUrlEncodedPutOperationTest
extends AbstractStatusIndependentPutOperationTest {

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractStatusIndependentUrlEncodedPutOperation<Object, Object, Object> createOperation() {
        return new TestStatusIndependentOperation();
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
     * Test for {@link AbstractStatusIndependentUrlEncodedPutOperation#createEntity(Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testCreateEntity()
    throws Throwable {
        final AbstractStatusIndependentUrlEncodedPutOperation<Object, Object, Object> operation =
                spy(createOperation());
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
        assertNotNull(result.getContentLength());
        assertEquals(expectedContentLength, result.getContentLength());
        then(operation).should(times(1)).createEntityParams(params);
        then(operation).should(times(1)).getEntityCharset(params);
    }

    /**
     * Test for {@link AbstractStatusIndependentUrlEncodedPutOperation#createEntity(Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testCreateEntityCreateEntityParamsFail()
    throws Throwable {
        final AbstractStatusIndependentUrlEncodedPutOperation<Object, Object, Object> operation =
                spy(createOperation());
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
     * Test for {@link AbstractStatusIndependentUrlEncodedPutOperation#createEntity(Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testCreateEntityGetEntityCharsetFail()
    throws Throwable {
        final AbstractStatusIndependentUrlEncodedPutOperation<Object, Object, Object> operation =
                spy(createOperation());
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
     * Test for {@link AbstractStatusIndependentUrlEncodedPutOperation#getEntityCharset(Object)}.
     * @throws Throwable Should not happen
     */
    @Test
    public void testGetEntityCharset()
    throws Throwable {
        final AbstractStatusIndependentUrlEncodedPutOperation<Object, Object, Object> operation =
                spy(createOperation());
        final Object params = new Object();
        final Charset expectedResult = StandardCharsets.UTF_8;
        final Charset result = operation.getEntityCharset(params);
        assertNotNull(result);
        assertSame(expectedResult, result);
    }

    /**
     * Mock implementation of {@code AbstractStatusIndependentUrlEncodedPutOperation}
     * for testing.
     */
    private static class TestStatusIndependentOperation
    extends AbstractStatusIndependentUrlEncodedPutOperation<Object, Object, Object> {

        /**
         * Mock implementation.
         * {@inheritDoc}
         */
        @Override
        protected URI getRequestURI(
                final Object params)
        throws HttpClientException {
            return null;
        }

        /**
         * Mock implementation.
         * {@inheritDoc}
         */
        @Override
        protected List<NameValuePair> createEntityParams(
                final Object params)
        throws HttpClientException {
            return null;
        }

        /**
         * Mock implementation.
         * {@inheritDoc}
         */
        @Override
        protected ResponseHandler<Object> createResponseHandler()
        throws HttpClientException {
            return null;
        }

        /**
         * Mock implementation.
         * {@inheritDoc}
         */
        @Override
        protected Object processResponseEntity(
                final Object params,
                final HttpServiceClient client,
                final HttpRequest request,
                final HttpResponse response,
                final Object responseEntity)
        throws HttpClientException {
            return null;
        }
    }
}
