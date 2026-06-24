package org.example.payloads.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "response")
public class EditCommentResponsePayload {

    @JsonProperty("comment")
    @JacksonXmlProperty(localName = "comment")
    private CommentResponsePayload comment;

    @JsonProperty("response")
    @JacksonXmlProperty(localName = "response")
    private String response;

    public EditCommentResponsePayload() {}

    private EditCommentResponsePayload(Builder builder) {
        this.comment = builder.comment;
        this.response = builder.response;
    }

    public static Builder builder() {
        return new Builder();
    }

    public CommentResponsePayload getComment() {
        return comment;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EditCommentResponsePayload that = (EditCommentResponsePayload) o;
        return Objects.equals(comment, that.comment)
                && Objects.equals(response, that.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment, response);
    }

    @Override
    public String toString() {
        return "EditCommentResponsePayload{" +
                "comment=" + comment +
                ", response='" + response + '\'' +
                '}';
    }

    public static class Builder {
        private CommentResponsePayload comment;
        private String response;

        public Builder comment(CommentResponsePayload comment) {
            this.comment = comment;
            return this;
        }

        public Builder response(String response) {
            this.response = response;
            return this;
        }

        public EditCommentResponsePayload build() {
            return new EditCommentResponsePayload(this);
        }
    }
}
