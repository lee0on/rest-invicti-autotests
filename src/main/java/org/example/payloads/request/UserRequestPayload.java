package org.example.payloads.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "user")
public class UserRequestPayload {

    @JsonProperty("username")
    @JacksonXmlProperty(localName = "username")
    private String username;

    @JsonProperty("email")
    @JacksonXmlProperty(localName = "email")
    private String email;

    @JsonProperty("password")
    @JacksonXmlProperty(localName = "password")
    private String password;

    @JsonProperty("first_name")
    @JacksonXmlProperty(localName = "first_name")
    private String first_name;

    @JsonProperty("last_name")
    @JacksonXmlProperty(localName = "last_name")
    private String last_name;

    public UserRequestPayload() {}

    private UserRequestPayload(Builder builder) {
        this.username = builder.username;
        this.email = builder.email;
        this.password = builder.password;
        this.first_name = builder.first_name;
        this.last_name = builder.last_name;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRequestPayload that = (UserRequestPayload) o;
        return Objects.equals(username, that.username)
                && Objects.equals(email, that.email)
                && Objects.equals(password, that.password)
                && Objects.equals(first_name, that.first_name)
                && Objects.equals(last_name, that.last_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, password, first_name, last_name);
    }

    @Override
    public String toString() {
        return "UserRequestPayload{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                '}';
    }

    public static class Builder {
        private String username;
        private String email;
        private String password;
        private String first_name;
        private String last_name;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder firstName(String first_name) {
            this.first_name = first_name;
            return this;
        }

        public Builder lastName(String last_name) {
            this.last_name = last_name;
            return this;
        }

        public UserRequestPayload build() {
            return new UserRequestPayload(this);
        }
    }
}
