package hashnode.cli.clients;

import com.google.gson.JsonObject;
import hashnode.cli.models.Root;
import hashnode.cli.models.Series;

import java.io.IOException;

public class GraphQLMutations {

    public static Series createNewSeries(String name, String slug, String publicationId, String apiToken) throws IOException {
        String query = """
                mutation CreateSeries($input: CreateSeriesInput!) {
                  createSeries(input: $input) {
                    series {
                      id
                      name
                      createdAt
                      slug
                    }
                  }
                }
                """;
        JsonObject variables = new JsonObject();
        JsonObject input = new JsonObject();
        input.addProperty("name", name);
        input.addProperty("slug", slug);
        input.addProperty("publicationId", publicationId);
        variables.add("input", input);

        Root root = GraphQLClient.executeAPICall(query,variables,apiToken);
        return root.data.createSeries.series;
    }
}