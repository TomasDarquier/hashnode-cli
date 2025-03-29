package hashnode.cli.clients;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;

import java.io.IOException;

public class GraphQLMutationClient{

    private static final String HASHNODE_API = "https://gql.hashnode.com";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    private static <T> T executeMutation(String query, JsonObject variables, Class<T> type, String authToken) throws IOException {
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
            return gson.fromJson(response.body().string(), type);
        }
    }
}