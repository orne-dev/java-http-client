# :package: 0.1.0

01. :gift: Added HTTP utility classes.
    01. Added class `dev.orne.http.Methods`
    01. Added class `dev.orne.http.Headers`
    01. Added class `dev.orne.http.MediaTypes`
    01. Added class `dev.orne.http.ContentType`
    01. Added class `dev.orne.http.StatusCodes`
01. :gift: Added HTTP client engine abstraction system.
    01. Added interface `dev.orne.http.client.engine.HttpRequest`
    01. Added interface `dev.orne.http.client.engine.HttpResponse`
    01. Added interface `dev.orne.http.client.engine.HttpResponseBody`
    01. Added interface `dev.orne.http.client.engine.HttpRequestCustomizer`
    01. Added interface `dev.orne.http.client.engine.HttpResponseHandler`
    01. Added interface `dev.orne.http.client.engine.HttpClientEngine`
01. :gift: Added client cookies API.
    01. Added interface `dev.orne.http.client.Cookie`
    01. Added interface `dev.orne.http.client.CookieStore`
    01. Added class `dev.orne.http.client.DefaultCookie`
01. :gift: Added client API.
    01. Added exception `dev.orne.http.client.HttpClientException`
    01. Added exception `dev.orne.http.client.HttpRequestBodyGenerationException`
    01. Added exception `dev.orne.http.client.HttpResponseStatusException`
    01. Added exception `dev.orne.http.client.HttpResponseHandlingException`
    01. Added exception `dev.orne.http.client.AuthenticationException`
    01. Added exception `dev.orne.http.client.AuthenticationRequiredException`
    01. Added exception `dev.orne.http.client.AuthenticationExpiredException`
    01. Added exception `dev.orne.http.client.CredentialsNotStoredException`
    01. Added exception `dev.orne.http.client.AuthenticationFailedException`
    01. Added exception `dev.orne.http.client.CredentialsInvalidException`
    01. Added exception `dev.orne.http.client.HttpResponseBodyParsingException`
    01. Added exception `dev.orne.http.client.UnsupportedContentTypeException`
    01. Added interface `dev.orne.http.client.HttpServiceClient`
    01. Added interface `dev.orne.http.client.StatedHttpServiceClient`
    01. Added interface `dev.orne.http.client.AuthenticableClientStatus`
    01. Added interface `dev.orne.http.client.AuthenticableHttpServiceClient`
    01. Added interface `dev.orne.http.client.AuthenticationAutoRenewalPolicy`
01. :gift: Added service operations API.
    01. Added interface `dev.orne.http.client.op.StatusIndependentOperation`
    01. Added interface `dev.orne.http.client.op.StatusDependentOperation`
    01. Added interface `dev.orne.http.client.op.StatusInitOperation`
    01. Added interface `dev.orne.http.client.op.AuthenticationOperation`
    01. Added interface `dev.orne.http.client.op.AuthenticatedOperation`
    01. Added interface `dev.orne.http.client.op.OperationResponseHandler`
01. :gift: Added HTTP request and response body handling API.
    01. Added interface `dev.orne.http.client.body.HttpRequestBodyProducer`
    01. Added interface `dev.orne.http.client.body.HttpResponseBodyParser`
    01. Added interface `dev.orne.http.client.body.HttpResponseBodyMediaTypeParser`
01. :gift: Added base client implementations.
    01. Added class `dev.orne.http.client.FutureUtils`
    01. Added class `dev.orne.http.client.BaseHttpServiceClient`
    01. Added class `dev.orne.http.client.BaseStatedHttpServiceClient`
    01. Added class `dev.orne.http.client.BaseAuthenticableHttpServiceClient`
01. Added Apache HTTP Client 5.x based HTTP client engine.
    01. Added class `dev.orne.http.client.engine.apache.ApacheCookie`
    01. Added class `dev.orne.http.client.engine.apache.ApacheCookieStore`
    01. Added class `dev.orne.http.client.engine.apache.ApacheHttpRequest`
    01. Added class `dev.orne.http.client.engine.apache.ApacheHttpResponse`
    01. Added class `dev.orne.http.client.engine.apache.ApacheHttpResponseBody`
    01. Added class `dev.orne.http.client.engine.apache.ApacheHttpClientEngine`
01. :gift: Added base abstract operation implementations.
    01. Added class `dev.orne.http.client.AbstractHttpServiceOperation`
    01. Added class `dev.orne.http.client.AbstractStatusIndependentOperation`
    01. Added class `dev.orne.http.client.AbstractStatusDependentOperation`
01. :gift: Added default HTTP request and response body handlers implementations.
    01. Added interface `dev.orne.http.client.body.WWWFormHttpResponseBodyParser`
    01. Added class `dev.orne.http.client.body.WWWFormHttpBody`
    01. Added interface `dev.orne.http.client.body.XmlHttpResponseBodyParser`
    01. Added class `dev.orne.http.client.body.JaxbHttpBody`
    01. Added interface `dev.orne.http.client.body.JsonHttpResponseBodyParser`
    01. Added class `dev.orne.http.client.body.JacksonHttpBody`
    01. Added class `dev.orne.http.client.body.DelegatedHttpRequestBodyParser`
01. :gift: Added testing utilities.
    01. Added class `dev.orne.http.ContentTypeGenerator`
    01. Added class `dev.orne.http.client.DefaultCookieGenerator`
    01. Added class `dev.orne.http.client.engine.apache.ApacheCookieGenerator`
