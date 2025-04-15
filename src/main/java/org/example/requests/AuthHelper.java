package org.example.requests;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class AuthHelper {

    public static RequestSpecification login(){
        return given()
                .auth()
                .basic("admin", "123456");
    }
}
