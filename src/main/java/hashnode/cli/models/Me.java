package hashnode.cli.models;

import lombok.Data;

@Data
public class Me {
    public String id;
    public String username;
    public String name;
    public Publications publications;
}
