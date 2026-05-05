package org.example.checks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.example.payloads.request.PostRequestPayload;
import org.example.payloads.response.EditPostResponsePayload;
import org.example.payloads.response.GetPostResponsePayload;
import org.example.payloads.response.PostDeletionResponsePayload;
import org.example.payloads.response.PostResponsePayload;
import org.example.requests.PostApi;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.example.factories.EasyRandomFactory.postGenerator;
import static org.junit.jupiter.api.Assertions.*;

public class PostTest {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final XmlMapper XML_MAPPER = new XmlMapper();

    private PostRequestPayload payload;
    private EasyRandom generator;
    private String createdPostId;

    @BeforeEach
    public void setPayload() {
        generator = postGenerator();
        payload = generator.nextObject(PostRequestPayload.class);
    }

    @AfterEach
    public void cleanUp() {
        if (createdPostId != null) {
            PostApi.deletePost(createdPostId);
        }
    }

    @Test
    @DisplayName("Create JSON post returns 200 with valid body")
    @Tag("smoke")
    public void createJsonPostReturns200WithValidBody() throws JsonProcessingException {
        var postResponse = PostApi.createPost(payload);

        PostResponsePayload createdPost = JSON_MAPPER.readValue(
                postResponse.getBody().asString(), PostResponsePayload.class);

        createdPostId = createdPost.getPostId();

        assertEquals(200, postResponse.getStatusCode());
        assertEquals(payload.getUserId(), createdPost.getUserId());
        assertEquals(payload.getTitle(), createdPost.getTitle());
        assertEquals(payload.getContent(), createdPost.getContent());
        assertFalse(createdPost.getPostId().isBlank(), "post_id must be non-blank");
        assertFalse(createdPost.getCreatedAt().isBlank(), "created_at must be non-blank");
    }

    @Test
    @DisplayName("Create XML post returns 200 with valid body")
    @Tag("smoke")
    public void createXmlPostReturns200WithValidBody() throws JsonProcessingException {
        var postResponse = PostApi.createPostXml(payload);

        PostResponsePayload createdPost = XML_MAPPER.readValue(
                postResponse.getBody().asString(), PostResponsePayload.class);

        createdPostId = createdPost.getPostId();

        assertEquals(200, postResponse.getStatusCode());
        assertEquals(payload.getUserId(), createdPost.getUserId());
        assertEquals(payload.getTitle(), createdPost.getTitle());
        assertEquals(payload.getContent(), createdPost.getContent());
    }

    @Test
    @DisplayName("Create post without required fields returns 400")
    @Tag("negative")
    public void createPostWithoutRequiredFieldsReturns400() {
        PostRequestPayload invalidPayload = PostRequestPayload.builder()
                .userId("")
                .title("")
                .content("")
                .build();

        var postResponse = PostApi.createPost(invalidPayload);

        assertEquals(400, postResponse.getStatusCode());
    }

    @Test
    @DisplayName("Get all posts returns 200 and non-empty array")
    @Tag("smoke")
    public void getAllPostsReturns200AndArray() throws JsonProcessingException {
        var getResponse = PostApi.getAllPosts();

        List<PostResponsePayload> posts = JSON_MAPPER.readValue(
                getResponse.getBody().asString(), new TypeReference<>() {});

        assertEquals(200, getResponse.getStatusCode());
        assertFalse(posts.isEmpty());

        PostResponsePayload firstPost = posts.getFirst();
        assertFalse(firstPost.getPostId().isBlank(), "post_id must be non-blank");
        assertFalse(firstPost.getUserId().isBlank(), "user_id must be non-blank");
        assertFalse(firstPost.getTitle().isBlank(), "title must be non-blank");
    }

