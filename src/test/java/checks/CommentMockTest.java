package checks;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.example.factories.EasyRandomFactory.commentGenerator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import mocks.CommentMock;
import org.example.payloads.request.CommentRequestPayload;
import org.example.payloads.response.CommentResponsePayload;
import org.example.requests.CommentApi;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

/**
 * WireMock-backed tests for {@link CommentApi}.
 *
 * <p>Unlike {@link CommentTest}, these tests never touch the real Comments
 * service: a WireMock server is started on a dynamic port and {@link CommentApi}
 * is redirected to it via the {@code api.baseUrl} system property. This lets
 * error and resilience scenarios (404, connection reset) be exercised
 * deterministically and offline.
 */
@Tag("mock")
class CommentMockTest {

    private static final String CONTEXT_PATH = "/basic_authentication/api/";

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .configureStaticDsl(true)
            .build();

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    static void pointApiAtMock() {
        System.setProperty("api.baseUrl", wireMock.baseUrl() + CONTEXT_PATH);
    }

    @AfterAll
    static void restoreDefaultBaseUrl() {
        System.clearProperty("api.baseUrl");
    }

    @BeforeEach
    void resetStubs() {
        wireMock.resetAll();
    }

    @Test
    @Tag("smoke")
    @DisplayName("Create comment returns the created comment body")
    void createCommentReturnsCreatedBody() throws Exception {
        CommentMock.stubCreateCommentSuccess();
        CommentRequestPayload payload =
                commentGenerator().nextObject(CommentRequestPayload.class);

        var response = CommentApi.createComment(payload);

        assertEquals(200, response.getStatusCode());
        CommentResponsePayload created =
                mapper.readValue(response.getBody().asString(), CommentResponsePayload.class);
        assertEquals(CommentMock.STUB_COMMENT_ID, created.getCommentId());
        assertEquals(CommentMock.STUB_COMMENT_TEXT, created.getComment());
    }

    @Test
    @Tag("smoke")
    @DisplayName("Get all comments returns the stubbed list")
    void getAllCommentsReturnsList() throws Exception {
        CommentMock.stubGetAllCommentsSuccess();

        var response = CommentApi.getAllComments();

        assertEquals(200, response.getStatusCode());
        List<CommentResponsePayload> comments =
                mapper.readValue(response.getBody().asString(), new TypeReference<>() {});
        assertEquals(2, comments.size());
    }

    @Test
    @Tag("negative")
    @DisplayName("Get a non-existent comment returns 404")
    void getMissingCommentReturnsNotFound() {
        CommentMock.stubGetCommentNotFound();

        var response = CommentApi.getComment("999999");

        assertEquals(404, response.getStatusCode());
        assertFalse(response.getBody().asString().isBlank());
    }

    @Test
    @Tag("regression")
    @DisplayName("A connection reset surfaces as an error rather than a response")
    void connectionResetIsRaised() {
        CommentMock.stubGetAllCommentsConnectionReset();

        assertThrows(Exception.class, CommentApi::getAllComments);
    }
}
