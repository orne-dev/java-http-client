# HTTP Service clients

An HTTP service client is a client that performs a defined set of operations
over a HTTP service.

Two type of clients are defined: stateless clients (`HttpServiceClient`)
and stated clients (`StatedHttpServiceClient`).

```mermaid
classDiagram
    class Closeable {
        <<Interface>>
        +close()
    }
    class HttpServiceClient {
        <<Interface>>
        +getBaseURI() URI
        +getCookieHandler() CookieHandler
        +getClient() HttpClient
        +execute(StatusIndependentOperation, P params) R
    }
    class StatusIndependentOperation~P, R~ {
        <<Interface>>
        +execute(P params, HttpServiceClient client) R
    }
    Closeable <|-- HttpServiceClient
    StatusIndependentOperation <.. HttpServiceClient
    StatusIndependentOperation ..> HttpServiceClient
```

## Stated HTTP service clients

Stated HTTP service clients keep track of a state between operation executions.

```mermaid
classDiagram
    class StatusIndependentOperation~P, R~ {
        <<Interface>>
        +execute(P params, HttpServiceClient client) R
    }
    class StatusDependentOperation~P, R, S~ {
        <<Interface>>
        +execute(P params, StatedHttpServiceClient~S~ client) R
    }
    class Closeable {
        <<Interface>>
        +close()
    }
    class HttpServiceClient {
        <<Interface>>
        +getBaseURI() URI
        +getCookieHandler() CookieHandler
        +getClient() HttpClient
        +execute(StatusIndependentOperation, P params) R
    }
    HttpServiceClient <|-- StatedHttpServiceClient
    Closeable <|-- HttpServiceClient
    StatusIndependentOperation <.. HttpServiceClient
    StatusIndependentOperation ..> HttpServiceClient
    class StatedHttpServiceClient~S~ {
        <<Interface>>
        +ensureInitialized() S
        +initializeStatus() S
        +getStatus() S
        +resetStatus()
        +execute(StatusDependentOperation operation, P params) R
    }
    StatusDependentOperation <.. StatedHttpServiceClient
    StatusDependentOperation ..> StatedHttpServiceClient
```

Note that stated clients can perform state independent operations.
One special type of state independent operation is the state initialization
operation, which returns the client's initial state. If this state
initialization depends on a service call or not is left to the operation
implementation

### Authenticable HTTP service clients

```mermaid
classDiagram
    class StatusIndependentOperation~P, R~ {
        <<Interface>>
        +execute(P params, HttpServiceClient client) R
    }
    class StatusDependentOperation~P, R, S~ {
        <<Interface>>
        +execute(P params, StatedHttpServiceClient~S~ client) R
    }
    class Closeable {
        <<Interface>>
        +close()
    }
    class HttpServiceClient {
        <<Interface>>
        +getBaseURI() URI
        +getCookieHandler() CookieHandler
        +getClient() HttpClient
        +execute(StatusIndependentOperation, P params) R
    }
    HttpServiceClient <|-- StatedHttpServiceClient
    Closeable <|-- HttpServiceClient
    StatusIndependentOperation <.. HttpServiceClient
    StatusIndependentOperation ..> HttpServiceClient
    class StatedHttpServiceClient~S~ {
        <<Interface>>
        +ensureInitialized() S
        +initializeStatus() S
        +getStatus() S
        +resetStatus()
        +execute(StatusDependentOperation operation, P params) R
    }
    StatusDependentOperation <.. StatedHttpServiceClient
    StatusDependentOperation ..> StatedHttpServiceClient
    class AuthenticableHttpServiceClient~S, C~ {
        <<Interface>>
        +isCredentialsStoringEnabled() boolean
        +setCredentialsStoringEnabled(boolean enabled)
        +isAuthenticationAutoRenewalEnabled() boolean
        +setAuthenticationAutoRenewalEnabled(boolean enabled)
        +ensureAuthenticated()
        +authenticate(C credentials)
        +authenticate()
    }
    StatedHttpServiceClient <|-- AuthenticableHttpServiceClient
```

