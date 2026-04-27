package org.example.payloads.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "response")
public class UserDeletionResponsePayload {

    @JsonProperty("message")
    @JacksonXmlProperty(localName = "message")
    private String message;

    @JsonProperty("username")
    @JacksonXmlProperty(localName = "username")
    private String username;

    public UserDeletionResponsePayload() {}

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDeletionResponsePayload that = (UserDeletionResponsePayload) o;
        return Objects.equals(message, that.message)
                && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, username);
    }

    @Override
    public String toString() {
        return "UserDeletionResponse{" +
                "message='" + message + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
