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
import java.util.function.Function;
import java.util.function.Supplier;

import javax.validation.constraints.NotNull;

/**
 * Interface for authentication auto renewal policies to be applied
 * when the service client has auto renewal enabled and stored credentials
 * and the execution of one or more operations have failed because the
 * previous authentication has expired.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public interface AuthenticationAutoRenewalPolicy {

    /**
     * Applies the authentication auto renewal policy before retrying the
     * execution of the requested operations.
     * 
     * @param <R> The operation result type.
     * @param <R> The client status type.
     * @param authExecuter The authentication operation execution lamda.
     * @param opExecuter The post authenticated operation(s) execution lamda.
     * @param result The result future to update the results into.
     */
    <R, S extends AuthenticableClientStatus> void apply(
            @NotNull Supplier<CompletableFuture<? extends S>> authExecuter,
            @NotNull Function<S, CompletableFuture<R>> opExecuter,
            @NotNull CompletableFuture<R> result);
}
