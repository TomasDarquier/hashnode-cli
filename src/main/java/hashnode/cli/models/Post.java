package hashnode.cli.models;

import lombok.Data;

import java.util.Date;

@Data
public class Post {
    public String title;
    public String subtitle;
    public String url;
    public int readTimeInMinutes;
    public Series series;
    public String brief;
    public Date publishedAt;
}
