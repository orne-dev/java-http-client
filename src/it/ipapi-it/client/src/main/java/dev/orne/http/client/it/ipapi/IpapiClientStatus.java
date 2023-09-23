package dev.orne.http.client.it.ipapi;

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

import dev.orne.http.client.AuthenticableClientStatus;

/**
 * Status bean for {@code IpAPI} HTTP service client.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-08
 * @since 0.1
 */
public class IpapiClientStatus
implements AuthenticableClientStatus {

    /** The API access key. */
    private String accessKey;

    /**
     * Returns the API access key.
     * 
     * @return The API access key.
     */
    public String getAccessKey() {
        return accessKey;
    }

    /**
     * Sets the API access key.
     * 
     * @param accessKey The API access key.
     */
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAuthenticated() {
        return this.accessKey != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetAuthentication() {
        this.accessKey = null;
    }
}
