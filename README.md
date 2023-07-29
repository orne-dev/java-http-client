# Orne HTTP service client

Utilities for implementation of HTTP based service clients.

## Status

[![License][status.license.badge]][status.license]
[![Latest version][status.maven.badge]][status.maven]
[![Javadoc][status.javadoc.badge]][javadoc]
[![Maven site][status.site.badge]][site]

| Latest Release | Develop |
| :------------: | :-------------: |
| [![Build Status][status.latest.ci.badge]][status.latest.ci] | [![Build Status][status.dev.ci.badge]][status.dev.ci] |
| [![Coverage][status.latest.cov.badge]][status.latest.cov] | [![Coverage][status.dev.cov.badge]][status.dev.cov] |

## Usage

The binaries can be obtained from [Maven Central][status.maven] with the
`dev.orne.test:generators` coordinates:

```xml
<dependency>
  <groupId>dev.orne</groupId>
  <artifactId>http-client</artifactId>
  <version>0.1.0</version>
</dependency>
```

## Further information

For further information refer to the [Javadoc][javadoc]
and [Maven Site][site].

[site]: https://orne-dev.github.io/java-http-client/
[javadoc]: https://javadoc.io/doc/dev.orne/http-client
[status.license]: http://www.gnu.org/licenses/gpl-3.0.txt
[status.license.badge]: https://img.shields.io/github/license/orne-dev/java-http-client
[status.maven]: https://search.maven.org/artifact/dev.orne/http-client
[status.maven.badge]: https://img.shields.io/maven-central/v/dev.orne/http-client.svg?label=Maven%20Central
[status.javadoc.badge]: https://javadoc.io/badge2/dev.orne/http-client/javadoc.svg
[status.site.badge]: https://img.shields.io/website?url=https%3A%2F%2Forne-dev.github.io%2Fjava-http-client%2F
[status.latest.ci]: https://github.com/orne-dev/java-http-client/actions/workflows/release.yml
[status.latest.ci.badge]: https://github.com/orne-dev/java-http-client/actions/workflows/release.yml/badge.svg?branch=master
[status.latest.cov]: https://sonarcloud.io/dashboard?id=orne-dev_java-http-client
[status.latest.cov.badge]: https://sonarcloud.io/api/project_badges/measure?project=orne-dev_java-http-client&metric=coverage
[status.dev.ci]: https://github.com/orne-dev/java-http-client/actions/workflows/build.yml
[status.dev.ci.badge]: https://github.com/orne-dev/java-http-client/actions/workflows/build.yml/badge.svg?branch=develop
[status.dev.cov]: https://sonarcloud.io/dashboard?id=orne-dev_java-http-client&branch=develop
[status.dev.cov.badge]: https://sonarcloud.io/api/project_badges/measure?project=orne-dev_java-http-client&metric=coverage&branch=develop
