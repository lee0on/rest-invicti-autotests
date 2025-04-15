package org.example.requests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.payloads.User;

public class UserApi {

    private static final String baseUrl = "http://rest.testsparker.com/basic_authentication/api/users";

    public static Response postUserJSON(User payload){
        return AuthHelper.login()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .body(payload)
                .when()
                .post(baseUrl);
    }

    public static Response postUserXML(String payload){
        return AuthHelper.login()
                .contentType(ContentType.XML)
                .header("Accept", "application/xml")
                .body(payload)
                .when()
                .post(baseUrl);
    }

    public static Response getUserByName(String username){
        return AuthHelper.login()
                .when()
                .get(baseUrl + "/" + username);
    }

    public static Response getUsers(){
        return AuthHelper.login()
                .when()
                .get(baseUrl);
    }

    public static Response putUserJSON(String username, User payload){
        return AuthHelper.login()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .put(baseUrl + "/" + username);
    }

    public static Response putUserXML(String username, String payload){
        return AuthHelper.login()
                .contentType(ContentType.XML)
                .header("Accept", "application/xml")
                .body(payload)
                .when()
                .put(baseUrl + "/" + username);
    }

    public static Response deleteXMLUser(String username){
        return AuthHelper.login()
                .header("Accept", "application/xml")
                .when()
                .delete(baseUrl + "/" + username);
    }
}
