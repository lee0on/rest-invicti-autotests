package org.example.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "response")
public class PostResponse {

    @JsonProperty
    private String user_id;
    @JsonProperty
    private String title;
    @JsonProperty
    private String content;
    @JsonProperty
    private String created_at;
    @JsonProperty
    private String post_id;

    public PostResponse() {}

    public PostResponse(String user_id, String title, String content, String created_at, String post_id){
        this.user_id = user_id;
        this.title = title;
        this.content = content;
        this.created_at = created_at;
        this.post_id = post_id;
    }

    @JacksonXmlProperty
    public String getUser_id() {
        return user_id;
    }

    @JacksonXmlProperty
    public String getTitle() {
        return title;
    }

    @JacksonXmlProperty
    public String getContent() {
        return content;
    }

    @JacksonXmlProperty
    public String getCreated_at() {
        return created_at;
    }

    @JacksonXmlProperty
    public String getPost_id() {
        return post_id;
    }
}
