package org.example.checks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.example.payloads.request.CommentRequestPayload;
import org.example.payloads.response.CommentDeletionResponsePayload;
import org.example.payloads.response.CommentResponsePayload;
import org.example.payloads.response.EditCommentResponsePayload;
import org.example.payloads.response.GetCommentResponsePayload;
import org.example.requests.CommentApi;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.example.factories.EasyRandomFactory.commentGenerator;
import static org.junit.jupiter.api.Assertions.*;

public class CommentTest {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final XmlMapper XML_MAPPER = new XmlMapper();

    private CommentRequestPayload payload;
    private EasyRandom generator;
    private String createdCommentId;

    @BeforeEach
    public void setPayload() {
        generator = commentGenerator();
        payload = generator.nextObject(CommentRequestPayload.class);
    }

    @AfterEach
    public void cleanUp() {
        if (createdCommentId != null) {
            CommentApi.deleteComment(createdCommentId);
        }
    }

    @Test
    @DisplayName("Create JSON comment returns 200 with valid body")
    @Tag("smoke")
    public void createJsonCommentReturns200WithValidBody() throws JsonProcessingException {
        var commentResponse = CommentApi.createComment(payload);

        CommentResponsePayload createdComment = JSON_MAPPER.readValue(
                commentResponse.getBody().asString(), CommentResponsePayload.class);

        createdCommentId = createdComment.getCommentId();

        assertEquals(200, commentResponse.getStatusCode());
        assertEquals(payload.getUserId(), createdComment.getUserId());
        assertEquals(payload.getPostId(), createdComment.getPostId());
        assertEquals(payload.getComment(), createdComment.getComment());
        assertFalse(createdComment.getCommentId().isBlank(), "comment_id must be non-blank");
        assertFalse(createdComment.getCreatedAt().isBlank(), "created_at must be non-blank");
    }

    @Test
    @DisplayName("Create XML comment returns 200 with valid body")
    @Tag("smoke")
    public void createXmlCommentReturns200WithValidBody() throws JsonProcessingException {
        var commentResponse = CommentApi.createCommentXml(payload);

        CommentResponsePayload createdComment = XML_MAPPER.readValue(
                commentResponse.getBody().asString(), CommentResponsePayload.class);

        createdCommentId = createdComment.getCommentId();

        assertEquals(200, commentResponse.getStatusCode());
        assertEquals(payload.getUserId(), createdComment.getUserId());
        assertEquals(payload.getPostId(), createdComment.getPostId());
        assertEquals(payload.getComment(), createdComment.getComment());
    }

    @Test
    @DisplayName("Create comment without required fields returns 400")
    @Tag("negative")
    public void createCommentWithoutRequiredFieldsReturns400() {
        CommentRequestPayload invalidPayload = CommentRequestPayload.builder()
                .userId("")
                .postId("")
                .comment("")
                .build();

        var commentResponse = CommentApi.createComment(invalidPayload);

        assertEquals(400, commentResponse.getStatusCode());
    }

    @Test
    @DisplayName("Get all comments returns 200 and non-empty array")
    @Tag("smoke")
    public void getAllCommentsReturns200AndArray() throws JsonProcessingException {
        var getResponse = CommentApi.getAllComments();

        List<CommentResponsePayload> comments = JSON_MAPPER.readValue(
                getResponse.getBody().asString(), new TypeReference<>() {});

        assertEquals(200, getResponse.getStatusCode());
        assertFalse(comments.isEmpty());

        CommentResponsePayload firstComment = comments.getFirst();
        assertFalse(firstComment.getCommentId().isBlank(), "comment_id must be non-blank");
        assertFalse(firstComment.getUserId().isBlank(), "user_id must be non-blank");
        assertFalse(firstComment.getPostId().isBlank(), "post_id must be non-blank");
    }

    @Test
    @DisplayName("Get comment by ID returns 200 with matching data")
    @Tag("smoke")
    public void getCommentByIdReturns200WithMatchingData() throws JsonProcessingException {
        var createResponse = CommentApi.createComment(payload);
        CommentResponsePayload created = JSON_MAPPER.readValue(
                createResponse.getBody().asString(), CommentResponsePayload.class);
        createdCommentId = created.getCommentId();

        var getResponse = CommentApi.getComment(createdCommentId);

        GetCommentResponsePayload wrapper = JSON_MAPPER.readValue(
                getResponse.getBody().asString(), GetCommentResponsePayload.class);

        assertEquals(200, getResponse.getStatusCode());
        assertEquals(createdCommentId, wrapper.getCommentId());
        assertNotNull(wrapper.getComment(), "nested comment object must not be null");
        assertEquals(payload.getUserId(), wrapper.getComment().getUserId());
        assertEquals(payload.getComment(), wrapper.getComment().getComment());
    }

    @Test
    @DisplayName("Get non-existent comment returns 404")
    @Tag("negative")
    public void getNonExistentCommentReturns404() {
        var getResponse = CommentApi.getComment("999999");

        assertEquals(404, getResponse.getStatusCode());
    }

    @Test
    @DisplayName("Get comment with invalid ID returns 400")
    @Tag("negative")
    public void getCommentWithInvalidIdReturns400() {
        var getResponse = CommentApi.getComment("invalid_id");

        assertEquals(400, getResponse.getStatusCode());
    }

