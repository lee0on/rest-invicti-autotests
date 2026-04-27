package org.example.requests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.payloads.request.UserRequestPayload;
import org.example.utils.XMLUtils;

/**
 * HTTP request abstraction for the User API.
 */
public class UserApi extends Api {

    private static final String BASE_URL = apiUrl + "users";

    /**
     * Creates a new user via JSON POST.
     *
     * @param payload user data to create
     * @return the API response
     */
    public static Response createUser(UserRequestPayload payload) {
        return AuthHelper.login()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
                .when()
                .post(BASE_URL);
    }

    /**
     * Creates a new user via XML POST.
     *
     * @param payload user data to create
     * @return the API response
     */
    public static Response createUserXml(UserRequestPayload payload) {
        return AuthHelper.login()
                .contentType(ContentType.XML)
                .accept(ContentType.XML)
                .body(XMLUtils.userToXml(payload))
                .when()
                .post(BASE_URL);
    }

    /**
     * Retrieves a user by username.
     *
     * @param username the username to look up
     * @return the API response
     */
    public static Response getUser(String username) {
        return AuthHelper.login()
                .when()
                .get(BASE_URL + "/" + username);
    }

    /**
     * Retrieves all users.
     *
     * @return the API response containing the user list
     */
    public static Response getAllUsers() {
        return AuthHelper.login()
                .when()
                .get(BASE_URL);
    }

    /**
     * Updates a user via JSON PUT.
     *
     * @param username the username to update
     * @param payload  updated user data
     * @return the API response
     */
    public static Response updateUser(String username, UserRequestPayload payload) {
        return AuthHelper.login()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .put(BASE_URL + "/" + username);
    }

    /**
     * Updates a user via XML PUT.
     *
     * @param username the username to update
     * @param payload  updated user data
     * @return the API response
     */
    public static Response updateUserXml(String username, UserRequestPayload payload) {
        return AuthHelper.login()
                .contentType(ContentType.XML)
                .accept(ContentType.XML)
                .body(XMLUtils.userToXml(payload))
                .when()
                .put(BASE_URL + "/" + username);
    }

    /**
     * Deletes a user by username.
     *
     * @param username the username to delete
     * @return the API response in XML format
     */
    public static Response deleteUser(String username) {
        return AuthHelper.login()
                .accept(ContentType.XML)
                .when()
                .delete(BASE_URL + "/" + username);
    }
}
