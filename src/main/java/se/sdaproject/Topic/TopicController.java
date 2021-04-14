package se.sdaproject.Topic;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.sdaproject.Article.Article;
import se.sdaproject.Article.ArticleRepository;
import se.sdaproject.ResourceNotFound;

import java.util.List;

@RestController
public class TopicController {

    TopicRepository topicRepository;
    ArticleRepository articleRepository;

    public TopicController(TopicRepository topicRepository, ArticleRepository articleRepository) {
        this.topicRepository = topicRepository;
        this.articleRepository = articleRepository;
    }

    // Create topics
    @PostMapping("/topics")
    public ResponseEntity<Topic> createTopic(@RequestBody Topic topic) {
        topicRepository.save(topic);
        return ResponseEntity.status(HttpStatus.CREATED).body(topic);
    }

    // Associate existing topics with article with a specific id
    @PostMapping("/articles/{articleId}/topics")
    public ResponseEntity<Article> associateTopicToArticle(@PathVariable Long articleId,
                                                           @RequestBody Topic topic) {
        Article article = articleRepository.findById(articleId).orElseThrow(ResourceNotFound::new);

        // Check existence of a named topic in an article
        List<Topic> topics = article.getTopics();
        for (Topic eachTopic : topics) {
            if (eachTopic.getName().equals(topic.getName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(article);
            }
        }

        // Add a topic to an article if it does not already exist in its repository
        topic = topicRepository.findByName(topic.getName());
        if (topic == null) {
            topicRepository.save(topic);
            article.getTopics().add(topic);
        } else {
            article.getTopics().add(topic);
        }

        articleRepository.save(article);

        return ResponseEntity.status(HttpStatus.CREATED).body(article);
    }

    // Return all topics
    @GetMapping("/topics")
    public ResponseEntity<List<Topic>> listAllTopics() {
        List<Topic> topic = topicRepository.findAll();
        return ResponseEntity.ok(topic);
    }

    // Return topics in article with a specific id
    @GetMapping("/articles/{articleId}/topics")
    public ResponseEntity<List<Topic>> listAllTopicsUnderArticle(@PathVariable Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(ResourceNotFound::new);
        return ResponseEntity.ok(article.getTopics());
    }

    // Return articles associated with topic of a specific id
    @GetMapping("/topics/{topicId}/articles")
    public ResponseEntity<List<Article>> listAllArticlesUnderTopic(@PathVariable Long topicId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(ResourceNotFound::new);
        return ResponseEntity.ok(topic.getArticles());
    }

    // Update a topic of specific id.
    @PutMapping("/topics/{id}")
    public ResponseEntity<Topic> updateArticleTopic(@PathVariable Long id,
                                                           @RequestBody Topic updatedTopic) {
        topicRepository.findById(id).orElseThrow(ResourceNotFound::new);
        updatedTopic.setId(id);
        topicRepository.save(updatedTopic);
        return ResponseEntity.ok(updatedTopic);
    }

    // Delete topic of a specific id.

    @DeleteMapping("/topics/{topicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTopic(@PathVariable Long topicId) {
        Topic articleTopic = topicRepository.findById(topicId)
                .orElseThrow(ResourceNotFound::new);
        topicRepository.delete(articleTopic);
    }

    // Delete the association of a topic for the given article. The topic & article
    // themselves remain.

    @DeleteMapping("/articles/{articleId}/topics/{topicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void dissociateTopicFromArticle(@PathVariable Long articleId, @PathVariable Long topicId) {

        Article article = articleRepository.findById(articleId).orElseThrow(ResourceNotFound::new);
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(ResourceNotFound::new);

        if (article.getTopics().contains(topic)) {
            article.getTopics().remove(topic);
            articleRepository.save(article);
        } else {
            throw new ResourceNotFound();
        }
    }

}