    @Test
    @DisplayName("Update JSON comment returns 200 with modified body")
    @Tag("regression")
    public void updateJsonCommentReturns200WithModifiedBody() throws JsonProcessingException {
        var createResponse = CommentApi.createComment(payload);
        CommentResponsePayload created = JSON_MAPPER.readValue(
                createResponse.getBody().asString(), CommentResponsePayload.class);
        createdCommentId = created.getCommentId();

        CommentRequestPayload updatedPayload = generator.nextObject(CommentRequestPayload.class);

        var putResponse = CommentApi.updateComment(createdCommentId, updatedPayload);

        EditCommentResponsePayload editResponse = JSON_MAPPER.readValue(
                putResponse.getBody().asString(), EditCommentResponsePayload.class);

        assertEquals(200, putResponse.getStatusCode());
        assertNotNull(editResponse.getComment(), "comment object in response must not be null");
        assertEquals(updatedPayload.getComment(), editResponse.getComment().getComment());
    }

    @Test
    @DisplayName("Update XML comment returns 200 with modified body")
    @Tag("regression")
    public void updateXmlCommentReturns200WithModifiedBody() throws JsonProcessingException {
        var createResponse = CommentApi.createCommentXml(payload);
        CommentResponsePayload created = XML_MAPPER.readValue(
                createResponse.getBody().asString(), CommentResponsePayload.class);
        createdCommentId = created.getCommentId();

        CommentRequestPayload updatedPayload = generator.nextObject(CommentRequestPayload.class);

        var putResponse = CommentApi.updateCommentXml(createdCommentId, updatedPayload);

        EditCommentResponsePayload editResponse = XML_MAPPER.readValue(
                putResponse.getBody().asString(), EditCommentResponsePayload.class);

        assertEquals(200, putResponse.getStatusCode());
        assertNotNull(editResponse.getComment(), "comment object in response must not be null");
        assertEquals(updatedPayload.getComment(), editResponse.getComment().getComment());
    }

    @Test
    @DisplayName("Update non-existent comment returns 404")
    @Tag("negative")
    public void updateNonExistentCommentReturns404() {
        CommentRequestPayload updatedPayload = generator.nextObject(CommentRequestPayload.class);

        var putResponse = CommentApi.updateComment("999999", updatedPayload);

        assertEquals(404, putResponse.getStatusCode());
    }

    @Test
    @DisplayName("Update comment with empty fields returns 400")
    @Tag("negative")
    public void updateCommentWithEmptyFieldsReturns400() throws JsonProcessingException {
        var createResponse = CommentApi.createComment(payload);
        CommentResponsePayload created = JSON_MAPPER.readValue(
                createResponse.getBody().asString(), CommentResponsePayload.class);
        createdCommentId = created.getCommentId();

        CommentRequestPayload invalidPayload = CommentRequestPayload.builder()
                .userId("")
                .postId("")
                .comment("")
                .build();

        var putResponse = CommentApi.updateComment(createdCommentId, invalidPayload);

        assertEquals(400, putResponse.getStatusCode());
    }

    @Test
    @DisplayName("Delete comment returns 200 with confirmation message")
    @Tag("smoke")
    public void deleteCommentReturns200WithConfirmation() throws JsonProcessingException {
        var createResponse = CommentApi.createComment(payload);
        CommentResponsePayload created = JSON_MAPPER.readValue(
                createResponse.getBody().asString(), CommentResponsePayload.class);
        String commentId = created.getCommentId();

        var deleteResponse = CommentApi.deleteComment(commentId);

        CommentDeletionResponsePayload deletedComment = XML_MAPPER.readValue(
                deleteResponse.getBody().asString(), CommentDeletionResponsePayload.class);

        assertEquals(200, deleteResponse.getStatusCode());
        assertEquals("comment deleted.", deletedComment.getMessage());
        assertEquals(commentId, deletedComment.getCommentId());

        createdCommentId = null;
    }

    @Test
    @DisplayName("Double deletion returns 200 then 404")
    @Tag("regression")
    public void doubleDeletionReturns200Then404() throws JsonProcessingException {
        var createResponse = CommentApi.createComment(payload);
        CommentResponsePayload created = JSON_MAPPER.readValue(
                createResponse.getBody().asString(), CommentResponsePayload.class);
        String commentId = created.getCommentId();

        var firstDelete = CommentApi.deleteComment(commentId);
        var secondDelete = CommentApi.deleteComment(commentId);

        assertEquals(200, firstDelete.getStatusCode());
        assertEquals(404, secondDelete.getStatusCode());

        createdCommentId = null;
    }

    @Test
    @DisplayName("Delete non-existent comment returns 404")
    @Tag("negative")
    public void deleteNonExistentCommentReturns404() {
        var deleteResponse = CommentApi.deleteComment("999999");

        assertEquals(404, deleteResponse.getStatusCode());
    }

    @Test
    @DisplayName("Update comment reflects changes on subsequent GET")
    @Tag("regression")
    public void updateCommentReflectsChangesOnGet() throws JsonProcessingException {
        var createResponse = CommentApi.createComment(payload);
        CommentResponsePayload created = JSON_MAPPER.readValue(
                createResponse.getBody().asString(), CommentResponsePayload.class);
        createdCommentId = created.getCommentId();

        CommentRequestPayload updatedPayload = generator.nextObject(CommentRequestPayload.class);
        CommentApi.updateComment(createdCommentId, updatedPayload);

        var getResponse = CommentApi.getComment(createdCommentId);
        GetCommentResponsePayload wrapper = JSON_MAPPER.readValue(
                getResponse.getBody().asString(), GetCommentResponsePayload.class);

        assertEquals(200, getResponse.getStatusCode());
        assertEquals(updatedPayload.getComment(), wrapper.getComment().getComment());
    }
}
