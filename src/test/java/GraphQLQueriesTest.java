import com.google.gson.JsonObject;
import hashnode.cli.clients.GraphQLClient;
import hashnode.cli.clients.GraphQLQueries;
import hashnode.cli.models.*;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

public class GraphQLQueriesTest {

    @Test
    public void getPublicationsTest() throws IOException {
        Node node1 = new Node();
        node1.setId("1");
        node1.setUrl("https://example.com");

        Edge edge1 = new Edge();
        edge1.setNode(node1);

        ArrayList<Edge> edges = new ArrayList<>();
        edges.add(edge1);

        Publications publications = new Publications();
        publications.setEdges(edges);

        Me me = new Me();
        me.setPublications(publications);

        Data data = new Data();
        data.setMe(me);

        Root root = new Root();
        root.setData(data);

        String authToken = "testAuthToken";

        try (MockedStatic<GraphQLClient> mocked = Mockito.mockStatic(GraphQLClient.class)) {

            mocked.when(() ->
                    GraphQLClient.executeAPICall(anyString(), any(JsonObject.class), eq(authToken))
            ).thenReturn(root);

            Publications result = GraphQLQueries.getPublications(authToken);

            assertEquals(publications, result);

            mocked.verify(() ->
                    GraphQLClient.executeAPICall(anyString(), any(JsonObject.class), eq(authToken)), times(1));
        }
    }


    @Test
    public void testGetSeriesTest() throws IOException {
        Node node1 = new Node();
        node1.setName("Serie A");
        node1.setId("123");

        Node node2 = new Node();
        node2.setName("Serie B");
        node2.setId("456");

        Edge edge1 = new Edge();
        edge1.setNode(node1);
        Edge edge2 = new Edge();
        edge2.setNode(node2);

        List<Edge> edgeList = new ArrayList<>();
        edgeList.add(edge1);
        edgeList.add(edge2);

        SeriesList seriesList = new SeriesList();
        seriesList.setEdges((ArrayList<Edge>) edgeList);

        Publication publication = new Publication();
        publication.setSeriesList(seriesList);

        Data data = new Data();
        data.setPublication(publication);

        Root root = new Root();
        root.setData(data);

        String authToken = "testAuthToken";
        String host = "example.hashnode.dev";

        try (MockedStatic<GraphQLClient> mocked = Mockito.mockStatic(GraphQLClient.class)) {
            mocked.when(() ->
                    GraphQLClient.executeAPICall(anyString(), any(JsonObject.class), eq(authToken))
            ).thenReturn(root);

            Map<String, String> result = GraphQLQueries.getSeries(authToken, host);

            Map<String, String> expected = new HashMap<>();
            expected.put("Serie A", "123");
            expected.put("Serie B", "456");

            assertEquals(expected, result);

            mocked.verify(() ->
                    GraphQLClient.executeAPICall(anyString(), any(JsonObject.class), eq(authToken)), times(1));
        }
    }

}

