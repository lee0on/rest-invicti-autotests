package org.example.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "post")
public class Post {

    @JsonProperty
    private String user_id;
    @JsonProperty
    private String title;
    @JsonProperty
    private String content;

    public Post() {}

    public Post(String user_id, String title, String content){
        this.user_id = user_id;
        this.title = title;
        this.content = content;
    }

    @XmlElement
    public String getUser_id() {
        return user_id;
    }

    @XmlElement
    public String getTitle() {
        return title;
    }

    @XmlElement
    public String getContent() {
        return content;
    }
}
