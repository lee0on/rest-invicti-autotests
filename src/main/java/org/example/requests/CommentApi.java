package org.example.requests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.payloads.request.CommentRequestPayload;
import org.example.utils.XMLUtils;

/**
 * HTTP request abstraction for the Comment API.
 */
public class CommentApi extends Api {

    private static final String BASE_URL = apiUrl + "comments";

    /**
     * Retrieves all comments.
     *
     * @return the API response containing the comment list
     */
    public static Response getAllComments() {
        return AuthHelper.login()
                .when()
                .get(BASE_URL);
    }

    /**
     * Creates a new comment via JSON POST.
     *
     * @param payload comment data to create
     * @return the API response
     */
    public static Response createComment(CommentRequestPayload payload) {
        return AuthHelper.login()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
                .when()
                .post(BASE_URL);
    }

    /**
     * Creates a new comment via XML POST.
     *
     * @param payload comment data to create
     * @return the API response
     */
    public static Response createCommentXml(CommentRequestPayload payload) {
        return AuthHelper.login()
                .contentType(ContentType.XML)
                .accept(ContentType.XML)
                .body(XMLUtils.commentToXml(payload))
                .when()
                .post(BASE_URL);
    }

    /**
     * Retrieves a comment by ID.
     *
     * @param commentId the comment ID to look up
     * @return the API response
     */
    public static Response getComment(String commentId) {
        return AuthHelper.login()
                .when()
                .get(BASE_URL + "/" + commentId);
    }

    /**
     * Updates a comment via JSON PUT.
     *
     * @param commentId the comment ID to update
     * @param payload   updated comment data
     * @return the API response
     */
    public static Response updateComment(String commentId, CommentRequestPayload payload) {
        return AuthHelper.login()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .put(BASE_URL + "/" + commentId);
    }

    /**
     * Updates a comment via XML PUT.
     *
     * @param commentId the comment ID to update
     * @param payload   updated comment data
     * @return the API response
     */
    public static Response updateCommentXml(String commentId, CommentRequestPayload payload) {
        return AuthHelper.login()
                .contentType(ContentType.XML)
                .accept(ContentType.XML)
                .body(XMLUtils.commentToXml(payload))
                .when()
                .put(BASE_URL + "/" + commentId);
    }

    /**
     * Deletes a comment by ID.
     *
     * @param commentId the comment ID to delete
     * @return the API response
     */
    public static Response deleteComment(String commentId) {
        return AuthHelper.login()
                .accept(ContentType.XML)
                .when()
                .delete(BASE_URL + "/" + commentId);
    }
}
