# Defining operations

Operations are abstractions of requests to the HTTP end point.

An operation consists on 3 or 4 components.

```mermaid
classDiagram
    class StatusIndependentOperation~P, R~ {
        <<Interface>>
        +execute(P params, HttpServiceClient client) R
    }
```

## Status independent operations

```mermaid
classDiagram
    class StatusIndependentOperation~P, R~ {
        <<Interface>>
        +execute(P params, HttpServiceClient client) R
    }
```

```mermaid
classDiagram
    class StatusIndependentOperation~P, R~ {
        <<Interface>>
        +execute(P params, HttpServiceClient client) R
    }
    class AbstractHttpServiceOperation~P, E, R, C extends HttpServiceClient~ {
        <<Abstract>>
        -BodyHandler~responseHandler~ responseHandler
        #resolveRequestURI(URI requestURI, @NotNull C client) URI
        #executeHttpRequest(P params, HttpRequest request, C client) R
        #getResponseHandler() BodyHandler~E~
        #createResponseHandler() BodyHandler~E~
        #processResponseEntity(P params, C client, HttpRequest request, HttpResponse<E> response, E responseEntity) R
        #processException(P params, C client, HttpRequest request, HttpResponse<E> response, Exception exception) HttpClientException
        #getLogger() Logger
    }
    class AbstractStatusIndependentOperation~P, E, R~ {
        <<abstract>>
        #URI resolveRequestURI(URI requestURI, C client)
    }
    StatusIndependentOperation <|.. AbstractStatusIndependentOperation
```


## Status dependent operations

```mermaid
classDiagram
    class StatusDependentOperation~P, R, S~ {
        <<Interface>>
        +execute(P params, StatedHttpServiceClient~S~ client) R
    }
```

### Status initialization operations

One special type of state independent operation are the state initialization
operations.

```mermaid
classDiagram
    class StatusIndependentOperation~P, R~ {
        <<Interface>>
    }
    class StatusInitOperation~S~ {
        <<Interface>>
        +execute(Void params, HttpServiceClient client) S
    }
    StatusIndependentOperation <|-- StatusInitOperation
```

### Authentication operations

One special type of state dependent operation are the authentication operations.

```mermaid
classDiagram
    class StatusDependentOperation~P, R, S~ {
        <<Interface>>
        +execute(P params, StatedHttpServiceClient~S~ client) R
    }
    class AuthenticationOperation~P, R, S extends AuthenticableClientStatus~ {
        <<Interface>>
        +R execute(P params, StatedHttpServiceClient~S~ client)
    }
    StatusDependentOperation <|.. AuthenticationOperation
```

## Implementing operations

### Base operation

```mermaid
classDiagram
    class AbstractHttpServiceOperation~P, E, R, C extends HttpServiceClient~ {
        <<Abstract>>
        -BodyHandler~E~ responseHandler
        -Logger logger
        #resolveRequestURI(URI requestURI, C client) URI
        #executeHttpRequest(P params, HttpRequest request, C client) R
        #getResponseHandler() BodyHandler~E~
        #createResponseHandler() BodyHandler~E~
        #processResponseEntity(P params, C client, HttpRequest request, HttpResponse~E~ response, E responseEntity) E
        #processException(P params, C client, HttpRequest request, HttpResponse~E~ response, Exception exception) HttpClientException
        #getLogger() Logger
    }
```

### Implementing status independent operations

