package org.example.checks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.response.Response;
import org.example.XMLUtils;
import org.example.payloads.User;
import org.example.payloads.UserDeletionResponse;
import org.example.payloads.UserResponse;
import org.example.requests.UserApi;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.*;

import static org.example.EasyRandomFactory.userGenerator;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User payload;
    private EasyRandom generator;

    @BeforeEach
    public void setPayload(){
        generator = userGenerator();
        payload = generator.nextObject(User.class);
    }

    @AfterEach
    public void cleanUp(){
        UserApi.deleteXMLUser(payload.getUsername());
    }

    @Test
    public void createdJSONUserReturns200WithValidBody() throws JsonProcessingException {
        Response postResponse = UserApi.postUserJSON(payload);

        String responseBody = postResponse.getBody().asString();
        ObjectMapper mapper = new ObjectMapper();
        UserResponse createdUser = mapper.readValue(responseBody, UserResponse.class);

        assertEquals(200, postResponse.getStatusCode());
        assertEquals(payload.getEmail(), createdUser.getEmail());
        assertEquals(payload.getUsername(), createdUser.getUsername());

        Response getResponse = UserApi.getUserByName(payload.getUsername());

        assertEquals(200, getResponse.getStatusCode());
    }

    @Test
    public void createdXMLUserReturns200WithValidBody() throws Exception {
        String xmlBody = XMLUtils.userToXml(payload);

        Response postResponse = UserApi.postUserXML(xmlBody);

        String responseBody = postResponse.getBody().asString();
        XmlMapper mapper = new XmlMapper();
        UserResponse createdUser = mapper.readValue(responseBody, UserResponse.class);


        assertEquals(200, postResponse.getStatusCode());
        assertEquals("testuser@example.com", createdUser.getEmail());
        assertEquals("testuser123", createdUser.getUsername());

        Response getResponse = UserApi.getUserByName(payload.getUsername());

        assertEquals(200, getResponse.getStatusCode());
    }

    @Test
    public void createdUserWithoutRequiredFieldsReturns400andBody(){
        User payload = new User(
                "",
                "",
                "",
                "Test",
                "User"
        );

        Response postResponse = UserApi.postUserJSON(payload);

        assertEquals(400, postResponse.getStatusCode());
        assertEquals("false", postResponse.getBody().asString());
    }

    @Test
    public void getAllUsersReturns200AndArray() throws JsonProcessingException {
        Response getResponse = UserApi.getUsers();

        String responseBody = getResponse.getBody().asString();
        ObjectMapper mapper = new ObjectMapper();
        List<User> users = mapper.readValue(responseBody, new TypeReference<>() {});

        User firstUser = users.getFirst();
        boolean userExists = users.stream()
                .anyMatch(user -> user.getUsername().equals("pgorczany"));

        assertEquals(200, getResponse.getStatusCode());
        assertTrue(userExists);
        assertNotNull(firstUser.getUsername());
        assertNotNull(firstUser.getEmail());
        assertFalse(users.isEmpty());
        assertTrue(users.size() > 20);
    }

    @Test
    public void getUnknownUserReturns404AndBody(){
        Response getResponse = UserApi.getUserByName("unknownUser");

        assertEquals(404, getResponse.getStatusCode());
        assertEquals("false", getResponse.getBody().asString());
    }

    @Test
    public void getEmptyUserReturns400AndBody(){
        Response getResponse = UserApi.getUserByName("");

        assertEquals(400, getResponse.getStatusCode());
        assertEquals("false", getResponse.getBody().asString());
    }

    @Test
    public void putJSONUserReturns200andBody() throws JsonProcessingException {
        Response postResponse = UserApi.postUserJSON(payload);

        User newPayload = generator.nextObject(User.class);

        Response putResponse = UserApi.putUserJSON(payload.getUsername(), newPayload);

        String responseBody = putResponse.getBody().asString();
        ObjectMapper mapper = new ObjectMapper();
        UserResponse modifiedUser = mapper.readValue(responseBody, UserResponse.class);

        assertEquals(200, putResponse.getStatusCode());
        assertEquals(newPayload.getEmail(), modifiedUser.getEmail());
        assertEquals(newPayload.getFirst_name(), modifiedUser.getFirst_name());
    }

    @Test
    public void putXMLUserReturns200andBody() throws Exception {
        String xmlBody = XMLUtils.userToXml(payload);
        UserApi.postUserXML(xmlBody);

        User newPayload = generator.nextObject(User.class);
        String newXmlBody = XMLUtils.userToXml(newPayload);

        Response putResponse = UserApi.putUserXML(payload.getUsername(),newXmlBody);

        String responseBody = putResponse.getBody().asString();
        XmlMapper mapper = new XmlMapper();
        UserResponse modifiedUser = mapper.readValue(responseBody, UserResponse.class);

        assertEquals(200, putResponse.getStatusCode());
        assertEquals(newPayload.getUsername(), modifiedUser.getUsername());
    }

    @Test
    public void deleteXMLUserReturns200andBody() throws JsonProcessingException {
        UserApi.postUserJSON(payload);
        Response deleteResponse = UserApi.deleteXMLUser(payload.getUsername());

        String responseBody = deleteResponse.getBody().asString();
        XmlMapper mapper = new XmlMapper();
        UserDeletionResponse deletedUser = mapper.readValue(responseBody, UserDeletionResponse.class);

        assertEquals(200, deleteResponse.getStatusCode());
        assertEquals("user deleted.", deletedUser.getMessage());
        assertEquals(payload.getUsername(), deletedUser.getUsername());
    }

    @Test
    public void doubleDeletionReturns200tnen404(){
        UserApi.postUserJSON(payload);
        Response deleteResponse = UserApi.deleteXMLUser(payload.getUsername());

        Response secondDeleteResponse = UserApi.deleteXMLUser(payload.getUsername());

        assertEquals(200, deleteResponse.getStatusCode());
        assertEquals(404, secondDeleteResponse.getStatusCode());
    }

    @Test
    public void concurrentDeletionAndUpdate() throws Exception {
        UserApi.postUserJSON(payload);

        User newPayload = generator.nextObject(User.class);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            CountDownLatch startLatch = new CountDownLatch(1);

            Future<Response> deleteFuture = executor.submit(() -> {
                startLatch.await();
                return UserApi.deleteXMLUser(payload.getUsername());
            });

            Future<Response> updateFuture = executor.submit(() -> {
                startLatch.await();
                return UserApi.putUserJSON(payload.getUsername(), newPayload);
            });

            startLatch.countDown();

            Response deleteResponse = deleteFuture.get(10, TimeUnit.SECONDS);
            Response updateResponse = updateFuture.get(10, TimeUnit.SECONDS);

            assertNotNull(deleteResponse, "deleteResponse не должен быть null");
            assertNotNull(updateResponse, "updateResponse не должен быть null");

            int deleteStatus = deleteResponse.getStatusCode();
            int updateStatus = updateResponse.getStatusCode();

            if (deleteStatus == 200) {
                assertEquals(404, updateStatus,
                        "После успешного удаления обновление должно вернуть 404");
            } else if (updateStatus == 200) {
                assertEquals(404, deleteStatus,
                        "После успешного обновления удаление должно вернуть 404");
            } else {
                fail("Неожиданные статусы. Удаление: " + deleteStatus +
                        ", обновление: " + updateStatus);
            }
        } finally {
            executor.shutdown();
            boolean terminated = executor.awaitTermination(5, TimeUnit.SECONDS);
            assertTrue(terminated, "Рабочие потоки не завершились за 5 секунд");
        }
    }
}
