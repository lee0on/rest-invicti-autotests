package org.example.payloads;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "response")
public class UserDeletionResponse {

    @JsonProperty
    private String message;
    @JsonProperty
    private String username;

    public UserDeletionResponse() {}

    public UserDeletionResponse(String message, String username){
        this.message = message;
        this.username = username;
    }

    @JacksonXmlProperty
    public String getMessage() {
        return message;
    }

    @JacksonXmlProperty
    public String getUsername() {
        return username;
    }
}
