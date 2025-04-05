package hashnode.cli.clients;

import com.google.gson.JsonObject;
import hashnode.cli.models.PublishPost;
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
        return root.getData().getCreateSeries().getSeries();
    }

    public static PublishPost createNewPost(
            String apiToken,
            String publicationId,
            String title,
            String content,
            boolean subtitleExists,
            String subtitle,
            boolean bannerExists,
            String bannerImage,
            boolean seriesExists,
            String seriesId) throws IOException {
        String query = """
                mutation PublishPost($input: PublishPostInput!) {
                  publishPost(input: $input) {
                    post {
                      title
                      subtitle
                      url
                      readTimeInMinutes
                      series{
                        name
                      }
                      brief
                      publishedAt
                    }
                  }
                }
                """;

        JsonObject variables = new JsonObject();
        JsonObject input = new JsonObject();
        input.addProperty("publicationId", publicationId);
        input.addProperty("title", title);
        if(subtitleExists) input.addProperty("subtitle", subtitle);
        input.addProperty("contentMarkdown", content);
        if(seriesExists) input.addProperty("seriesId", seriesId);
        if(bannerExists){
            JsonObject coverImageOptions = new JsonObject();
            coverImageOptions.addProperty("coverImageURL", bannerImage);
            input.add("coverImageOptions", coverImageOptions);
        }

        variables.add("input", input);

        Root root = GraphQLClient.executeAPICall(query,variables,apiToken);
        return root.getData().getPublishPost();
    }
}