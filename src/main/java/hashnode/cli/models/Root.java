package hashnode.cli.models;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Root {
    @JsonProperty("data")
    public Data data;
}