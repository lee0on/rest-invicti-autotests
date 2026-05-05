package org.example.payloads.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "response")
public class PostDeletionResponsePayload {

    @JsonProperty("message")
    @JacksonXmlProperty(localName = "message")
    private String message;

    @JsonProperty("post_id")
    @JacksonXmlProperty(localName = "post_id")
    private String postId;

    public PostDeletionResponsePayload() {}

    public String getMessage() {
        return message;
    }

    public String getPostId() {
        return postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostDeletionResponsePayload that = (PostDeletionResponsePayload) o;
        return Objects.equals(message, that.message)
                && Objects.equals(postId, that.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, postId);
    }

    @Override
    public String toString() {
        return "PostDeletionResponsePayload{" +
                "message='" + message + '\'' +
                ", postId='" + postId + '\'' +
                '}';
    }
}