package org.example.requests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.payloads.request.PostRequestPayload;
import org.example.utils.XMLUtils;

/**
 * HTTP request abstraction for the Post API.
 */
public class PostApi extends Api {

    private static final String BASE_URL = apiUrl + "posts";

    /**
     * Retrieves all posts.
     *
     * @return the API response containing the post list
     */
    public static Response getAllPosts() {
        return AuthHelper.login()
                .when()
                .get(BASE_URL);
    }

    /**
     * Creates a new post via JSON POST.
     *
     * @param payload post data to create
     * @return the API response
     */
    public static Response createPost(PostRequestPayload payload) {
        return AuthHelper.login()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
                .when()
                .post(BASE_URL);
    }

    /**
     * Creates a new post via XML POST.
     *
     * @param payload post data to create
     * @return the API response
     */
    public static Response createPostXml(PostRequestPayload payload) {
        return AuthHelper.login()
                .contentType(ContentType.XML)
                .accept(ContentType.XML)
                .body(XMLUtils.postToXml(payload))
                .when()
                .post(BASE_URL);
    }

    /**
     * Retrieves a post by ID.
     *
     * @param postId the post ID to look up
     * @return the API response
     */
    public static Response getPost(String postId) {
        return AuthHelper.login()
                .when()
                .get(BASE_URL + "/" + postId);
    }

    /**
     * Updates a post via JSON PUT.
     *
     * @param postId  the post ID to update
     * @param payload updated post data
     * @return the API response
     */
    public static Response updatePost(String postId, PostRequestPayload payload) {
        return AuthHelper.login()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .put(BASE_URL + "/" + postId);
    }

    /**
     * Updates a post via XML PUT.
     *
     * @param postId  the post ID to update
     * @param payload updated post data
     * @return the API response
     */
    public static Response updatePostXml(String postId, PostRequestPayload payload) {
        return AuthHelper.login()
                .contentType(ContentType.XML)
                .accept(ContentType.XML)
                .body(XMLUtils.postToXml(payload))
                .when()
                .put(BASE_URL + "/" + postId);
    }

    /**
     * Deletes a post by ID.
     *
     * @param postId the post ID to delete
     * @return the API response
     */
    public static Response deletePost(String postId) {
        return AuthHelper.login()
                .accept(ContentType.XML)
                .when()
                .delete(BASE_URL + "/" + postId);
    }

    /**
     * Retrieves all comments for a post.
     *
     * @param postId the post ID whose comments to retrieve
     * @return the API response containing the comment list
     */
    public static Response getPostComments(String postId) {
        return AuthHelper.login()
                .when()
                .get(BASE_URL + "/" + postId + "/comments");
    }
}