    @Test
    @DisplayName("Get post by ID returns 200 with matching data")
    @Tag("smoke")
    public void getPostByIdReturns200WithMatchingData() throws JsonProcessingException {
        var createResponse = PostApi.createPost(payload);
        PostResponsePayload created = JSON_MAPPER.readValue(
                createResponse.getBody().asString(), PostResponsePayload.class);
        createdPostId = created.getPostId();

        var getResponse = PostApi.getPost(createdPostId);

        GetPostResponsePayload wrapper = JSON_MAPPER.readValue(
                getResponse.getBody().asString(), GetPostResponsePayload.class);

        assertEquals(200, getResponse.getStatusCode());
        assertEquals(createdPostId, wrapper.getPostId());
        assertNotNull(wrapper.getPost(), "nested post object must not be null");
        assertEquals(payload.getTitle(), wrapper.getPost().getTitle());
        assertEquals(payload.getContent(), wrapper.getPost().getContent());
    }

    @Test
    @DisplayName("Get non-existent post returns 404")
    @Tag("negative")
    public void getNonExistentPostReturns404() {
        var getResponse = PostApi.getPost("999999");

        assertEquals(404, getResponse.getStatusCode());
    }

    @Test
    @DisplayName("Get post with invalid ID returns 400")
    @Tag("negative")
    public void getPostWithInvalidIdReturns400() {
        var getResponse = PostApi.getPost("invalid_id");

        assertEquals(400, getResponse.getStatusCode());
    }

    @Test
    @DisplayName("Update JSON post returns 200 with modified body")
    @Tag("regression")
    public void updateJsonPostReturns200WithModifiedBody() throws JsonProcessingException {
        var createResponse = PostApi.createPost(payload);
        PostResponsePayload created = JSON_MAPPER.readValue(
                createResponse.getBody().asString(), PostResponsePayload.class);
        createdPostId = created.getPostId();

        PostRequestPayload updatedPayload = generator.nextObject(PostRequestPayload.class);

        var putResponse = PostApi.updatePost(createdPostId, updatedPayload);

        EditPostResponsePayload editResponse = JSON_MAPPER.readValue(
                putResponse.getBody().asString(), EditPostResponsePayload.class);

        assertEquals(200, putResponse.getStatusCode());
        assertNotNull(editResponse.getPost(), "post object in response must not be null");
        assertEquals(updatedPayload.getTitle(), editResponse.getPost().getTitle());
        assertEquals(updatedPayload.getContent(), editResponse.getPost().getContent());
    }

    @Test
    @DisplayName("Update XML post returns 200 with modified body")
    @Tag("regression")
    public void updateXmlPostReturns200WithModifiedBody() throws JsonProcessingException {
        var createResponse = PostApi.createPostXml(payload);
        PostResponsePayload created = XML_MAPPER.readValue(
                createResponse.getBody().asString(), PostResponsePayload.class);
        createdPostId = created.getPostId();

        PostRequestPayload updatedPayload = generator.nextObject(PostRequestPayload.class);

        var putResponse = PostApi.updatePostXml(createdPostId, updatedPayload);

        EditPostResponsePayload editResponse = XML_MAPPER.readValue(
                putResponse.getBody().asString(), EditPostResponsePayload.class);

        assertEquals(200, putResponse.getStatusCode());
        assertNotNull(editResponse.getPost(), "post object in response must not be null");
        assertEquals(updatedPayload.getTitle(), editResponse.getPost().getTitle());
    }

    @Test
    @DisplayName("Update non-existent post returns 404")
    @Tag("negative")
    public void updateNonExistentPostReturns404() {
        PostRequestPayload updatedPayload = generator.nextObject(PostRequestPayload.class);

        var putResponse = PostApi.updatePost("999999", updatedPayload);

        assertEquals(404, putResponse.getStatusCode());
    }

