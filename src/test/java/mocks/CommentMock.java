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

import com.github.tomakehurst.wiremock.http.Fault;

/**
 * WireMock stub configurations for the Comments API.
 *
 * <p>Each static method registers a single stub describing one scenario
 * (success, client error, server error, timeout). These stubs isolate tests
 * from the real Comments service so error handling and resilience can be
 * exercised deterministically.
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

    private CommentMock() {
    }

    /**
     * Stubs {@code GET /comments} to return HTTP 200 with a JSON array of
     * comment objects, simulating a successful list retrieval.
     */
    public static void stubGetAllCommentsSuccess() {
        stubFor(get(urlEqualTo(COMMENTS_PATH))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", JSON)
                        .withBody("""
                                [
                                  {
                                    "comment_id": "17",
                                    "user_id": "3",
                                    "post_id": "5",
                                    "comment": "Great write-up, thanks for sharing.",
                                    "created_at": "2026-06-20T09:14:00Z"
                                  },
                                  {
                                    "comment_id": "18",
                                    "user_id": "7",
                                    "post_id": "5",
                                    "comment": "I disagree with the second point.",
                                    "created_at": "2026-06-21T11:42:00Z"
                                  }
                                ]
                                """)));
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
                        .withBody("""
                                {
                                  "user_id": "7",
                                  "post_id": "5",
                                  "comment": "I disagree with the second point.",
                                  "created_at": "2026-06-21T11:42:00Z",
                                  "comment_id": "18"
                                }
                                """)));
    }

    /**
     * Stubs {@code POST /comments} with a missing/empty body to return HTTP 400,
     * simulating a validation failure when required fields are absent.
     */
    public static void stubCreateCommentBadRequest() {
        stubFor(post(urlEqualTo(COMMENTS_PATH))
                .withRequestBody(equalToJson("{}"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", JSON)
                        .withBody("""
                                {
                                  "comment_id": false,
                                  "message": "user_id, post_id and comment are required."
                                }
                                """)));
    }

    /**
     * Stubs {@code POST /comments} to return HTTP 500, simulating an internal
     * server error so callers can exercise their server-error handling.
     */
    public static void stubCreateCommentServerError() {
        stubFor(post(urlEqualTo(COMMENTS_PATH))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", JSON)
                        .withBody("""
                                {
                                  "comment_id": false,
                                  "message": "Internal server error."
                                }
                                """)));
    }

    /**
     * Stubs {@code GET /comments/{id}} to return HTTP 200 with a wrapper object
     * containing the requested comment, simulating a successful fetch by id.
     */
    public static void stubGetCommentSuccess() {
        stubFor(get(urlPathMatching(COMMENT_BY_ID_PATTERN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", JSON)
                        .withBody("""
                                {
                                  "comment_id": "18",
                                  "comment": {
                                    "comment_id": "18",
                                    "user_id": "7",
                                    "post_id": "5",
                                    "comment": "I disagree with the second point.",
                                    "created_at": "2026-06-21T11:42:00Z"
                                  }
                                }
                                """)));
    }

    /**
     * Stubs {@code GET /comments/{id}} to return HTTP 404 with the not-found
     * wrapper payload, simulating a request for a comment that does not exist.
     */
    public static void stubGetCommentNotFound() {
        stubFor(get(urlPathMatching(COMMENT_BY_ID_PATTERN))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", JSON)
                        .withBody("""
                                {
                                  "comment_id": "comment_id",
                                  "comment": false
                                }
                                """)));
    }

    /**
     * Stubs {@code PUT /comments/{id}} to return HTTP 200 with the updated
     * comment wrapper, simulating a successful update.
     */
    public static void stubUpdateCommentSuccess() {
        stubFor(put(urlPathMatching(COMMENT_BY_ID_PATTERN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", JSON)
                        .withBody("""
                                {
                                  "comment": {
                                    "comment_id": "18",
                                    "user_id": "7",
                                    "post_id": "5",
                                    "comment": "Edited: I see the point now.",
                                    "created_at": "2026-06-21T11:42:00Z"
                                  },
                                  "response": null
                                }
                                """)));
    }

    /**
     * Stubs {@code DELETE /comments/{id}} to return HTTP 200 with the deletion
     * confirmation payload, simulating a successful delete.
     */
    public static void stubDeleteCommentSuccess() {
        stubFor(delete(urlPathMatching(COMMENT_BY_ID_PATTERN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", JSON)
                        .withBody("""
                                {
                                  "message": "comment deleted.",
                                  "comment_id": "18"
                                }
                                """)));
    }

    /**
     * Stubs {@code GET /comments/{id}} to respond after a fixed delay that
     * exceeds typical client read timeouts, simulating a slow/timing-out
     * upstream so timeout-resilience handling can be verified.
     */
    public static void stubGetCommentTimeout() {
        stubFor(get(urlPathMatching(COMMENT_BY_ID_PATTERN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", JSON)
                        .withFixedDelay(10_000)
                        .withBody("""
                                {
                                  "comment_id": "18",
                                  "comment": {
                                    "comment_id": "18",
                                    "user_id": "7",
                                    "post_id": "5",
                                    "comment": "This body arrives too late.",
                                    "created_at": "2026-06-21T11:42:00Z"
                                  }
                                }
                                """)));
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
