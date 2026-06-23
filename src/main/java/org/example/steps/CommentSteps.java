package org.example.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.example.payloads.request.CommentRequestPayload;
import org.example.payloads.response.CommentDeletionResponsePayload;
import org.example.payloads.response.CommentResponsePayload;
import org.example.payloads.response.EditCommentResponsePayload;
import org.example.payloads.response.GetCommentResponsePayload;
import org.example.requests.CommentApi;
import org.jeasy.random.EasyRandom;

import java.util.List;

import static org.example.factories.EasyRandomFactory.commentGenerator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Cucumber step definitions for the Comment API.
 *
 * <p>Thin orchestration layer: each step delegates to {@link CommentApi},
 * {@link CommentRequestPayload} and {@code commentGenerator()}. No HTTP / REST Assured
 * logic lives here.</p>
 *
 * <p>Scenario state is kept in instance fields because no shared {@code TestContext}
 * exists yet.</p>
 *
 * <p>TODO: migrate shared scenario state to a DI-injected {@code TestContext}.</p>
 */
public class CommentSteps {

    private static final String NON_EXISTENT_COMMENT_ID = "999999";

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final XmlMapper xmlMapper = new XmlMapper();

    private EasyRandom generator;
    private CommentRequestPayload payload;
    private Response response;
    private String createdCommentId;

    @Before
    public void setUp() {
        generator = commentGenerator();
        payload = null;
        response = null;
        createdCommentId = null;
    }

    @Given("the comment API is available")
    public void theCommentApiIsAvailable() {
        // Connectivity precondition handled by CommentApi base configuration.
    }

    @Given("I am authenticated as an admin user")
    public void iAmAuthenticatedAsAnAdminUser() {
        // Authentication is performed inside CommentApi (basic auth) on every call.
    }

    @Given("I have valid comment details")
    public void iHaveValidCommentDetails() {
        payload = generator.nextObject(CommentRequestPayload.class);
    }

    @Given("I have comment details without the required fields")
    public void iHaveCommentDetailsWithoutTheRequiredFields() {
        payload = CommentRequestPayload.builder()
                .userId("")
                .postId("")
                .comment("")
                .build();
    }

    @Given("an existing comment")
    public void anExistingComment() throws JsonProcessingException {
        payload = generator.nextObject(CommentRequestPayload.class);
        response = CommentApi.createComment(payload);

        CommentResponsePayload created = jsonMapper.readValue(
                response.getBody().asString(), CommentResponsePayload.class);
        createdCommentId = created.getCommentId();
    }

    @When("I create a new comment")
    public void iCreateANewComment() throws JsonProcessingException {
        response = CommentApi.createComment(payload);

        if (response.getStatusCode() == 200) {
            CommentResponsePayload created = jsonMapper.readValue(
                    response.getBody().asString(), CommentResponsePayload.class);
            createdCommentId = created.getCommentId();
        }
    }

    @When("I retrieve the comment by its identifier")
    public void iRetrieveTheCommentByItsIdentifier() {
        response = CommentApi.getComment(createdCommentId);
    }

    @When("I retrieve all comments")
    public void iRetrieveAllComments() {
        response = CommentApi.getAllComments();
    }

    @When("I update the comment with new text")
    public void iUpdateTheCommentWithNewText() {
        payload = generator.nextObject(CommentRequestPayload.class);
        response = CommentApi.updateComment(createdCommentId, payload);
    }

    @When("I delete the comment")
    public void iDeleteTheComment() {
        response = CommentApi.deleteComment(createdCommentId);
    }

    @When("I retrieve a comment that does not exist")
    public void iRetrieveACommentThatDoesNotExist() {
        response = CommentApi.getComment(NON_EXISTENT_COMMENT_ID);
    }

    @Then("the comment is created successfully")
    public void theCommentIsCreatedSuccessfully() throws JsonProcessingException {
        assertEquals(200, response.getStatusCode());

        CommentResponsePayload created = jsonMapper.readValue(
                response.getBody().asString(), CommentResponsePayload.class);
        assertEquals(payload.getUserId(), created.getUserId());
        assertEquals(payload.getPostId(), created.getPostId());
        assertEquals(payload.getComment(), created.getComment());
    }

    @Then("the response contains the comment id")
    public void theResponseContainsTheCommentId() throws JsonProcessingException {
        CommentResponsePayload created = jsonMapper.readValue(
                response.getBody().asString(), CommentResponsePayload.class);
        assertNotNull(created.getCommentId(), "comment_id must be present");
        assertFalse(created.getCommentId().isBlank(), "comment_id must be non-blank");
    }

    @Then("the requested comment is returned")
    public void theRequestedCommentIsReturned() throws JsonProcessingException {
        assertEquals(200, response.getStatusCode());

        GetCommentResponsePayload wrapper = jsonMapper.readValue(
                response.getBody().asString(), GetCommentResponsePayload.class);
        assertEquals(createdCommentId, wrapper.getCommentId());
        assertNotNull(wrapper.getComment(), "nested comment object must not be null");
        assertEquals(payload.getComment(), wrapper.getComment().getComment());
    }

    @Then("the list of comments is not empty")
    public void theListOfCommentsIsNotEmpty() throws JsonProcessingException {
        assertEquals(200, response.getStatusCode());

        List<CommentResponsePayload> comments = jsonMapper.readValue(
                response.getBody().asString(), new TypeReference<>() {});
        assertFalse(comments.isEmpty(), "comment list must not be empty");
    }

    @Then("I receive a bad request error")
    public void iReceiveABadRequestError() {
        assertEquals(400, response.getStatusCode());
    }

    @Then("the comment text is updated")
    public void theCommentTextIsUpdated() throws JsonProcessingException {
        assertEquals(200, response.getStatusCode());

        EditCommentResponsePayload editResponse = jsonMapper.readValue(
                response.getBody().asString(), EditCommentResponsePayload.class);
        assertNotNull(editResponse.getComment(), "comment object in response must not be null");
        assertEquals(payload.getComment(), editResponse.getComment().getComment());
    }

    @Then("the comment is removed with a confirmation")
    public void theCommentIsRemovedWithAConfirmation() throws JsonProcessingException {
        assertEquals(200, response.getStatusCode());

        CommentDeletionResponsePayload deleted = xmlMapper.readValue(
                response.getBody().asString(), CommentDeletionResponsePayload.class);
        assertEquals(createdCommentId, deleted.getCommentId());
        assertEquals("comment deleted.", deleted.getMessage());

        createdCommentId = null;
    }

    @Then("I receive a not found error")
    public void iReceiveANotFoundError() {
        assertEquals(404, response.getStatusCode());
    }
}
