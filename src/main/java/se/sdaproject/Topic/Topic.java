package se.sdaproject.Topic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import se.sdaproject.Article.Article;

import javax.persistence.*;
import java.util.List;

@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "topics")
    @JsonIgnore
    /*@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
    @JsonIdentityReference(alwaysAsId = true)*/
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Article> articles;

    public Topic() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

}
