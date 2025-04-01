package hashnode.cli.clients;

import com.google.gson.JsonObject;
import hashnode.cli.models.Publications;
import hashnode.cli.models.Root;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GraphQLQueries {

    public static Publications getPublications(String authToken) throws IOException {
        String query = """
                query Me ($first:Int!){
                  me {
                    id
                    username
                    name
                    publications(first:$first){
                      edges{
                        node{
                          id
                          url
                        }
                      }
                    }
                  }
                }
                """;
        JsonObject variables = new JsonObject();
        variables.addProperty("first", 20);

        Root root = GraphQLClient.executeAPICall(query, variables, authToken);

        return root.data.me.publications;
    }


    public static ArrayList<String> getSeries(String authToken, String host) throws IOException {
        String query = """
                query Publication(
                  $host: String,
                  $first: Int!
                ) {
                  publication(
                    host: $host
                  ) {
                    seriesList(first: $first){
                      edges{
                        node{
                          name
                        }
                      }
                    }
                  }
                }
                """;

        JsonObject variables = new JsonObject();
        variables.addProperty("first", 20);
        variables.addProperty("host", host);

        Root root = GraphQLClient.executeAPICall(query, variables, authToken);

        return root.data.publication.seriesList.edges
                .stream()
                .map(edge -> edge.node.name)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}