    @Test
    @DisplayName("Update post with empty fields returns 400")
    @Tag("negative")
    public void updatePostWithEmptyFieldsReturns400() throws JsonProcessingException {
        var createResponse = PostApi.createPost(payload);
        PostResponsePayload created = JSON_MAPPER.readValue(
                createResponse.getBody().asString(), PostResponsePayload.class);
        createdPostId = created.getPostId();

        PostRequestPayload invalidPayload = PostRequestPayload.builder()
                .userId("")
                .title("")
                .content("")
                .build();

        var putResponse = PostApi.updatePost(createdPostId, invalidPayload);

        assertEquals(400, putResponse.getStatusCode());
    }

    @Test
    @DisplayName("Delete post returns 200 with confirmation message")
    @Tag("smoke")
    public void deletePostReturns200WithConfirmation() throws JsonProcessingException {
        var createResponse = PostApi.createPost(payload);
        PostResponsePayload created = JSON_MAPPER.readValue(
                createResponse.getBody().asString(), PostResponsePayload.class);
        String postId = created.getPostId();

        var deleteResponse = PostApi.deletePost(postId);

        PostDeletionResponsePayload deletedPost = XML_MAPPER.readValue(
                deleteResponse.getBody().asString(), PostDeletionResponsePayload.class);

        assertEquals(200, deleteResponse.getStatusCode());
        assertEquals("post deleted.", deletedPost.getMessage());
        assertEquals(postId, deletedPost.getPostId());

        createdPostId = null;
    }

    @Test
    @DisplayName("Double deletion returns 200 then 404")
    @Tag("regression")
    public void doubleDeletionReturns200Then404() throws JsonProcessingException {
        var createResponse = PostApi.createPost(payload);
        PostResponsePayload created = JSON_MAPPER.readValue(
                createResponse.getBody().asString(), PostResponsePayload.class);
        String postId = created.getPostId();

        var firstDelete = PostApi.deletePost(postId);
        var secondDelete = PostApi.deletePost(postId);

        assertEquals(200, firstDelete.getStatusCode());
        assertEquals(404, secondDelete.getStatusCode());

        createdPostId = null;
    }

    @Test
    @DisplayName("Delete non-existent post returns 404")
    @Tag("negative")
    public void deleteNonExistentPostReturns404() {
        var deleteResponse = PostApi.deletePost("999999");

        assertEquals(404, deleteResponse.getStatusCode());
    }

    @Test
    @DisplayName("Get post comments returns 200 and list")
    @Tag("smoke")
    public void getPostCommentsReturns200AndList() throws JsonProcessingException {
        var createResponse = PostApi.createPost(payload);
        PostResponsePayload created = JSON_MAPPER.readValue(
                createResponse.getBody().asString(), PostResponsePayload.class);
        createdPostId = created.getPostId();

        var commentsResponse = PostApi.getPostComments(createdPostId);

        assertEquals(200, commentsResponse.getStatusCode());
    }

    @Test
    @DisplayName("Get comments for non-existent post returns 404")
    @Tag("negative")
    public void getCommentsForNonExistentPostReturns404() {
        var commentsResponse = PostApi.getPostComments("999999");

        assertEquals(404, commentsResponse.getStatusCode());
    }

    @Test
    @DisplayName("Update post reflects changes on subsequent GET")
    @Tag("regression")
    public void updatePostReflectsChangesOnGet() throws JsonProcessingException {
        var createResponse = PostApi.createPost(payload);
        PostResponsePayload created = JSON_MAPPER.readValue(
                createResponse.getBody().asString(), PostResponsePayload.class);
        createdPostId = created.getPostId();

        PostRequestPayload updatedPayload = generator.nextObject(PostRequestPayload.class);
        PostApi.updatePost(createdPostId, updatedPayload);

        var getResponse = PostApi.getPost(createdPostId);
        GetPostResponsePayload wrapper = JSON_MAPPER.readValue(
                getResponse.getBody().asString(), GetPostResponsePayload.class);

        assertEquals(200, getResponse.getStatusCode());
        assertEquals(updatedPayload.getTitle(), wrapper.getPost().getTitle());
        assertEquals(updatedPayload.getContent(), wrapper.getPost().getContent());
    }
}
