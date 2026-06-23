package org.example.payloads.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "response")
public class GetCommentResponsePayload {

    @JsonProperty("comment_id")
    @JacksonXmlProperty(localName = "comment_id")
    private String commentId;

    @JsonProperty("comment")
    @JacksonXmlProperty(localName = "comment")
    private CommentResponsePayload comment;

    public GetCommentResponsePayload() {}

    public String getCommentId() {
        return commentId;
    }

    public CommentResponsePayload getComment() {
        return comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetCommentResponsePayload that = (GetCommentResponsePayload) o;
        return Objects.equals(commentId, that.commentId)
                && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, comment);
    }

    @Override
    public String toString() {
        return "GetCommentResponsePayload{" +
                "commentId='" + commentId + '\'' +
                ", comment=" + comment +
                '}';
    }
}
