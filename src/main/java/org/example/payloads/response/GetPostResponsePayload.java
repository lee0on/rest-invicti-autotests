package org.example.payloads.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "response")
public class GetPostResponsePayload {

    @JsonProperty("post_id")
    @JacksonXmlProperty(localName = "post_id")
    private String postId;

    @JsonProperty("post")
    @JacksonXmlProperty(localName = "post")
    private PostResponsePayload post;

    public GetPostResponsePayload() {}

    public String getPostId() {
        return postId;
    }

    public PostResponsePayload getPost() {
        return post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetPostResponsePayload that = (GetPostResponsePayload) o;
        return Objects.equals(postId, that.postId)
                && Objects.equals(post, that.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, post);
    }

    @Override
    public String toString() {
        return "GetPostResponsePayload{" +
                "postId='" + postId + '\'' +
                ", post=" + post +
                '}';
    }
}