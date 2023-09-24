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

        /**
         * Atom Syndication Format.
         * RFC 4287 Section 7.
         */
        public static final String ATOM_XML = "application/atom+xml";
        /**
         * JSON Format for iCalendar.
         * RFC 7265 Section 7.
         */
        public static final String CALENDAR_JSON = "application/calendar+json";
        /**
         * XML Format for iCalendar.
         * RFC 6321 Section 7.2.
         */
        public static final String CALENDAR_XML = "application/calendar+xml";
        /**
         * Domain Name System DNS) detached information.
         * RFC 4027 Section 5.
         */
        public static final String DNS = "application/dns";
        /**
         * Extensible MultiModal Annotation.
         * @see <a href="https://www.w3.org/TR/emma/#appB">Specification</a>
         */
        public static final String EMMA = "application/emma+xml";
        /**
         * Emotion Markup Language (EmotionML).
         * @see <a href="https://www.w3.org/TR/emotionml/#MIME-type">Specification</a>
         */
        public static final String EMOTIONML_XML = "application/emotionml+xml";
        /**
         * Portable Font Resource (PFR).
         * RFC 3073.
         */
        public static final String FONT_FDPFR = "application/font-tdpfr";
        /**
         * Geospatial data interchange format (GeoJSON).
         * RFC 7946 Section 12.
         */
        public static final String GEO_JSON = "application/geo+json";
        /**
         * GZIP compression format.
         * RFC 6713 Section 3.
         */
        public static final String GZIP = "application/gzip";
        /**
         * Ink Markup Language (InkML).
         * @see <a href="https://www.w3.org/TR/2011/REC-InkML-20110920/#mime-definition">Specification</a>
         */
        public static final String INKML = "application/inkml+xml";
        /**
         * Java JAR archive.
         * @see <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/jar/jar.html">Specification</a>
         */
        public static final String JAVA_ARCHIVE = "application/java-archive";
        /**
         * JavaScript programming language.
         * RFC 9239 Section 6.
         * Deprecated in favor of {@link Text#JAVASCRIPT}.
         */
        public static final String JAVASCRIPT = "application/javascript";
        /**
         * JavaScript Object Notation (JSON).
         * RFC 8259 Section 11.
         */
        public static final String JSON = "application/json";
        /**
         * JavaScript Object Notation (JSON) Patch.
         * RFC 6902 Section 6.
         */
        public static final String JSON_PATCH = "application/json-patch+json";
        /**
         * JavaScript Object Notation (JSON) Text Sequence.
         * RFC 7464 Section 4.
         */
        public static final String JSON_SEQ = "application/json-seq";
        /**
         * Web Application Manifest.
         * @see <a href="https://www.w3.org/TR/appmanifest/#iana-considerations">Specification</a>
         */
        public static final String MANIFEST_JSON = "application/manifest+json";
        /**
         * Mathematical Markup Language (MathML).
         * @see <a href="https://www.w3.org/TR/MathML3/appendixb.html">Specification</a>
         */
        public static final String MATHML_XML = "application/mathml+xml";
        /**
         * Mathematical Markup Language (MathML) content.
         * @see <a href="https://www.w3.org/TR/MathML3/appendixb.html">Specification</a>
         */
        public static final String MATHML_CONTENT_XML = "application/mathml-content+xml";
        /**
         * Mathematical Markup Language (MathML) presentation.
         * @see <a href="https://www.w3.org/TR/MathML3/appendixb.html">Specification</a>
         */
        public static final String MATHML_PRESENTATION_XML = "application/mathml-presentation+xml";
        /**
         * JavaScript Object Notation (JSON) Merge Patch.
         * RFC 7396 Section 4.
         */
        public static final String MERGE_PATCH_JSON = "application/merge-patch+json";
        /**
         * MP4 file with MPEG-4 (ISO/IEC 14496) system stream and neither
         * visual nor audio presentation.
         * RFC 4337 Section 3.3.
         */
        public static final String MP4 = "application/mp4";
        /**
         * MPEG-4 Systems stream (ISO/IEC 14496-1).
         * RFC 3640 Section 4.1.
         */
        public static final String MPEG4 = "application/mpeg4-generic";
        /**
         * Natural Language Semantics Markup Language.
         * RFC 6787 Section 13.2.1.
         */
        public static final String NLSML = "application/nlsml+xml";
        /**
         * CommonJS module.
         * @see <a href="https://nodejs.org/api/modules.html">Specification</a>
         */
        public static final String NODE = "application/node";
        /**
         * OAuth authorization request.
         * RFC 9101 Section 9.4.1.
         */
        public static final String OAUTH_AUTHZ_REQ = "application/oauth-authz-req+jwt";
        /**
         * Arbitrary binary data.
         * RFC 2046 Section 4.5.1.
         */
        public static final String OCTET_STREAM = "application/octet-stream";
        /**
         * OGG stream.
         * RFC 5334 Section 10.1.
         */
        public static final String OGG = "application/ogg";
        /**
         * Portable Document Format (PDF).
         * RFC 8118 Section 8.
         */
        public static final String PDF = "application/pdf";
        /**
         * PEM encoded certificate chain.
         * RFC 8555 Section 7.
         */
        public static final String PEM_CERTIFICATE_CHAIN = "application/pem-certificate-chain";
        /**
         * Pretty Good Privacy (PGP) encrypted content control information.
         * RFC 3156 Section 4.
         */
        public static final String PGP_ENCRYPTED = "application/pgp-encrypted";
        /**
         * Pretty Good Privacy (PGP) public keys.
         * RFC 3156 Section 7.
         */
        public static final String PGP_KEYS = "application/pgp-keys";
        /**
         * Pretty Good Privacy (PGP) signed content digital signature.
         * RFC 3156 Section 5.
         */
        public static final String PGP_SIGNATURE = "application/pgp-signature";
        /**
         * Secure/Multipurpose Internet Mail Extensions (S/MIME) message.
         * RFC 8551 Section 5.1.
         */
        public static final String PKCS7_MIME = "application/pkcs7-mime";
        /**
         * Secure/Multipurpose Internet Mail Extensions (S/MIME) signature.
         * RFC 8551 Section 5.2.
         */
        public static final String PKCS7_SIGNATURE = "application/pkcs7-signature";
        /**
         * Public-Key Cryptography Standards (PKCS) private-key information.
         * RFC 5958 Section 7.1.
         */
        public static final String PKCS8 = "application/pkcs8";
        /**
         * Public-Key Cryptography Standards (PKCS) encrypted private-key information.
         * RFC 8351.
         */
        public static final String PKCS8_ENCRYPTED = "application/pkcs8-encrypted";
        /**
         * Public-Key Cryptography Standards (PKCS) certificate signing request.
         * RFC 5967.
         */
        public static final String PKCS10 = "application/pkcs10";
        /**
         * Public-Key Cryptography Standards (PKCS) personal information
         * exchange container.
         * RFC 7292.
         */
        public static final String PKCS12 = "application/pkcs12";
        /**
         * PostScript program.
         * RFC 2046 Section 4.5.2.
         */
        public static final String POSTSCRIPT = "application/postscript";
        /**
         * Portable Symmetric Key Container (PSKC).
         * RFC 6030 Section 12.1.
         */
        public static final String PSKC = "application/pskc+xml";
        /**
         * Request for Comments (RFC) in XML (xml2rfc).
         * RFC 7991 Section 8.1.
         */
        public static final String RFC_XML = "application/rfc+xml";
        /**
         * Rich Text Format (RTF).
         */
        public static final String RTF = "application/rtf";
        /**
         * State Chart XML (SCXML).
         * @see <a href="https://www.w3.org/TR/scxml/#mimetype">Specification</a>
         */
        public static final String SCXML = "application/scxml+xml";
        /**
         * Standard Generalized Markup Language (SGML).
         * RFC 1874 Section 2.2.
         */
        public static final String SGML = "application/SGML";
        /**
         * Simple Object Access Protocol (SOAP) message envelop.
         * RFC 3902 Section 2.
         */
        public static final String SOAP_XML = "application/soap+xml";
        /**
         * Structured Query Language (SQL).
         * RFC 6922.
         */
        public static final String SQL = "application/sql";
        /**
         * Speech Synthesis Markup Language (SSML).
         * @see <a href="https://www.w3.org/TR/speech-synthesis11/#AppC">Specification</a>
         */
        public static final String SSML = "application/scxml+xml";
        /**
         * JSON Format for vCard.
         * RFC 7095 Section 7.
         */
        public static final String VCARD_JSON = "application/vcard+json";
        /**
         * XML Format for vCard.
         * RFC 6351 Section 8.1.
         */
        public static final String VCARD_XML = "application/vcard+xml";
        /**
         * Voice Extensible Markup Language (VoiceXML).
         * @see <a href="https://www.w3.org/TR/voicexml20/#dmlAMediaType">Specification</a>
         */
        public static final String VOICE_XML = "application/voicexml+xml";
        /**
         * Web Assembly module.
         * @see <a href="https://webassembly.org/docs/web/">Specification</a>
         */
        public static final String WASM = "application/wasm";
        /**
         * Web Services Description Language (WSDL).
         * @see <a href="https://www.w3.org/TR/wsdl20/#ietf-draft">Specification</a>
         */
        public static final String WSDL = "application/wsdl+xml";
        /**
         * Web Services Policy document.
         * @see <a href="https://www.w3.org/TR/wsdl20/#ietf-draft">Specification</a>
         */
        public static final String WSPOLICY = "application/wspolicy+xml";
        /**
         * Simple Certificate Enrolment Protocol (SCEP) certificate enrolment
         * or renewal message.
         * RFC 8894 Section 6.4.
         */
        public static final String X_PKI_MESSAGE = "application/x-pki-message";
        /**
         * URL encoded list of tuples.
         * @see <a href="https://url.spec.whatwg.org/#application/x-www-form-urlencoded">Specification</a>
         */
        public static final String X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
        /**
         * Simple Certificate Enrolment Protocol (SCEP) certificate response
         * message.
         * RFC 8894 Section 6.1.
         */
        public static final String X_X509_CA_CERT = "application/x-x509-ca-cert";
        /**
         * Simple Certificate Enrolment Protocol (SCEP) certificate chain
         * response message.
         * RFC 9999 Section 6.2.
         */
        public static final String X_X509_CA_RA_CERT = "application/x-x509-ca-ra-cert";
        /**
         * Simple Certificate Enrolment Protocol (SCEP) CA next certificate
         * response message.
         * RFC 9999 Section 6.3.
         */
        public static final String X_X509_NEXT_RA_CERT = "application/x-x509-next-ca-cert";
        /**
         * URL encoded list of tuples.
         * @see <a href="https://html.spec.whatwg.org/multipage/iana.html#application/xhtml+xml">Specification</a>
         */
        public static final String XHTML = "application/xhtml+xml";
        /**
         * Extensible Markup Language (XML).
         * RFC 7303 Section 9.1.
         */
        public static final String XML = "application/xml";
        /**
         * Extensible Markup Language (XML) document type definition.
         * RFC 9999 Section 9.5.
         */
        public static final String XML_DTD = "application/xml-dtd";
        /**
         * Extensible Markup Language (XML) external parsed entity.
         * RFC 9999 Section 9.3.
         */
        public static final String XML_EXTERNAL_PARSED_ENTITY = "application/xml-external-parsed-entity";
        /**
         * Extensible Markup Language (XML) patch operation.
         * RFC 7351 Section 3.
         */
        public static final String XML_PATCH = "application/xml-patch+xml";
        /**
         * Extensible Messaging and Presence Protocol (XMPP) message.
         * RFC 3923 Section 12.2.
         */
        public static final String XMPP = "application/xmpp+xml";
        /**
         * XSL Transformation (XSLT).
         * @see <a href="https://www.w3.org/TR/xslt20/#xslt-mime-definition">Specification</a>
         */
        public static final String XSLT = "application/xslt+xml";
        /**
         * YAML Ain’t Markup Language (YAML). 
         * @see <a href="https://yaml.org/spec/">Specification</a>
         */
        public static final String YAML = "application/yaml";
        /**
         * ZIP compression format.
         * @see <a href="https://pkware.cachefly.net/webdocs/casestudies/APPNOTE.TXT">Specification</a>
         */
        public static final String ZIP = "application/zip";
        /**
         * ZLIB compression format.
         * RFC 6713 Section 2.
         */
        public static final String ZLIB = "application/zlib";
        /**
         * Zstandard compression format.
         * RFC 8878 Section 7.1.
         */
        public static final String ZSTD = "application/zstd";

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

        /**
         * MP4 File with audio but without visual presentation.
         * RFC 4337 Section 3.2.
         */
        public static final String MP4 = "audio/mp4";
        /**
         * MPEG-1 (ISO/IEC 11172-3) or MPEG-2 (ISO/IEC 13818-3) audio stream.
         * RFC 3003.
         */
        public static final String MPEG = "audio/mpeg";
        /**
         * MPEG-4 audio stream (ISO/IEC 14496-3).
         * RFC 3640 Section 4.1.
         */
        public static final String MPEG4 = "audio/mpeg4-generic";
        /**
         * OGG audio stream.
         * RFC 5334 Section 10.3.
         */
        public static final String OGG = "audio/ogg";
        /**
         * WEBM audio stream.
         * @see <a href="https://www.webmproject.org/docs/container/">Specification</a>
         */
        public static final String WEBM = "audio/webm";

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

        /**
         * Collection font.
         * RFC 8081 Section 4.4.4.
         */
        public static final String COLLECTION = "font/collection";
        /**
         * OpenType Layout (OTF) font.
         * RFC 8081 Section 4.4.3.
         */
        public static final String OTF = "font/otf";
        /**
         * Generic SFNT font.
         * RFC 8081 Section 4.4.1.
         */
        public static final String SFNT = "font/sfnt";
        /**
         * TTF font.
         * RFC 8081 Section 4.4.2.
         */
        public static final String TTF = "font/ttf";
        /**
         * WOFF 1.0 font.
         * RFC 8081 Section 4.4.5.
         */
        public static final String WOFF = "font/woff";
        /**
         * WOFF 2.0 font.
         * RFC 8081 Section 4.4.6.
         */
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

        /**
         * Animated Portable Network Graphics (APNG).
         * @see <a href="https://www.w3.org/TR/png/#image-apng">Specification</a>
         */
        public static final String APNG = "image/apng";
        /**
         * AV1 Image File Format (AVIF)
         * @see <a href="https://aomediacodec.github.io/av1-avif/">Specification</a>
         */
        public static final String AVIF = "image/avif";
        /**
         * Graphics Interchange Format (GIF).
         * @see <a href="https://www.w3.org/Graphics/GIF/spec-gif89a.txt">Specification</a>
         */
        public static final String GIF = "image/gif";
        /**
         * Joint Photographic Expert Group image (JPEG).
         * RFC 9999 Section 7. 
         */
        public static final String JPEG = "image/jpeg";
        /**
         * Portable Network Graphics (PNG).
         * @see <a href="https://www.w3.org/TR/png/#A-Media-type">Specification</a>
         */
        public static final String PNG = "image/png";
        /**
         * JTag Image File Format (TIFF).
         * RFC 3302. 
         */
        public static final String TIFF = "image/tiff";
        /**
         * Scalable Vector Graphics (SVG).
         * @see <a href="https://www.w3.org/TR/SVG/mimereg.html">Specification</a>
         */
        public static final String SVG_XML = "image/svg+xml";
        /**
         * Web Picture (WebP).
         * @see <a href="https://www.ietf.org/archive/id/draft-zern-webp-12.html#name-iana-considerations">Specification</a>
         */
        public static final String WEBP = "image/webp";

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

        /**
         * Pretty Good Privacy (PGP) encrypted content.
         * RFC 3156 Section 4.
         */
        public static final String ENCRYPTED = "multipart/encrypted";
        /**
         * HTML forma data.
         * RFC 7578.
         */
        public static final String FORM_DATA = "multipart/form-data";
        /**
         * Multiple Language Content Type.
         * RFC 8255.
         */
        public static final String MULTILINGUAL = "multipart/multilingual";
        /**
         * Compound object with several inter-related parts.
         * RFC 2387.
         */
        public static final String RELATED = "multipart/related";
        /**
         * Pretty Good Privacy (PGP) signed content.
         * RFC 3156 Section 5.
         */
        public static final String SIGNED = "multipart/signed";

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

        /**
         * Internet Calendaring and Scheduling Core Object (iCalendar).
         * RFC 5545 Section 8.1.
         */
        public static final String CALENDAR = "text/calendar";
        /**
         * Cascading Style Sheet (CSS).
         * RFC 2318.
         */
        public static final String CSS = "text/css";
        /**
         * Comma-Separated Values (CSV) file.
         * RFC 4180 Section 4.
         */
        public static final String CSV = "text/csv";
        /**
         * Directory information.
         * RFC 2425.
         */
        public static final String DIRECTORY = "text/directory";
        /**
         * Domain Name System DNS) detached information.
         * RFC 4027 Section 5.
         */
        public static final String DNS = "text/dns";
        /**
         * ECMAScript programming language.
         * RFC 9239 Section 6.2.1.
         * Deprecated in favor of {@link #JAVASCRIPT}.
         */
        public static final String ECMASCRIPT = "text/ecmascript";
        /**
         * HTML server event stream.
         * @see <a href="https://html.spec.whatwg.org/multipage/iana.html#text/event-stream">Specification</a>
         */
        public static final String EVENT_STREAM = "text/event-stream";
        /**
         * HTML document.
         * @see <a href="https://html.spec.whatwg.org/multipage/iana.html#text/html">Specification</a>
         */
        public static final String HTML = "text/html";
        /**
         * JavaScript programming language.
         * RFC 9239 Section 6.1.1.
         */
        public static final String JAVASCRIPT = "text/javascript";
        /**
         * Markdown document.
         * RFC 7763.
         */
        public static final String MARKDOWN = "text/markdown";
        /**
         * Ping request.
         * @see <a href="https://html.spec.whatwg.org/multipage/iana.html#text/ping">Specification</a>
         */
        public static final String PING = "text/ping";
        /**
         * Plain text.
         * RFC 2046 Section 4.1.3.
         */
        public static final String PLAIN = "text/plain";
        /**
         * Rich Text Format (RTF).
         */
        public static final String RTF = "text/rtf";
        /**
         * Standard Generalized Markup Language (SGML).
         * RFC 1874 Section 2.1.
         */
        public static final String SGML = "text/SGML";
        /**
         * Tab-Separated Values (TSV).
         */
        public static final String TAB_SEPARATED_VALUES = "text/tab-separated-values";
        /**
         * vCard data.
         * RFC 6350 Section 10.
         */
        public static final String VCARD = "text/vcard";
        /**
         * Web Video Text Track (WebVTT).
         * @see <a href="https://w3c.github.io/webvtt/">Specification</a>
         */
        public static final String VTT = "text/vtt";
        /**
         * WebGPU Shading Language (WGSL).
         * @see <a href="https://www.w3.org/TR/WGSL/">Specification</a>
         */
        public static final String WGSL = "text/wgsl";
        /**
         * Extensible Markup Language (XML).
         * RFC 7303 Section 9.2.
         */
        public static final String XML = "text/xml";
        /**
         * Extensible Markup Language (XML) external parsed entity.
         * RFC 9999 Section 9.4.
         */
        public static final String XML_EXTERNAL_PARSED_ENTITY = "text/xml-external-parsed-entity";

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

        /**
         * H.261 video stream.
         * RFC 3555 Section 4.2.4.
         */
        public static final String H261 = "video/H261";
        /**
         * H.261 video stream.
         * RFC 3555 Section 4.2.5.
         */
        public static final String H263 = "video/H263";
        /**
         * H.263 video stream.
         * RFC 3984 Section 8.1.
         */
        public static final String H264 = "video/H264";
        /**
         * H.265 video stream.
         * RFC 7798 Section 7.1.
         */
        public static final String H265 = "video/H265";
        /**
         * H.266 video stream.
         * RFC 9328 Section 7.1.
         */
        public static final String H266 = "video/H266";
        /**
         * JPEG video stream.
         * RFC 3555 Section 4.2.3.
         */
        public static final String JPEG = "video/JPEG";
        /**
         * JPEG 2000 video stream.
         * RFC 5371 Section 6.
         */
        public static final String JPEG_2000 = "video/jpeg2000";
        /**
         * MP4 video stream.
         * RFC 4337 Section 3.1.
         */
        public static final String MP4 = "video/mp4";
        /**
         * MPEG-4 visual stream (ISO/IEC 14496-2).
         * RFC 3640 Section 4.1.
         */
        public static final String MPEG = "video/mpeg4-generic";
        /**
         * OGG visual stream.
         * RFC 5334 Section 10.2.
         */
        public static final String OGG = "video/ogg";
        /**
         * WebM visual stream.
         * @see <a href="https://www.webmproject.org/docs/container/">Specification</a>
         */
        public static final String WEBM = "video/webm";

        /**
         * Private constructor.
         */
        private Video() {
            // Utility class
        }
    }
}