## Implementation

```mermaid
classDiagram
    class HttpServiceClient {
        <<Interface>>
        +getBaseURI() URI
        +getCookieHandler() CookieHandler
        +getClient() HttpClient
        +execute(StatusIndependentOperation, P params) R
    }
    class StatusIndependentOperation~P, R~ {
        <<Interface>>
        +execute(P params, HttpServiceClient client) R
    }
    StatusIndependentOperation <.. HttpServiceClient
    class BaseHttpServiceClient {
        -URI baseURI
        -CookieHandler cookieHandler
        -HttpClient client
        -Logger logger
        +BaseHttpServiceClient(URI baseURI)
        #BaseHttpServiceClient(URI baseURI, CookieHandler cookieHandler, HttpClient client)
        #configureHttpClient(HttpClient.Builder builder)
        #getLogger() Logger
    }
    HttpServiceClient <|.. BaseHttpServiceClient
```

### Stated client implementation

```mermaid
classDiagram
    class StatusIndependentOperation~P, R~ {
        <<Interface>>
        +execute(P params, HttpServiceClient client) R
    }
    class StatusDependentOperation~P, R, S~ {
        <<Interface>>
        +execute(P params, StatedHttpServiceClient~S~ client) R
    }
    class HttpServiceClient {
        <<Interface>>
        +getBaseURI() URI
        +getCookieHandler() CookieHandler
        +getClient() HttpClient
        +execute(StatusIndependentOperation, P params) R
    }
    StatusIndependentOperation <.. HttpServiceClient
    class StatedHttpServiceClient~S~ {
        <<Interface>>
        +ensureInitialized() S
        +initializeStatus() S
        +getStatus() S
        +resetStatus()
        +execute(StatusDependentOperation operation, P params) R
    }
    HttpServiceClient <|-- StatedHttpServiceClient
    StatusDependentOperation <.. StatedHttpServiceClient
    class AuthenticableHttpServiceClient~S, C~ {
        <<Interface>>
        +isCredentialsStoringEnabled() boolean
        +setCredentialsStoringEnabled(boolean enabled)
        +isAuthenticationAutoRenewalEnabled() boolean
        +setAuthenticationAutoRenewalEnabled(boolean enabled)
        +ensureAuthenticated()
        +authenticate(C credentials)
        +authenticate()
    }
    StatedHttpServiceClient <|-- AuthenticableHttpServiceClient
    class BaseHttpServiceClient {
        -URI baseURI
        -CookieHandler cookieHandler
        -HttpClient client
        -Logger logger
        +BaseHttpServiceClient(URI baseURI)
        #BaseHttpServiceClient(URI baseURI, CookieHandler cookieHandler, HttpClient client)
        #configureHttpClient(HttpClient.Builder builder)
        #getLogger() Logger
    }
    HttpServiceClient <|.. BaseHttpServiceClient
    class BaseStatedHttpServiceClient~S~ {
        -StatusInitOperation~S~ statusInitOperation
        -S status
        +BaseStatedHttpServiceClient(URI baseURI, StatusInitOperation~S~ statusInitOperation)
        #BaseStatedHttpServiceClient(URI baseURI, CookieHandler cookieHandler, HttpClient client, StatusInitOperation~S~ statusInitOperation)
        #getStatusInitOperation() StatusInitOperation~S~

    }
    BaseHttpServiceClient <|-- BaseStatedHttpServiceClient
    StatedHttpServiceClient <|.. BaseStatedHttpServiceClient
    class StatusInitOperation~S~ {
        <<Interface>>
        +execute(Void params, HttpServiceClient client) S
    }
    StatusIndependentOperation <|-- StatusInitOperation
    StatusInitOperation <.. BaseStatedHttpServiceClient
```

### Authenticable client implementation

