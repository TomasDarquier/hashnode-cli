package hashnode.cli.clients;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.io.IOException;

public class GraphQLClient {

    private static final String HASHNODE_API = "https://gql.hashnode.com";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

   public static JsonObject executeMutation(
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
                .addHeader("Authorization", authToken)
                .addHeader("Content-Type", "application/json")
                .build();

        try(Response response = client.newCall(request).execute()){
            if(!response.isSuccessful()){
                throw new IOException("Error in the communication with the Hashnode API: \n" + response);
            }
            return JsonParser.parseString(response.body().string()).getAsJsonObject();
        }
    }
}