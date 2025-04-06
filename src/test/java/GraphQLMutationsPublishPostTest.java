import com.google.gson.JsonObject;
import hashnode.cli.clients.GraphQLClient;
import hashnode.cli.clients.GraphQLMutations;
import hashnode.cli.models.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

public class GraphQLMutationsPublishPostTest {

    private String apiToken;
    private String publicationId;
    private Series mockSeries;

    @Before
    public void setup() {
        apiToken = "testToken";
        publicationId = "1n1n1n1n1n1n1n";
        mockSeries = new Series();
        mockSeries.setName("series name");
    }

    @Test
    public void publishPost_returnsExpectedPublishPost() throws IOException {
        Post expectedPost = createMockPost("Title", "Subtitle", "https://example.com/test/title", mockSeries);

        Root root = new Root();
        root.setData(new Data());
        PublishPost publishPost = new PublishPost();
        publishPost.setPost(expectedPost);
        root.getData().setPublishPost(publishPost);

        try (MockedStatic<GraphQLClient> mocked = Mockito.mockStatic(GraphQLClient.class)) {
            mocked.when(() ->
                    GraphQLClient.executeAPICall(anyString(), any(JsonObject.class), eq(apiToken))
            ).thenReturn(root);

            PublishPost result = GraphQLMutations.createNewPost(
                    apiToken,
                    publicationId,
                    expectedPost.getTitle(),
                    "content",
                    true,
                    expectedPost.getSubtitle(),
                    false,
                    null,
                    true,
                    "1s1s1s1s1s1"
            );

            assertEquals(publishPost, result);
            mocked.verify(() ->
                    GraphQLClient.executeAPICall(anyString(), any(JsonObject.class), eq(apiToken)), times(1));
        }
    }

    private Post createMockPost(String title, String subtitle, String url, Series series) {
        Post post = new Post();
        post.setTitle(title);
        post.setSubtitle(subtitle);
        post.setUrl(url);
        post.setSeries(series);
        post.setReadTimeInMinutes(10);
        post.setBrief("brief");
        post.setPublishedAt(new Date());
        return post;
    }
}
