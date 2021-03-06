package se.sdaproject.Article;

import se.sdaproject.Comment.Comment;
import se.sdaproject.Topic.Topic;

import javax.persistence.*;
import java.util.List;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String body;
    private String authorName;

    @OneToMany(mappedBy = "owner")
    private List<Comment> comments;

    @ManyToMany
    private List<Topic> topics;

    public Article() {

    }
    public Article(Long id, String title, String body, String authorName) {
        this.title = title;
        this.body = body;
        this.authorName = authorName;
    }

    public Long getId() { return id; }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Topic> getTopics() {
        return this.topics;
    }

    public void setArticleTopics(List<Topic> topics) {
        this.topics = topics;
    }
}
