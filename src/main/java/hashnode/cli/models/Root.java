package hashnode.cli.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Root {
    @JsonProperty("data")
    public Data data;
}