package dev.orne.http;

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

import javax.validation.constraints.NotNull;

/**
 * Constants for (some) media type codes.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-06
 * @since 0.1
 */
public final class MediaTypes {

    /**
     * Private constructor.
     */
    private MediaTypes() {
        // Utility class
    }

    /**
     * Returns {@code true} if specified media type is an application media type.
     * 
     * @param mediaType The media type to check.
     * @return {@code true} if specified media type is an application media type.
     */
    public static boolean isApplication(
            final @NotNull String mediaType) {
        return mediaType.startsWith("application/");
    }

    /**
     * Returns {@code true} if specified media type is an audio media type.
     * 
     * @param mediaType The media type to check.
     * @return {@code true} if specified media type is an audio media type.
     */
    public static boolean isAudio(
            final @NotNull String mediaType) {
        return mediaType.startsWith("audio/");
    }

    /**
     * Returns {@code true} if specified media type is a font media type.
     * 
     * @param mediaType The media type to check.
     * @return {@code true} if specified media type is a font media type.
     */
    public static boolean isFont(
            final @NotNull String mediaType) {
        return mediaType.startsWith("font/");
    }

    /**
     * Returns {@code true} if specified media type is an image media type.
     * 
     * @param mediaType The media type to check.
     * @return {@code true} if specified media type is an image media type.
     */
    public static boolean isImage(
            final @NotNull String mediaType) {
        return mediaType.startsWith("image/");
    }

    /**
     * Returns {@code true} if specified media type is a message media type.
     * 
     * @param mediaType The media type to check.
     * @return {@code true} if specified media type is a message media type.
     */
    public static boolean isMessage(
            final @NotNull String mediaType) {
        return mediaType.startsWith("message/");
    }

    /**
     * Returns {@code true} if specified media type is a model media type.
     * 
     * @param mediaType The media type to check.
     * @return {@code true} if specified media type is a model media type.
     */
    public static boolean isModel(
            final @NotNull String mediaType) {
        return mediaType.startsWith("model/");
    }

    /**
     * Returns {@code true} if specified media type is a multi-part media type.
     * 
     * @param mediaType The media type to check.
     * @return {@code true} if specified media type is a multi-part media type.
     */
    public static boolean isMultipart(
            final @NotNull String mediaType) {
        return mediaType.startsWith("multipart/");
    }

    /**
     * Returns {@code true} if specified media type is a text based media type.
     * 
     * @param mediaType The media type to check.
     * @return {@code true} if specified media type is a text based media type.
     */
    public static boolean isText(
            final @NotNull String mediaType) {
        return mediaType.startsWith("text/");
    }

    /**
     * Returns {@code true} if specified media type is a video media type.
     * 
     * @param mediaType The media type to check.
     * @return {@code true} if specified media type is a video media type.
     */
    public static boolean isVideo(
            final @NotNull String mediaType) {
        return mediaType.startsWith("video/");
    }

    /**
     * Constants for application media type codes.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-06
     * @since 0.1
     */
    public static final class Application {

        public static final String ATOM_XML = "application/atom+xml";
        public static final String CALENDAR_JSON = "application/calendar+json";
        public static final String CALENDAR_XML = "application/calendar+xml";
        public static final String DNS = "application/dns";
        public static final String EMMA = "application/emma+xml";
        public static final String EMOTIONML_XML = "application/emotionml+xml";
        public static final String FONT_SFNT = "application/font-sfnt";
        public static final String FONT_FDPFR = "application/font-tdpfr";
        public static final String FONT_WOFF = "application/font-woff";
        public static final String GEO_JSON = "application/geo+json";
        public static final String GZIP = "application/gzip";
        public static final String JAVA_ARCHIVE = "application/java-archive";
        public static final String JAVASCRIPT = "application/javascript";
        public static final String JSON = "application/json";
        public static final String JSON_PATCH = "application/json-patch+json";
        public static final String JSON_SEQ = "application/json-seq";
        public static final String MANIFEST_JSON = "application/manifest+json";
        public static final String MATHML_CONTENT_XML = "application/mathml-content+xml";
        public static final String MATHML_PRESENTATION_XML = "application/mathml-presentation+xml";
        public static final String MERGE_PATCH_JSON = "application/merge-patch+json";
        public static final String MP4 = "application/mp4";
        public static final String MPEG4 = "application/mpeg4-generic";
        public static final String NODE = "application/node";
        public static final String OAUTH_AUTHZ_REQ = "application/oauth-authz-req+jwt";
        public static final String OCTET_STREAM = "application/octet-stream";
        public static final String OGG = "application/ogg";
        public static final String PDF = "application/pdf";
        public static final String PEM_CERTIFICATE_CHAIN = "application/pem-certificate-chain";
        public static final String PGP_ENCRYPTED = "application/pgp-encrypted";
        public static final String PGP_KEYS = "application/pgp-keys";
        public static final String PGP_SIGNATURE = "application/pgp-signature";
        public static final String PKCS7_MIME = "application/pkcs7-mime";
        public static final String PKCS7_SIGNATURE = "application/pkcs7-signature";
        public static final String PKCS8 = "application/pkcs8";
        public static final String PKCS8_ENCRYPTED = "application/pkcs8-encrypted";
        public static final String PKCS10 = "application/pkcs10";
        public static final String PKCS12 = "application/pkcs12";
        public static final String POSTSCRIPT = "application/postscript";
        public static final String RFC_XML = "application/rfc+xml";
        public static final String RTF = "application/rtf";
        public static final String SGML = "application/SGML";
        public static final String SOAP_XML = "application/soap+xml";
        public static final String SQL = "application/sql";
        public static final String VCARD_JSON = "application/vcard+json";
        public static final String VCARD_XML = "application/vcard+xml";
        public static final String WASM = "application/wasm";
        public static final String X_PKI_MESSAGE = "application/x-pki-message";
        public static final String X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
        public static final String X_X509_CA_CERT = "application/x-x509-ca-cert";
        public static final String X_X509_CA_RA_CERT = "application/x-x509-ca-ra-cert";
        public static final String X_X509_NEXT_RA_CERT = "application/x-x509-next-ra-cert";
        public static final String XHTML = "application/xhtml+xml";
        public static final String XML = "application/xml";
        public static final String XML_DTD = "application/xml-dtd";
        public static final String XML_EXTERNAL_PARSED_ENTITY = "application/xml-external-parsed-entity";
        public static final String XML_PATCH = "application/xml-patch+xml";
        public static final String XMPP = "application/xmpp+xml";
        public static final String XSLT = "application/xslt+xml";
        public static final String ZIP = "application/zip";
        public static final String ZLIB = "application/zlib";

