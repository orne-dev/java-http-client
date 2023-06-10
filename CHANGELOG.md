# :package: 0.1.0

01. :gift: Added HTTP utility classes
    01. Added class `dev.orne.http.Methods`
    01. Added class `dev.orne.http.Headers`
    01. Added class `dev.orne.http.MediaTypes`
    01. Added class `dev.orne.http.ContentType`
    01. Added class `dev.orne.http.StatusCodes`
01. :gift: Added client API
    01. Added interface `dev.orne.http.client.HttpServiceClient`
    01. Added exception `dev.orne.http.client.HttpClientException`
    01. Added interface `dev.orne.http.client.StatusIndependentOperation`
    01. Added interface `dev.orne.http.client.StatedHttpServiceClient`
    01. Added interface `dev.orne.http.client.StatusDependentOperation`
    01. Added interface `dev.orne.http.client.StatusInitOperation`
    01. Added interface `dev.orne.http.client.AuthenticableClientStatus`
    01. Added exception `dev.orne.http.client.AuthenticationException`
    01. Added exception `dev.orne.http.client.AuthenticationRequiredException`
    01. Added interface `dev.orne.http.client.AuthenticatedOperation`
    01. Added exception `dev.orne.http.client.AuthenticationFailedException`
    01. Added interface `dev.orne.http.client.AuthenticationOperation`
    01. Added interface `dev.orne.http.client.AuthenticableHttpServiceClient`
    01. Added exception `dev.orne.http.client.AuthenticationExpiredException`
    01. Added exception `dev.orne.http.client.CredentialsInvalidException`
    01. Added exception `dev.orne.http.client.CredentialsNotStoredException`
01. :gift: Added HTTP client engine abstraction system.
    01. Added interface `dev.orne.http.client.engine.HttpRequestHeadersSupplier`
    01. Added interface `dev.orne.http.client.engine.HttpRequestBodySupplier`
    01. Added interface `dev.orne.http.client.engine.HttpResponseHeadersSupplier`
    01. Added interface `dev.orne.http.client.engine.HttpResponseBodySupplier`
    01. Added interface `dev.orne.http.client.engine.HttpResponseHandler`
    01. Added interface `dev.orne.http.client.engine.HttpClientEngine`
    01. Added class `dev.orne.http.client.engine.AbstractHttpClientEngine`
    01. Added Apache HTTP Client 4.x based HTTP client engine
        01. Added class `dev.orne.http.client.engine.apache.ApacheHttpClientEngine`
01. :gift: Added default client implementations
    01. Added class `dev.orne.http.client.BaseHttpServiceClient`
    01. Added class `dev.orne.http.client.BaseStatedHttpServiceClient`
    01. Added class `dev.orne.http.client.BaseAuthenticableHttpServiceClient`
01. :gift: Added default operation implementations
    01. Added class `dev.orne.http.client.AbstractHttpServiceOperation`
    01. Added class `dev.orne.http.client.AbstractStatusIndependentOperation`
    01. Added class `dev.orne.http.client.AbstractStatusIndependentGetOperation`
    01. Added class `dev.orne.http.client.AbstractStatusIndependentPutOperation`
    01. Added class `dev.orne.http.client.AbstractStatusIndependentUrlEncodedPutOperation`
    01. Added class `dev.orne.http.client.AbstractStatusIndependentPostOperation`
    01. Added class `dev.orne.http.client.AbstractStatusIndependentUrlEncodedPostOperation`
    01. Added class `dev.orne.http.client.AbstractStatusIndependentDeleteOperation`
    01. Added class `dev.orne.http.client.AbstractStatusDependentOperation`
    01. Added class `dev.orne.http.client.AbstractStatusDependentGetOperation`
    01. Added class `dev.orne.http.client.AbstractStatusDependentPutOperation`
    01. Added class `dev.orne.http.client.AbstractStatusDependentUrlEncodedPutOperation`
    01. Added class `dev.orne.http.client.AbstractStatusDependentPostOperation`
    01. Added class `dev.orne.http.client.AbstractStatusDependentUrlEncodedPostOperation`
    01. Added class `dev.orne.http.client.AbstractStatusDependentDeleteOperation`
01. :gift: Added default response handlers implementations
    01. Added class `dev.orne.http.client.VoidResponseHandler`
    01. Added class `dev.orne.http.client.AbstractMimeTypeResponseHandler`
    01. Added class `dev.orne.http.client.JaxbXMLResponseHandler`
    01. Added class `dev.orne.http.client.JacksonJSONResponseHandler`
