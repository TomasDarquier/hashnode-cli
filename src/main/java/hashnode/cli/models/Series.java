package hashnode.cli.models;

import lombok.Data;

import java.util.Date;

@Data
public class Series {
    public String id;
    public String name;
    public Date createdAt;
    public String slug;
}
