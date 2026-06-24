package org.example.payloads.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "response")
public class CommentDeletionResponsePayload {

    @JsonProperty("message")
    @JacksonXmlProperty(localName = "message")
    private String message;

    @JsonProperty("comment_id")
    @JacksonXmlProperty(localName = "comment_id")
    private String commentId;

    public CommentDeletionResponsePayload() {}

    private CommentDeletionResponsePayload(Builder builder) {
        this.message = builder.message;
        this.commentId = builder.commentId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getMessage() {
        return message;
    }

    public String getCommentId() {
        return commentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentDeletionResponsePayload that = (CommentDeletionResponsePayload) o;
        return Objects.equals(message, that.message)
                && Objects.equals(commentId, that.commentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, commentId);
    }

    @Override
    public String toString() {
        return "CommentDeletionResponsePayload{" +
                "message='" + message + '\'' +
                ", commentId='" + commentId + '\'' +
                '}';
    }

    public static class Builder {
        private String message;
        private String commentId;

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder commentId(String commentId) {
            this.commentId = commentId;
            return this;
        }

        public CommentDeletionResponsePayload build() {
            return new CommentDeletionResponsePayload(this);
        }
    }
}
