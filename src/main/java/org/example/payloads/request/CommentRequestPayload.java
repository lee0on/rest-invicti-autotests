package org.example.payloads.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "comment")
public class CommentRequestPayload {

    @JsonProperty("user_id")
    @JacksonXmlProperty(localName = "user_id")
    private String userId;

    @JsonProperty("post_id")
    @JacksonXmlProperty(localName = "post_id")
    private String postId;

    @JsonProperty("comment")
    @JacksonXmlProperty(localName = "comment")
    private String comment;

    public CommentRequestPayload() {}

    private CommentRequestPayload(Builder builder) {
        this.userId = builder.userId;
        this.postId = builder.postId;
        this.comment = builder.comment;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getUserId() {
        return userId;
    }

    public String getPostId() {
        return postId;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentRequestPayload that = (CommentRequestPayload) o;
        return Objects.equals(userId, that.userId)
                && Objects.equals(postId, that.postId)
                && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, postId, comment);
    }

    @Override
    public String toString() {
        return "CommentRequestPayload{" +
                "userId='" + userId + '\'' +
                ", postId='" + postId + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    public static class Builder {
        private String userId;
        private String postId;
        private String comment;

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder postId(String postId) {
            this.postId = postId;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public CommentRequestPayload build() {
            return new CommentRequestPayload(this);
        }
    }
}