        /**
         * Private constructor.
         */
        private Application() {
            // Utility class
        }
    }

    /**
     * Constants for audio media type codes.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-06
     * @since 0.1
     */
    public static final class Audio {

        public static final String MP4 = "audio/mp4";
        public static final String MPEG = "audio/mpeg";
        public static final String MPEG4 = "audio/mpeg4-generic";
        public static final String OGG = "audio/ogg";

        /**
         * Private constructor.
         */
        private Audio() {
            // Utility class
        }
    }

    /**
     * Constants for font media type codes.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-06
     * @since 0.1
     */
    public static final class Font {

        public static final String COLLECTION = "font/collection";
        public static final String OTF = "font/otf";
        public static final String SFNT = "font/sfnt";
        public static final String TTF = "font/ttf";
        public static final String WOFF = "font/woff";
        public static final String WOFF2 = "font/woff2";

        /**
         * Private constructor.
         */
        private Font() {
            // Utility class
        }
    }

    /**
     * Constants for image media type codes.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-06
     * @since 0.1
     */
    public static final class Image {

        public static final String BMP = "image/bmp";
        public static final String JPEG = "image/jpeg";
        public static final String PNG = "image/png";
        public static final String TIFF = "image/tiff";
        public static final String SVG_XML = "image/svg+xml";

        /**
         * Private constructor.
         */
        private Image() {
            // Utility class
        }
    }

    /**
     * Constants for multipart media type codes.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-06
     * @since 0.1
     */
    public static final class Multipart {

        public static final String ENCRYPTED = "multipart/encrypted";
        public static final String FORM_DATA = "multipart/form-data";
        public static final String HEADER_SET = "multipart/header-set";
        public static final String MULTILINGUAL = "multipart/multilingual";
        public static final String RELATED = "multipart/related";
        public static final String REPORT = "multipart/report";
        public static final String SIGNED = "multipart/signed";
        public static final String VOICE_MESSAGE = "multipart/voice-message";

        /**
         * Private constructor.
         */
        private Multipart() {
            // Utility class
        }
    }

    /**
     * Constants for text media type codes.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-06
     * @since 0.1
     */
    public static final class Text {

        public static final String CACHE_MANIFEST = "text/cache-manifest";
        public static final String CALENDAR = "text/calendar";
        public static final String CSS = "text/css";
        public static final String CSV = "text/csv";
        public static final String CSV_SCHEMA = "text/csv-schema";
        public static final String DIRECTORY = "text/directory";
        public static final String DNS = "text/dns";
        public static final String ECMASCRIPT = "text/ecmascript";
        public static final String HTML = "text/html";
        public static final String JAVASCRIPT = "text/javascript";
        public static final String MARKDOWN = "text/markdown";
        public static final String PLAIN = "text/plain";
        public static final String RTF = "text/rtf";
        public static final String SGML = "text/SGML";
        public static final String STRINGS = "text/strings";
        public static final String TAB_SEPARATED_VALUES = "text/tab-separated-values";
        public static final String VCARD = "text/vcard";
        public static final String WGSL = "text/wgsl";
        public static final String XML = "text/xml";

        /**
         * Private constructor.
         */
        private Text() {
            // Utility class
        }
    }

    /**
     * Constants for video media type codes.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2023-06
     * @since 0.1
     */
    public static final class Video {

        public static final String H261 = "video/H261";
        public static final String H263 = "video/H263";
        public static final String H264 = "video/H264";
        public static final String H265 = "video/H265";
        public static final String H266 = "video/H266";
        public static final String JPEG = "video/JPEG";
        public static final String JPEG_2000 = "video/jpeg2000";
        public static final String MP4 = "video/mp4";
        public static final String MPEG = "video/mpeg4-generic";
        public static final String OGG = "video/ogg";

        /**
         * Private constructor.
         */
        private Video() {
            // Utility class
        }
    }
}
