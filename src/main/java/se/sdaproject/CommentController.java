package se.sdaproject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CommentController {

    CommentRepository commentRepository;
    ArticleRepository articleRepository;

    public CommentController (CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    @PostMapping("/articles/{articleId}/comments")
    public ResponseEntity<Comment> createComment(@PathVariable Long articleId, @RequestBody Comment comment){
        Article article = articleRepository.findById(articleId).orElseThrow(ResourceNotFound::new);
        comment.setOwner(article);
        commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @GetMapping("/articles/{articleId}/comments")
    public ResponseEntity<List<Comment>> listAllArticlesComments(@PathVariable Long articleId){
        Article article = articleRepository.findById(articleId).orElseThrow(ResourceNotFound::new);
        return ResponseEntity.ok(article.getComments());
    }
    
    @GetMapping(value = "/comments", params={"authorName"})
    public ResponseEntity<List<Comment>> listAllAuthorsComments(@RequestParam String authorName){
        List<Comment> comments = commentRepository.findByAuthorName(authorName);
        if (comments.isEmpty()){
            throw new ResourceNotFound();
        }
        return ResponseEntity.ok(commentRepository.findByAuthorName(authorName));
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @Valid @RequestBody Comment updatedComment) {
        Comment comment = commentRepository.findById(id).orElseThrow(ResourceNotFound::new);
        updatedComment.setId(id);
        commentRepository.save(updatedComment);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(ResourceNotFound::new);
        commentRepository.delete(comment);
        return ResponseEntity.ok(comment);
    }

}
