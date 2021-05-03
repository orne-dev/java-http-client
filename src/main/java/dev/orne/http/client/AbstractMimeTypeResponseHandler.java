package dev.orne.http.client;

/*-
 * #%L
 * Orne HTTP Client
 * %%
 * Copyright (C) 2020 - 2021 Orne Developments
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

import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;

import javax.validation.constraints.NotNull;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract implementation of {@code AbstractResponseHandler} for
 * a supported mime types.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-05
 * @param <E> El HTTP response entity type
 * @since 0.1
 */
public abstract class AbstractMimeTypeResponseHandler<E>
extends AbstractResponseHandler<E> {

    /** The logger for this instance's actual class. */
    private Logger logger;

    /**
     * Returns {@code true} if the specified {@code MimeType} is supported.
     * 
     * @param mimeType The {@code MimeType} to check
     * @return If the {@code MimeType} is supported
     */
    protected abstract boolean isMimeTypeSupported(
            final @NotNull String mimeType);

    /**
     * Returns the default {@code ContentType} to use when entity
     * declares no content type.
     * 
     * @return The default {@code ContentType}
     */
    protected abstract @NotNull ContentType getDefaultContentType();

    /**
     * <p>Retrieves the {@code ContentType} from the {@code HttpEntity}
     * and verifies that the {@code MimeType} and the {@code Charset} are
     * supported.</p>
     * 
     * <p>If entity has no {@code ContentType} the result of
     * {@link #getDefaultContentType()} is returned.</p>
     * 
     * @param entity The HTTP entity
     * @return The supported content type of the entity
     * @throws IOException Is an error occurs retrieving the content type
     * or the content type is not supported
     */
    protected @NotNull ContentType getSupportedContentType(
            final @NotNull HttpEntity entity)
    throws IOException {
        final ContentType result;
        if (entity.getContentType() == null) {
            getLogger().debug("Response has no content type. Assuming default one.");
            result = getDefaultContentType();
        } else {
            try {
                result = ContentType.parse(
                        entity.getContentType().getValue());
                if (!isMimeTypeSupported(result.getMimeType())) {
                    throw new IOException(String.format(
                            "Unsupported mime type: %s",
                            result));
                }
            } catch (final ParseException pe) {
                throw new IOException(
                        "Cannot parse content type of incoming entity",
                        pe);
            } catch (final UnsupportedCharsetException uce) {
                throw new IOException(
                        "Unsupported charted in incoming entity",
                        uce);
            }
        }
        return result;
    }

    /**
     * Returns the logger for this instance's actual class.
     * 
     * @return The logger for this instance's actual class
     */
    protected @NotNull Logger getLogger() {
        synchronized (this) {
            if (this.logger == null) {
                this.logger = LoggerFactory.getLogger(getClass());
            }
            return this.logger;
        }
    }
}
