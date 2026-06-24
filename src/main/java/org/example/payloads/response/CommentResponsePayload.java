package org.example.payloads.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "comment")
public class CommentResponsePayload {

    @JsonProperty("comment_id")
    @JacksonXmlProperty(localName = "comment_id")
    private String commentId;

    @JsonProperty("user_id")
    @JacksonXmlProperty(localName = "user_id")
    private String userId;

    @JsonProperty("post_id")
    @JacksonXmlProperty(localName = "post_id")
    private String postId;

    @JsonProperty("comment")
    @JacksonXmlProperty(localName = "comment")
    private String comment;

    @JsonProperty("created_at")
    @JacksonXmlProperty(localName = "created_at")
    private String createdAt;

    public CommentResponsePayload() {}

    private CommentResponsePayload(Builder builder) {
        this.commentId = builder.commentId;
        this.userId = builder.userId;
        this.postId = builder.postId;
        this.comment = builder.comment;
        this.createdAt = builder.createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCommentId() {
        return commentId;
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

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentResponsePayload that = (CommentResponsePayload) o;
        return Objects.equals(commentId, that.commentId)
                && Objects.equals(userId, that.userId)
                && Objects.equals(postId, that.postId)
                && Objects.equals(comment, that.comment)
                && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, userId, postId, comment, createdAt);
    }

    @Override
    public String toString() {
        return "CommentResponsePayload{" +
                "commentId='" + commentId + '\'' +
                ", userId='" + userId + '\'' +
                ", postId='" + postId + '\'' +
                ", comment='" + comment + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    public static class Builder {
        private String commentId;
        private String userId;
        private String postId;
        private String comment;
        private String createdAt;

        public Builder commentId(String commentId) {
            this.commentId = commentId;
            return this;
        }

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

        public Builder createdAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public CommentResponsePayload build() {
            return new CommentResponsePayload(this);
        }
    }
}