```mermaid
classDiagram
    class StatusIndependentOperation~P, R~ {
        <<Interface>>
        +execute(P params, HttpServiceClient client) R
    }
    class StatusDependentOperation~P, R, S~ {
        <<Interface>>
        +execute(P params, StatedHttpServiceClient~S~ client) R
    }
    class AuthenticationOperation~P, R, S extends AuthenticableClientStatus~ {
        <<Interface>>
        +R execute(P params, StatedHttpServiceClient~S~ client)
    }
    StatusDependentOperation <|.. AuthenticationOperation
    class HttpServiceClient {
        <<Interface>>
        +getBaseURI() URI
        +getCookieHandler() CookieHandler
        +getClient() HttpClient
        +execute(StatusIndependentOperation, P params) R
    }
    StatusIndependentOperation <.. HttpServiceClient
    class StatedHttpServiceClient~S~ {
        <<Interface>>
        +ensureInitialized() S
        +initializeStatus() S
        +getStatus() S
        +resetStatus()
        +execute(StatusDependentOperation operation, P params) R
    }
    HttpServiceClient <|-- StatedHttpServiceClient
    StatusDependentOperation <.. StatedHttpServiceClient
    class AuthenticableHttpServiceClient~S, C~ {
        <<Interface>>
        +isCredentialsStoringEnabled() boolean
        +setCredentialsStoringEnabled(boolean enabled)
        +isAuthenticationAutoRenewalEnabled() boolean
        +setAuthenticationAutoRenewalEnabled(boolean enabled)
        +ensureAuthenticated()
        +authenticate(C credentials)
        +authenticate()
    }
    StatedHttpServiceClient <|-- AuthenticableHttpServiceClient
    class BaseHttpServiceClient {
        -URI baseURI
        -CookieHandler cookieHandler
        -HttpClient client
        -Logger logger
        +BaseHttpServiceClient(URI baseURI)
        #BaseHttpServiceClient(URI baseURI, CookieHandler cookieHandler, HttpClient client)
        #configureHttpClient(HttpClient.Builder builder)
        #getLogger() Logger
    }
    HttpServiceClient <|.. BaseHttpServiceClient
    class BaseStatedHttpServiceClient~S~ {
        -StatusInitOperation~S~ statusInitOperation
        -S status
        +BaseStatedHttpServiceClient(URI baseURI, StatusInitOperation~S~ statusInitOperation)
        #BaseStatedHttpServiceClient(URI baseURI, CookieHandler cookieHandler, HttpClient client, StatusInitOperation~S~ statusInitOperation)
        #getStatusInitOperation() StatusInitOperation~S~

    }
    BaseHttpServiceClient <|-- BaseStatedHttpServiceClient
    StatedHttpServiceClient <|.. BaseStatedHttpServiceClient
    class StatusInitOperation~S~ {
        <<Interface>>
        +execute(Void params, HttpServiceClient client) S
    }
    StatusIndependentOperation <|-- StatusInitOperation
    StatusInitOperation <.. BaseStatedHttpServiceClient
    class AuthenticableClientStatus {
        <<Interface>>
        +isAuthenticated() boolean
        +resetAuthentication()
    }
    class BaseAuthenticableHttpServiceClient~S extends AuthenticableClientStatus, C~ {
        -AuthenticationOperation authenticationOperation
        -boolean credentialsStoringEnabled
        -C storedCredentials
        -boolean authenticationAutoRenewalEnabled
        +BaseAuthenticableHttpServiceClient(URI baseURI, StatusInitOperation statusInitOperation, AuthenticationOperation authenticationOperation)
        #BaseAuthenticableHttpServiceClient(URI baseURI, CookieHandler cookieHandler, HttpClient client, StatusInitOperation statusInitOperation, AuthenticationOperation authenticationOperation)
        #getAuthenticationOperation() AuthenticationOperation
        #boolean hasStoredCredentials()
        #setStoredCredentials(C credentials)
    }
    BaseStatedHttpServiceClient <|-- BaseAuthenticableHttpServiceClient
    AuthenticableHttpServiceClient <|.. BaseAuthenticableHttpServiceClient
    AuthenticableClientStatus <.. BaseAuthenticableHttpServiceClient
    AuthenticationOperation <.. BaseAuthenticableHttpServiceClient
```
