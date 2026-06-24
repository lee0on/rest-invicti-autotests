package mocks;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.http.Fault;
import org.example.payloads.response.CommentDeletionResponsePayload;
import org.example.payloads.response.CommentResponsePayload;
import org.example.payloads.response.EditCommentResponsePayload;
import org.example.payloads.response.GetCommentResponsePayload;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * WireMock stub configurations for the Comments API.
 *
 * <p>Each static method registers a single stub describing one scenario
 * (success, client error, server error, timeout). These stubs isolate tests
 * from the real Comments service so error handling and resilience can be
 * exercised deterministically.
 *
 * <p>Success bodies are serialized from the same payload POJOs the production
 * code deserializes into, so the stubs cannot silently drift from the contract.
 * The deliberately non-conforming error bodies (e.g. {@code "comment_id": false})
 * are kept as ordered maps because they do not match any typed payload.
 *
 * <p>Canonical fixture values are exposed as {@code STUB_*} constants so tests
 * can assert against exactly what the stubs return.
 *
 * <p>Base path: {@code /basic_authentication/api/comments}.
 *
 * <p>This class contains no assertions or test logic — it only registers stubs.
 */
public final class CommentMock {

    private static final String COMMENTS_PATH = "/basic_authentication/api/comments";
    private static final String COMMENT_BY_ID_PATTERN =
            "/basic_authentication/api/comments/[0-9]+";
    private static final String JSON = "application/json";

    /** Canonical comment used by the success stubs; tests assert against these. */
    public static final String STUB_COMMENT_ID = "18";
    public static final String STUB_USER_ID = "7";
    public static final String STUB_POST_ID = "5";
    public static final String STUB_COMMENT_TEXT = "I disagree with the second point.";
    public static final String STUB_CREATED_AT = "2026-06-21T11:42:00Z";
    public static final String STUB_EDITED_TEXT = "Edited: I see the point now.";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private CommentMock() {
    }

    private static CommentResponsePayload canonicalComment() {
        return CommentResponsePayload.builder()
                .commentId(STUB_COMMENT_ID)
                .userId(STUB_USER_ID)
                .postId(STUB_POST_ID)
                .comment(STUB_COMMENT_TEXT)
                .createdAt(STUB_CREATED_AT)
                .build();
    }

    private static String json(Object body) {
        try {
            return MAPPER.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize stub body: " + body, e);
        }
    }

    /**
     * Stubs {@code GET /comments} to return HTTP 200 with a JSON array of
     * comment objects, simulating a successful list retrieval.
     */
    public static void stubGetAllCommentsSuccess() {
        CommentResponsePayload second = CommentResponsePayload.builder()
                .commentId("17")
                .userId("3")
                .postId(STUB_POST_ID)
                .comment("Great write-up, thanks for sharing.")
                .createdAt("2026-06-20T09:14:00Z")
                .build();

        stubFor(get(urlEqualTo(COMMENTS_PATH))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", JSON)
                        .withBody(json(List.of(second, canonicalComment())))));
    }

    /**
     * Stubs {@code POST /comments} to return HTTP 200 with the created comment,
     * simulating a successful comment creation.
     */
    public static void stubCreateCommentSuccess() {
        stubFor(post(urlEqualTo(COMMENTS_PATH))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", JSON)
                        .withBody(json(canonicalComment()))));
    }

    /**
     * Stubs {@code POST /comments} with a missing/empty body to return HTTP 400,
     * simulating a validation failure when required fields are absent.
     */
    public static void stubCreateCommentBadRequest() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("comment_id", false);
        body.put("message", "user_id, post_id and comment are required.");

        stubFor(post(urlEqualTo(COMMENTS_PATH))
                .withRequestBody(equalToJson("{}"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", JSON)
                        .withBody(json(body))));
    }

    /**
     * Stubs {@code POST /comments} to return HTTP 500, simulating an internal
     * server error so callers can exercise their server-error handling.
     */
    public static void stubCreateCommentServerError() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("comment_id", false);
        body.put("message", "Internal server error.");

        stubFor(post(urlEqualTo(COMMENTS_PATH))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", JSON)
                        .withBody(json(body))));
    }

    /**
     * Stubs {@code GET /comments/{id}} to return HTTP 200 with a wrapper object
     * containing the requested comment, simulating a successful fetch by id.
     */
    public static void stubGetCommentSuccess() {
        GetCommentResponsePayload body = GetCommentResponsePayload.builder()
                .commentId(STUB_COMMENT_ID)
                .comment(canonicalComment())
                .build();

        stubFor(get(urlPathMatching(COMMENT_BY_ID_PATTERN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", JSON)
                        .withBody(json(body))));
    }

    /**
     * Stubs {@code GET /comments/{id}} to return HTTP 404 with the not-found
     * wrapper payload, simulating a request for a comment that does not exist.
     */
    public static void stubGetCommentNotFound() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("comment_id", "comment_id");
        body.put("comment", false);

        stubFor(get(urlPathMatching(COMMENT_BY_ID_PATTERN))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", JSON)
                        .withBody(json(body))));
    }

    /**
     * Stubs {@code PUT /comments/{id}} to return HTTP 200 with the updated
     * comment wrapper, simulating a successful update.
     */
    public static void stubUpdateCommentSuccess() {
        EditCommentResponsePayload body = EditCommentResponsePayload.builder()
                .comment(CommentResponsePayload.builder()
                        .commentId(STUB_COMMENT_ID)
                        .userId(STUB_USER_ID)
                        .postId(STUB_POST_ID)
                        .comment(STUB_EDITED_TEXT)
                        .createdAt(STUB_CREATED_AT)
                        .build())
                .response(null)
                .build();

        stubFor(put(urlPathMatching(COMMENT_BY_ID_PATTERN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", JSON)
                        .withBody(json(body))));
    }

    /**
     * Stubs {@code DELETE /comments/{id}} to return HTTP 200 with the deletion
     * confirmation payload, simulating a successful delete.
     */
    public static void stubDeleteCommentSuccess() {
        CommentDeletionResponsePayload body = CommentDeletionResponsePayload.builder()
                .message("comment deleted.")
                .commentId(STUB_COMMENT_ID)
                .build();

        stubFor(delete(urlPathMatching(COMMENT_BY_ID_PATTERN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", JSON)
                        .withBody(json(body))));
    }

    /**
     * Stubs {@code GET /comments/{id}} to respond after a fixed delay that
     * exceeds typical client read timeouts, simulating a slow/timing-out
     * upstream so timeout-resilience handling can be verified.
     */
    public static void stubGetCommentTimeout() {
        GetCommentResponsePayload body = GetCommentResponsePayload.builder()
                .commentId(STUB_COMMENT_ID)
                .comment(canonicalComment())
                .build();

        stubFor(get(urlPathMatching(COMMENT_BY_ID_PATTERN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", JSON)
                        .withFixedDelay(10_000)
                        .withBody(json(body))));
    }

    /**
     * Stubs {@code GET /comments} to drop the connection mid-response (empty
     * response / connection reset), simulating an abrupt network failure
     * distinct from a slow timeout.
     */
    public static void stubGetAllCommentsConnectionReset() {
        stubFor(get(urlEqualTo(COMMENTS_PATH))
                .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));
    }
}
