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


public class GraphQLMutationsCreateSeriesTest {

    private String apiToken;
    private String publicationId;

    @Before
    public void setup() {
        apiToken = "fakeToken";
        publicationId = "1n1n1n1n1n1n1n";
    }

    @Test
    public void createNewSeries_returnsCreatedSeries() throws IOException {
        Series expectedSeries = createMockSeries("New Series", "new-series", "1s1s1s1s1s1");

        Root root = new Root();
        root.setData(new Data());
        root.getData().setCreateSeries(new CreateSeries());
        root.getData().getCreateSeries().setSeries(expectedSeries);

        try (MockedStatic<GraphQLClient> mocked = Mockito.mockStatic(GraphQLClient.class)) {
            mocked.when(() ->
                    GraphQLClient.executeAPICall(anyString(), any(JsonObject.class), eq(apiToken))
            ).thenReturn(root);

            Series result = GraphQLMutations.createNewSeries(
                    expectedSeries.getName(),
                    expectedSeries.getSlug(),
                    publicationId,
                    apiToken
            );

            assertEquals(expectedSeries, result);
            mocked.verify(() ->
                    GraphQLClient.executeAPICall(anyString(), any(JsonObject.class), eq(apiToken)), times(1));
        }
    }

    private Series createMockSeries(String name, String slug, String id) {
        Series series = new Series();
        series.setName(name);
        series.setSlug(slug);
        series.setId(id);
        series.setCreatedAt(new Date());
        return series;
    }
}
