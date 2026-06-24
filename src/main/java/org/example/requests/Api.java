package org.example.requests;

/**
 * Base class for API request abstractions.
 *
 * <p>Holds the single definition of the API base URL. The value is resolved
 * per call (not cached in a {@code static final}) so tests can redirect requests
 * at runtime — e.g. point them at a WireMock server on a dynamically assigned
 * port. Resolution order:
 *
 * <ol>
 *   <li>{@code api.baseUrl} system property</li>
 *   <li>{@code API_BASE_URL} environment variable</li>
 *   <li>the public Invicti test site (default)</li>
 * </ol>
 */
public class Api {

    private static final String DEFAULT_BASE_URL =
            "http://rest.testsparker.com/basic_authentication/api/";

    /**
     * Resolves the API base URL, honouring the {@code api.baseUrl} system property
     * and {@code API_BASE_URL} environment variable before falling back to the default.
     *
     * @return the base URL, always terminated with a trailing slash by convention
     */
    protected static String apiUrl() {
        String fromProperty = System.getProperty("api.baseUrl");
        if (fromProperty != null && !fromProperty.isBlank()) {
            return fromProperty;
        }
        String fromEnv = System.getenv("API_BASE_URL");
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv;
        }
        return DEFAULT_BASE_URL;
    }
}
