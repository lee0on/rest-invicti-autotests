package org.example.payloads.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "post")
public class PostResponsePayload {

    @JsonProperty("post_id")
    @JacksonXmlProperty(localName = "post_id")
    private String postId;

    @JsonProperty("user_id")
    @JacksonXmlProperty(localName = "user_id")
    private String userId;

    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title")
    private String title;

    @JsonProperty("content")
    @JacksonXmlProperty(localName = "content")
    private String content;

    @JsonProperty("created_at")
    @JacksonXmlProperty(localName = "created_at")
    private String createdAt;

    public PostResponsePayload() {}

    public String getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostResponsePayload that = (PostResponsePayload) o;
        return Objects.equals(postId, that.postId)
                && Objects.equals(userId, that.userId)
                && Objects.equals(title, that.title)
                && Objects.equals(content, that.content)
                && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, userId, title, content, createdAt);
    }

    @Override
    public String toString() {
        return "PostResponsePayload{" +
                "postId='" + postId + '\'' +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}