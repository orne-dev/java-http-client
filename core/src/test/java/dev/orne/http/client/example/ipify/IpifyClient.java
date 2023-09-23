package dev.orne.http.client.example.ipify;

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

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.CompletionStage;

import javax.validation.constraints.NotNull;

import dev.orne.http.client.BaseHttpServiceClient;
import dev.orne.http.client.engine.HttpClientEngine;

/**
 * Example {@code Ipify} client.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-08
 * @since 0.1
 * @see {@linkplain https://www.ipify.org/}
 */
public class IpifyClient
extends BaseHttpServiceClient {

    public static final String API_URI = "https://api64.ipify.org";

    public IpifyClient(
            final @NotNull HttpClientEngine engine) {
        this(engine, URI.create(API_URI));
    }

    public IpifyClient(
            @NotNull HttpClientEngine engine,
            @NotNull URI baseURI) {
        super(engine, baseURI);
    }

    public IpifyClient(
            @NotNull HttpClientEngine engine,
            @NotNull URL baseURL)
    throws URISyntaxException {
        super(engine, baseURL);
    }

    public @NotNull CompletionStage<String> getPublicIp() {
        return this.execute(IpifyGetPublicIp.INSTANCE, null);
    }

    public @NotNull CompletionStage<String> getPublicIpAsJson() {
        return this.execute(IpifyGetPublicJsonIp.INSTANCE, null);
    }
}
