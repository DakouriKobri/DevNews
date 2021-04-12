package se.sdaproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/articles")
@RestController
public class ArticleController {

        ArticleRepository articleRepository;

        @Autowired
        public ArticleController(ArticleRepository articleRepository){
            this.articleRepository = articleRepository;
        }

        @GetMapping
        public List<Article> listAllArticles(){
            return articleRepository.findAll();
        }

        @GetMapping("/{id}")
        public ResponseEntity<Article> getArticle(@PathVariable Long id){
            Article article = articleRepository
                    .findById(id)
                    .orElseThrow(ResourceNotFound::new);
            return ResponseEntity.ok(article);
        }

        @PostMapping
        public ResponseEntity<Article> createArticle(@RequestBody Article article){
            articleRepository.save(article);
            return ResponseEntity.status(HttpStatus.CREATED).body(article);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article updatedArticle) {
            articleRepository.findById(id).orElseThrow(ResourceNotFound::new);
            updatedArticle.setId(id);
            Article article = articleRepository.save(updatedArticle);
            return ResponseEntity.ok(article);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Article> deleteArticle(@PathVariable Long id) {
            Article article = articleRepository.findById(id).orElseThrow(ResourceNotFound::new);
            articleRepository.delete(article);
            return ResponseEntity.ok(article);
        }



}