```mermaid
classDiagram
    class StatusIndependentOperation~P, R~ {
        <<Interface>>
        +execute(P params, HttpServiceClient client) R
    }
    class AbstractHttpServiceOperation~P, E, R, C extends HttpServiceClient~ {
        <<Abstract>>
        -BodyHandler~E~ responseHandler
        -Logger logger
        #resolveRequestURI(URI requestURI, C client) URI
        #executeHttpRequest(P params, HttpRequest request, C client) R
        #getResponseHandler() BodyHandler~E~
        #createResponseHandler() BodyHandler~E~
        #processResponseEntity(P params, C client, HttpRequest request, HttpResponse~E~ response, E responseEntity) E
        #processException(P params, C client, HttpRequest request, HttpResponse~E~ response, Exception exception) HttpClientException
        #getLogger() Logger
    }
    class AbstractStatusIndependentOperation~P, E, R~ {
        <<Abstract>>
        #getRequestURI(P params) URI
        #configureRequest(HttpRequest.Builder builder, P params)
    }
    StatusIndependentOperation <|.. AbstractStatusIndependentOperation
    AbstractHttpServiceOperation <|-- AbstractStatusIndependentOperation
    class AbstractStatusIndependentGetOperation~P, E, R~ {
        <<Abstract>>
    }
    AbstractStatusIndependentOperation <|-- AbstractStatusIndependentGetOperation
    class AbstractStatusIndependentPostOperation~P, E, R~ {
        <<Abstract>>
        #createEntityPublisher(P params) HttpRequest.BodyPublisher
    }
    AbstractStatusIndependentOperation <|-- AbstractStatusIndependentPostOperation
    class AbstractStatusIndependentPutOperation~P, E, R~ {
        <<Abstract>>
        #createEntityPublisher(P params) HttpRequest.BodyPublisher
    }
    AbstractStatusIndependentOperation <|-- AbstractStatusIndependentPutOperation
    class AbstractStatusIndependentDeleteOperation~P, E, R~ {
        <<Abstract>>
    }
    AbstractStatusIndependentOperation <|-- AbstractStatusIndependentDeleteOperation
    class AbstractStatusIndependentUrlEncodedPostOperation~P, E, R~ {
        <<Abstract>>
        #getEntityCharset(P params) Charset
        #createEntityParams(P params) Map<String, String>
    }
    AbstractStatusIndependentPostOperation <|-- AbstractStatusIndependentUrlEncodedPostOperation
    class AbstractStatusIndependentUrlEncodedPutOperation~P, E, R~ {
        <<Abstract>>
        #getEntityCharset(P params) Charset
        #createEntityParams(P params) Map<String, String>
    }
    AbstractStatusIndependentPutOperation <|-- AbstractStatusIndependentUrlEncodedPutOperation
```


### Implementing status dependent operations

```mermaid
classDiagram
    class StatusDependentOperation~P, R, S~ {
        <<Interface>>
        +execute(P params, StatedHttpServiceClient~S~ client) R
    }
    class AbstractHttpServiceOperation~P, E, R, C extends HttpServiceClient~ {
        <<Abstract>>
        -BodyHandler~E~ responseHandler
        -Logger logger
        #resolveRequestURI(URI requestURI, C client) URI
        #executeHttpRequest(P params, HttpRequest request, C client) R
        #getResponseHandler() BodyHandler~E~
        #createResponseHandler() BodyHandler~E~
        #processResponseEntity(P params, C client, HttpRequest request, HttpResponse~E~ response, E responseEntity) E
        #processException(P params, C client, HttpRequest request, HttpResponse~E~ response, Exception exception) HttpClientException
        #getLogger() Logger
    }
    class AbstractStatusDependentOperation~P, E, R, S~ {
        <<Abstract>>
        #getRequestURI(P params, S status) URI
        #configureRequest(HttpRequest.Builder builder, P params, S status)
    }
    StatusDependentOperation <|.. AbstractStatusDependentOperation
    AbstractHttpServiceOperation <|-- AbstractStatusDependentOperation
    class AbstractStatusDependentGetOperation~P, E, R, S~ {
        <<Abstract>>
    }
    AbstractStatusDependentOperation <|-- AbstractStatusDependentGetOperation
    class AbstractStatusDependentPostOperation~P, E, R, S~ {
        <<Abstract>>
        #createEntityPublisher(P params, S status) HttpRequest.BodyPublisher
    }
    AbstractStatusDependentOperation <|-- AbstractStatusDependentPostOperation
    class AbstractStatusDependentPutOperation~P, E, R, S~ {
        <<Abstract>>
        #createEntityPublisher(P params, S status) HttpRequest.BodyPublisher
    }
    AbstractStatusDependentOperation <|-- AbstractStatusDependentPutOperation
    class AbstractStatusDependentDeleteOperation~P, E, R, S~ {
        <<Abstract>>
    }
    AbstractStatusDependentOperation <|-- AbstractStatusDependentDeleteOperation
    class AbstractStatusDependentUrlEncodedPostOperation~P, E, R, S~ {
        <<Abstract>>
        #getEntityCharset(P params, S status) Charset
        #createEntityParams(P params, S status) Map<String, String>
    }
    AbstractStatusDependentPostOperation <|-- AbstractStatusDependentUrlEncodedPostOperation
    class AbstractStatusDependentUrlEncodedPutOperation~P, E, R, S~ {
        <<Abstract>>
        #getEntityCharset(P params, S status) Charset
        #createEntityParams(P params, S status) Map<String, String>
    }
    AbstractStatusDependentPutOperation <|-- AbstractStatusDependentUrlEncodedPutOperation
```

