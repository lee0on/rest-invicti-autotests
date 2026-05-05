package org.example.payloads.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "post")
public class PostRequestPayload {

    @JsonProperty("user_id")
    @JacksonXmlProperty(localName = "user_id")
    private String userId;

    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title")
    private String title;

    @JsonProperty("content")
    @JacksonXmlProperty(localName = "content")
    private String content;

    public PostRequestPayload() {}

    private PostRequestPayload(Builder builder) {
        this.userId = builder.userId;
        this.title = builder.title;
        this.content = builder.content;
    }

    public static Builder builder() {
        return new Builder();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostRequestPayload that = (PostRequestPayload) o;
        return Objects.equals(userId, that.userId)
                && Objects.equals(title, that.title)
                && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, title, content);
    }

    @Override
    public String toString() {
        return "PostRequestPayload{" +
                "userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public static class Builder {
        private String userId;
        private String title;
        private String content;

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public PostRequestPayload build() {
            return new PostRequestPayload(this);
        }
    }
}