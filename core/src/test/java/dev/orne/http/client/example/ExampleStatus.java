package dev.orne.http.client.example;

/*-
 * #%L
 * Orne HTTP Client
 * %%
 * Copyright (C) 2020 - 2023 Orne Developments
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

import dev.orne.http.client.AuthenticableClientStatus;

public class ExampleStatus
implements AuthenticableClientStatus {

    /** The authentication token. */
    private String authenticationToken;

    public String getAuthenticationToken() {
        return this.authenticationToken;
    }

    public void setAuthenticationToken(String token) {
        this.authenticationToken = token;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAuthenticated() {
        return this.authenticationToken != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetAuthentication() {
        this.authenticationToken = null;
    }
}
