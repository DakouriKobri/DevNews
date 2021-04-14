package se.sdaproject.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.sdaproject.ResourceNotFound;

import java.util.List;

@RequestMapping("/articles")
@RestController
public class ArticleController {

        ArticleRepository articleRepository;

        @Autowired
        public ArticleController(ArticleRepository articleRepository){
            this.articleRepository = articleRepository;
        }

        // Return all articles
        @GetMapping
        public List<Article> listAllArticles(){
            return articleRepository.findAll();
        }

        // Return articles with a specific id
        @GetMapping("/{id}")
        public ResponseEntity<Article> getArticle(@PathVariable Long id){
            Article article = articleRepository
                    .findById(id)
                    .orElseThrow(ResourceNotFound::new);
            return ResponseEntity.ok(article);
        }

        // Create a new Article
        @PostMapping
        public ResponseEntity<Article> createArticle(@RequestBody Article article){
            articleRepository.save(article);
            return ResponseEntity.status(HttpStatus.CREATED).body(article);
        }

        // Update article with specific id
        @PutMapping("/{id}")
        public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article updatedArticle) {
            articleRepository.findById(id).orElseThrow(ResourceNotFound::new);
            updatedArticle.setId(id);
            Article article = articleRepository.save(updatedArticle);
            return ResponseEntity.ok(article);
        }

        // Delete  article with specific id
        @DeleteMapping("/{id}")
        public ResponseEntity<Article> deleteArticle(@PathVariable Long id) {
            Article article = articleRepository.findById(id).orElseThrow(ResourceNotFound::new);
            articleRepository.delete(article);
            return ResponseEntity.ok(article);
        }



}
