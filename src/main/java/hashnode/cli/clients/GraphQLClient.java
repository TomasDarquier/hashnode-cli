package hashnode.cli.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import hashnode.cli.models.Root;
import okhttp3.*;

import java.io.IOException;

public class GraphQLClient {

    private static final String HASHNODE_API = "https://gql.hashnode.com";
    private static final OkHttpClient client = new OkHttpClient();

   public static Root executeAPICall(
            String query,
            JsonObject variables,
            String authToken
    ) throws IOException {

        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("query", query);
        requestJson.add("variables", variables);

        RequestBody requestBody = RequestBody.create(requestJson.toString(), MediaType.parse("application/json"));
       Request request = new Request.Builder()
               .url(HASHNODE_API)
               .post(requestBody)
               .header("Cache-Control", "no-cache, no-store, must-revalidate")
               .header("Pragma", "no-cache")
               .header("Expires", "0")
               .addHeader("Authorization", authToken)
               .addHeader("Content-Type", "application/json")
               .build();

        try(Response response = client.newCall(request).execute()){
            if(!response.isSuccessful()){
                throw new IOException("Error in the communication with the Hashnode API: \n" + response);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body().string(), Root.class);
        }
    }
}
