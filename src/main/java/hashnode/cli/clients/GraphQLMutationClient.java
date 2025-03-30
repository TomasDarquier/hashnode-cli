package hashnode.cli.clients;

import com.google.gson.JsonObject;

import java.io.IOException;

public class GraphQLMutationClient{

    public static String getPublicationId(String host, String authToken) throws IOException {
        String query = """
                query Publication(
                  $host: String
                ) {
                  publication(
                    host: $host
                  ) {
                    url
                    id
                  }
                }
                """;
        JsonObject variables = new JsonObject();
        variables.addProperty("host", host);

        //TODO
        //learn how to manage the response
        GraphQLClient.executeMutation(query, variables, authToken);
        return null;
    }
}