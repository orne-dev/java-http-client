package dev.orne.http.client;

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

import java.util.concurrent.CompletableFuture;

import javax.validation.constraints.NotNull;

/**
 * Utility methods for {@code Future} handling.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public final class FutureUtils {

    /**
     * Private constructor.
     */
    private FutureUtils() {
        // Utility class
    }

    /**
     * Creates a completed {@code CompletableFuture} with the specified
     * failure.
     * <p>
     * Required because {@code CompletableFuture.failedFuture()} is not
     * available until Java 9.
     * 
     * @param <R> The future result type.
     * @param t The failure cause.
     * @return The completed {@code CompletableFuture}.
     */
    public static <R> @NotNull CompletableFuture<R> completableFailure(
            final @NotNull Throwable t) {
        final CompletableFuture<R> result = new CompletableFuture<>();
        result.completeExceptionally(t);
        return result;
    }
}
