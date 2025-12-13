package org.example.requests;

import io.restassured.response.Response;

public class PostApi extends Api {

    private static final String baseUrl = apiUrl + "posts/";

    public static Response getPost(int id){
        return AuthHelper.login()
                .when()
                .get(baseUrl + id);
    }
}
