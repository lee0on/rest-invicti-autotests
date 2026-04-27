package org.example.checks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.response.Response;
import org.example.payloads.request.UserRequestPayload;
import org.example.payloads.response.UserDeletionResponsePayload;
import org.example.payloads.response.UserResponsePayload;
import org.example.requests.UserApi;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.*;

import static org.example.factories.EasyRandomFactory.userGenerator;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final XmlMapper XML_MAPPER = new XmlMapper();

    private UserRequestPayload payload;
    private EasyRandom generator;

    @BeforeEach
    public void setPayload() {
        generator = userGenerator();
        payload = generator.nextObject(UserRequestPayload.class);
    }

    @AfterEach
    public void cleanUp() {
        UserApi.deleteUser(payload.getUsername());
    }

    @Test
    @DisplayName("Create JSON user returns 200 with valid body")
    @Tag("smoke")
    public void createdJSONUserReturns200WithValidBody() throws JsonProcessingException {
        Response postResponse = UserApi.createUser(payload);

        UserResponsePayload createdUser = JSON_MAPPER.readValue(
                postResponse.getBody().asString(), UserResponsePayload.class);

        assertEquals(200, postResponse.getStatusCode());
        assertEquals(payload.getEmail(), createdUser.getEmail());
        assertEquals(payload.getUsername(), createdUser.getUsername());

        Response getResponse = UserApi.getUser(payload.getUsername());

        assertEquals(200, getResponse.getStatusCode());
    }

    @Test
    @DisplayName("Create XML user returns 200 with valid body")
    @Tag("smoke")
    public void createdXMLUserReturns200WithValidBody() throws JsonProcessingException {
        Response postResponse = UserApi.createUserXml(payload);

        UserResponsePayload createdUser = XML_MAPPER.readValue(
                postResponse.getBody().asString(), UserResponsePayload.class);

        assertEquals(200, postResponse.getStatusCode());
        assertEquals(payload.getEmail(), createdUser.getEmail());
        assertEquals(payload.getUsername(), createdUser.getUsername());

        Response getResponse = UserApi.getUser(payload.getUsername());

        assertEquals(200, getResponse.getStatusCode());
    }

    @Test
    @DisplayName("Create user without required fields returns 400")
    @Tag("negative")
    public void createdUserWithoutRequiredFieldsReturns400andBody() {
        UserRequestPayload invalidPayload = UserRequestPayload.builder()
                .username("")
                .email("")
                .password("")
                .firstName(payload.getFirst_name())
                .lastName(payload.getLast_name())
                .build();

        Response postResponse = UserApi.createUser(invalidPayload);

        assertEquals(400, postResponse.getStatusCode());
        assertEquals("false", postResponse.getBody().asString());
    }

    @Test
    @DisplayName("Get all users returns 200 and array of users")
    @Tag("smoke")
    public void getAllUsersReturns200AndArray() throws JsonProcessingException {
        Response getResponse = UserApi.getAllUsers();

        List<UserResponsePayload> users = JSON_MAPPER.readValue(
                getResponse.getBody().asString(), new TypeReference<>() {});

        assertEquals(200, getResponse.getStatusCode());
        assertFalse(users.isEmpty());

        UserResponsePayload firstUser = users.getFirst();
        assertFalse(firstUser.getUsername().isBlank(), "username must be non-blank");
        assertFalse(firstUser.getEmail().isBlank(), "email must be non-blank");
    }

    @Test
    @DisplayName("Get unknown user returns 404")
    @Tag("negative")
    public void getUnknownUserReturns404AndBody() {
        Response getResponse = UserApi.getUser("unknownUser");

        assertEquals(404, getResponse.getStatusCode());
        assertEquals("false", getResponse.getBody().asString());
    }

    @Test
    @DisplayName("Get user with empty username returns 400")
    @Tag("negative")
    public void getEmptyUserReturns400AndBody() {
        Response getResponse = UserApi.getUser("");

        assertEquals(400, getResponse.getStatusCode());
        assertEquals("false", getResponse.getBody().asString());
    }

    @Test
    @DisplayName("Update JSON user returns 200 with modified body")
    @Tag("regression")
    public void putJSONUserReturns200andBody() throws JsonProcessingException {
        UserApi.createUser(payload);

        UserRequestPayload newPayload = generator.nextObject(UserRequestPayload.class);

        Response putResponse = UserApi.updateUser(payload.getUsername(), newPayload);

        UserResponsePayload modifiedUser = JSON_MAPPER.readValue(
                putResponse.getBody().asString(), UserResponsePayload.class);

        assertEquals(200, putResponse.getStatusCode());
        assertEquals(newPayload.getEmail(), modifiedUser.getEmail());
        assertEquals(newPayload.getFirst_name(), modifiedUser.getFirst_name());
    }

    @Test
    @DisplayName("Update XML user returns 200 with modified body")
    @Tag("regression")
    public void putXMLUserReturns200andBody() throws JsonProcessingException {
        UserApi.createUserXml(payload);

        UserRequestPayload newPayload = generator.nextObject(UserRequestPayload.class);

        Response putResponse = UserApi.updateUserXml(payload.getUsername(), newPayload);

        UserResponsePayload modifiedUser = XML_MAPPER.readValue(
                putResponse.getBody().asString(), UserResponsePayload.class);

        assertEquals(200, putResponse.getStatusCode());
        assertEquals(newPayload.getUsername(), modifiedUser.getUsername());
    }

    @Test
    @DisplayName("Delete user returns 200 with confirmation message")
    @Tag("smoke")
    public void deleteXMLUserReturns200andBody() throws JsonProcessingException {
        UserApi.createUser(payload);
        Response deleteResponse = UserApi.deleteUser(payload.getUsername());

        UserDeletionResponsePayload deletedUser = XML_MAPPER.readValue(
                deleteResponse.getBody().asString(), UserDeletionResponsePayload.class);

        assertEquals(200, deleteResponse.getStatusCode());
        assertEquals("user deleted.", deletedUser.getMessage());
        assertEquals(payload.getUsername(), deletedUser.getUsername());
    }

    @Test
    @DisplayName("Double deletion returns 200 then 404")
    @Tag("regression")
    public void doubleDeletionReturns200then404() {
        UserApi.createUser(payload);
        Response deleteResponse = UserApi.deleteUser(payload.getUsername());

        Response secondDeleteResponse = UserApi.deleteUser(payload.getUsername());

        assertEquals(200, deleteResponse.getStatusCode());
        assertEquals(404, secondDeleteResponse.getStatusCode());
    }

    @Test
    @DisplayName("Create user with duplicate username returns error")
    @Tag("negative")
    public void createDuplicateUserReturnsError() {
        UserApi.createUser(payload);

        UserRequestPayload duplicatePayload = UserRequestPayload.builder()
                .username(payload.getUsername())
                .email(generator.nextObject(UserRequestPayload.class).getEmail())
                .password(payload.getPassword())
                .firstName(payload.getFirst_name())
                .lastName(payload.getLast_name())
                .build();

        Response duplicateResponse = UserApi.createUser(duplicatePayload);

        assertTrue(duplicateResponse.getStatusCode() == 400 || duplicateResponse.getStatusCode() == 409,
                "Duplicate username should return 400 or 409, got: " + duplicateResponse.getStatusCode());
    }

    @Test
    @DisplayName("Create JSON user returns response with user_id and created_at")
    @Tag("regression")
    public void createUserReturnsAllResponseFields() throws JsonProcessingException {
        Response postResponse = UserApi.createUser(payload);

        UserResponsePayload createdUser = JSON_MAPPER.readValue(
                postResponse.getBody().asString(), UserResponsePayload.class);

        assertEquals(200, postResponse.getStatusCode());
        assertEquals(payload.getUsername(), createdUser.getUsername());
        assertEquals(payload.getEmail(), createdUser.getEmail());
        assertEquals(payload.getFirst_name(), createdUser.getFirst_name());
        assertEquals(payload.getLast_name(), createdUser.getLast_name());
        assertFalse(createdUser.getUser_id().isBlank(), "user_id must be non-blank in response");
        assertFalse(createdUser.getCreated_at().isBlank(), "created_at must be non-blank in response");
    }

    @Test
    @DisplayName("Get user by username returns full matching data")
    @Tag("regression")
    public void getUserReturnsFullMatchingData() throws JsonProcessingException {
        UserApi.createUser(payload);

        Response getResponse = UserApi.getUser(payload.getUsername());

        UserResponsePayload fetchedUser = JSON_MAPPER.readValue(
                getResponse.getBody().asString(), UserResponsePayload.class);

        assertEquals(200, getResponse.getStatusCode());
        assertEquals(payload.getUsername(), fetchedUser.getUsername());
        assertEquals(payload.getEmail(), fetchedUser.getEmail());
        assertEquals(payload.getFirst_name(), fetchedUser.getFirst_name());
        assertEquals(payload.getLast_name(), fetchedUser.getLast_name());
        assertFalse(fetchedUser.getUser_id().isBlank(), "user_id must be non-blank");
    }

    @Test
    @DisplayName("Update non-existent user returns 404")
    @Tag("negative")
    public void updateNonExistentUserReturns404() {
        UserRequestPayload newPayload = generator.nextObject(UserRequestPayload.class);

        Response putResponse = UserApi.updateUser("nonExistentUser_xyz", newPayload);

        assertEquals(404, putResponse.getStatusCode());
    }

    @Test
    @DisplayName("Update user with empty required fields returns 400")
    @Tag("negative")
    public void updateUserWithEmptyFieldsReturns400() {
        UserApi.createUser(payload);

        UserRequestPayload invalidPayload = UserRequestPayload.builder()
                .username("")
                .email("")
                .password("")
                .firstName("")
                .lastName("")
                .build();

        Response putResponse = UserApi.updateUser(payload.getUsername(), invalidPayload);

        assertEquals(400, putResponse.getStatusCode());
    }

    @Test
    @DisplayName("Update user reflects changes on subsequent GET")
    @Tag("regression")
    public void updateUserReflectsChangesOnGet() throws JsonProcessingException {
        UserApi.createUser(payload);

        UserRequestPayload updatedPayload = generator.nextObject(UserRequestPayload.class);

        UserApi.updateUser(payload.getUsername(), updatedPayload);

        Response getResponse = UserApi.getUser(updatedPayload.getUsername());

        UserResponsePayload fetchedUser = JSON_MAPPER.readValue(
                getResponse.getBody().asString(), UserResponsePayload.class);

        assertEquals(200, getResponse.getStatusCode());
        assertEquals(updatedPayload.getEmail(), fetchedUser.getEmail());
        assertEquals(updatedPayload.getFirst_name(), fetchedUser.getFirst_name());
        assertEquals(updatedPayload.getLast_name(), fetchedUser.getLast_name());
    }

    @Test
    @DisplayName("Delete non-existent user returns 404")
    @Tag("negative")
    public void deleteNonExistentUserReturns404() {
        Response deleteResponse = UserApi.deleteUser("nonExistentUser_xyz");

        assertEquals(404, deleteResponse.getStatusCode());
    }

    @Test
    @DisplayName("Create XML user without required fields returns 400")
    @Tag("negative")
    public void createXmlUserWithoutRequiredFieldsReturns400() {
        UserRequestPayload invalidPayload = UserRequestPayload.builder()
                .username("")
                .email("")
                .password("")
                .firstName(payload.getFirst_name())
                .lastName(payload.getLast_name())
                .build();

        Response postResponse = UserApi.createUserXml(invalidPayload);

        assertEquals(400, postResponse.getStatusCode());
    }

    @Test
    @DisplayName("Concurrent deletion and update handles race condition")
    @Tag("regression")
    public void concurrentDeletionAndUpdate() throws Exception {
        UserApi.createUser(payload);

        UserRequestPayload newPayload = generator.nextObject(UserRequestPayload.class);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            CountDownLatch startLatch = new CountDownLatch(1);

            Future<Response> deleteFuture = executor.submit(() -> {
                startLatch.await();
                return UserApi.deleteUser(payload.getUsername());
            });

            Future<Response> updateFuture = executor.submit(() -> {
                startLatch.await();
                return UserApi.updateUser(payload.getUsername(), newPayload);
            });

            startLatch.countDown();

            Response deleteResponse = deleteFuture.get(10, TimeUnit.SECONDS);
            Response updateResponse = updateFuture.get(10, TimeUnit.SECONDS);

            assertNotNull(deleteResponse, "Delete response must not be null");
            assertNotNull(updateResponse, "Update response must not be null");

            int deleteStatus = deleteResponse.getStatusCode();
            int updateStatus = updateResponse.getStatusCode();

            if (deleteStatus == 200) {
                assertEquals(404, updateStatus,
                        "After successful deletion, update should return 404");
            } else if (updateStatus == 200) {
                assertEquals(404, deleteStatus,
                        "After successful update, deletion should return 404");
            } else {
                fail("Unexpected statuses. Delete: " + deleteStatus +
                        ", Update: " + updateStatus);
            }
        } finally {
            executor.shutdown();
            boolean terminated = executor.awaitTermination(5, TimeUnit.SECONDS);
            assertTrue(terminated, "Worker threads did not terminate within 5 seconds");
        }
    }
}
