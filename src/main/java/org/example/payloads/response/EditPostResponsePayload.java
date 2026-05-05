package org.example.payloads.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "response")
public class EditPostResponsePayload {

    @JsonProperty("post")
    @JacksonXmlProperty(localName = "post")
    private PostResponsePayload post;

    @JsonProperty("response")
    @JacksonXmlProperty(localName = "response")
    private String response;

    public EditPostResponsePayload() {}

    public PostResponsePayload getPost() {
        return post;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EditPostResponsePayload that = (EditPostResponsePayload) o;
        return Objects.equals(post, that.post)
                && Objects.equals(response, that.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(post, response);
    }

    @Override
    public String toString() {
        return "EditPostResponsePayload{" +
                "post=" + post +
                ", response='" + response + '\'' +
                '}';
    }
}