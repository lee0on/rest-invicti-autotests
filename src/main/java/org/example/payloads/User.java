package org.example.payloads;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement(name = "user")
public class User {

    @JsonProperty
    private String username;
    @JsonProperty
    private String email;
    @JsonProperty
    private String password;
    @JsonProperty
    private String first_name;
    @JsonProperty
    private String last_name;

    public User() {}

    public User(String username, String email, String password, String first_name, String last_name){
        this.username = username;
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    @XmlElement
    public String getUsername() {
        return username;
    }

    @XmlElement
    public String getEmail() {
        return email;
    }

    @XmlElement
    public String getPassword() {
        return password;
    }

    @XmlElement
    public String getFirst_name() {
        return first_name;
    }

    @XmlElement
    public String getLast_name() {
        return last_name;
    }

    @Override
    public String toString() {
        return "User{" +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + first_name + '\'' +
                ", lastName='" + last_name + '\'' +
                '}';
    }
}
