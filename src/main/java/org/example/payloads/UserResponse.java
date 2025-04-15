package org.example.payloads;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "response")
public class UserResponse {

    @JsonProperty
    private String username;
    @JsonProperty
    private String email;
    @JsonProperty
    private String password;
    @JsonProperty
    private String first_name;
    @JsonProperty
    private String last_name;
    @JsonProperty
    private String created_at;
    @JsonProperty
    private String user_id;

    public UserResponse() {}

    public UserResponse(String username, String email, String password, String first_name, String last_name, String created_at, String user_id){
        this.username = username;
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.created_at = created_at;
        this.user_id = user_id;
    }

    @JacksonXmlProperty
    public String getUsername() {
        return username;
    }

    @JacksonXmlProperty
    public String getEmail() {
        return email;
    }

    @JacksonXmlProperty
    public String getPassword() {
        return password;
    }

    @JacksonXmlProperty
    public String getFirst_name() {
        return first_name;
    }

    @JacksonXmlProperty
    public String getLast_name() {
        return last_name;
    }

    @JacksonXmlProperty
    public String getCreated_at() {
        return created_at;
    }

    @JacksonXmlProperty
    public String getUser_id() {
        return user_id;
    }
}
