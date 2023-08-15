package dev.orne.http.client.example;

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

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.http.client.engine.apache.ApacheHttpClientEngine;

/**
 * Integration test of {@code IpifyClient}.
 * <p>
 * Requires access to {@linkplain https://www.ipify.org/}.
 * 
 * @see IpifyClient
 * @see {@linkplain https://www.ipify.org/}
 */
@Tag("it")
class IpifyClientApacheIT {

    /** The HTTP service client. */
    private static IpifyClient client;

    /**
     * Creates the HTTP service client.
     */
    @BeforeAll
    static void createClient() {
        client = new IpifyClient(new ApacheHttpClientEngine());
    }

    /**
     * Closes the HTTP service client.
     */
    @AfterAll
    static void closeClient() throws IOException {
        client.close();
    }

    /**
     * Tests the plain text operation call.
     * 
     * @throws Throwable Should not happen.
     */
    @Test
    void testGetIp()
    throws Throwable {
        final CompletableFuture<Void> waitAsync = new CompletableFuture<>();
        client.getPublicIp().whenComplete((ip, ex) -> {
            try {
                assertNull(ex);
                assertNotNull(ip);
                waitAsync.complete(null);
            } catch (AssertionError e) {
                waitAsync.completeExceptionally(e);
            }
        });
        assertDoesNotThrow(() -> waitAsync.get());
    }

    /**
     * Tests the JSON operation call.
     * 
     * @throws Throwable Should not happen.
     */
    @Test
    void testGetIpJson()
    throws Throwable {
        final CompletableFuture<Void> waitAsync = new CompletableFuture<>();
        client.getPublicIpAsJson().whenComplete((ip, ex) -> {
            try {
                assertNull(ex);
                assertNotNull(ip);
                waitAsync.complete(null);
            } catch (AssertionError e) {
                waitAsync.completeExceptionally(e);
            }
        });
        assertDoesNotThrow(() -> waitAsync.get());
    }
}
