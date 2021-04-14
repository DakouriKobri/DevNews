package se.sdaproject.Comment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.sdaproject.Article.Article;
import se.sdaproject.Article.ArticleRepository;
import se.sdaproject.ResourceNotFound;

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

    // Create comment with a specific id
    @PostMapping("/articles/{articleId}/comments")
    public ResponseEntity<Comment> createComment(@PathVariable Long articleId, @RequestBody Comment comment){
        Article article = articleRepository.findById(articleId).orElseThrow(ResourceNotFound::new);
        comment.setOwner(article);
        commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    // Return comments on article with a specific id
    @GetMapping("/articles/{articleId}/comments")
    public ResponseEntity<List<Comment>> listAllArticlesComments(@PathVariable Long articleId){
        Article article = articleRepository.findById(articleId).orElseThrow(ResourceNotFound::new);
        return ResponseEntity.ok(article.getComments());
    }

    // Return comments on article by a specific author name
    @GetMapping(value = "/comments", params={"authorName"})
    public ResponseEntity<List<Comment>> listAllAuthorsComments(@RequestParam String authorName){
        List<Comment> comments = commentRepository.findByAuthorName(authorName);
        if (comments.isEmpty()){
            throw new ResourceNotFound();
        }
        return ResponseEntity.ok(commentRepository.findByAuthorName(authorName));
    }

    // Update comments on article with a specific id
    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @Valid @RequestBody Comment updatedComment) {
        Comment comment = commentRepository.findById(id).orElseThrow(ResourceNotFound::new);
        updatedComment.setId(id);
        commentRepository.save(updatedComment);
        return ResponseEntity.ok(updatedComment);
    }

    // Delete comments on article with a specific id
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(ResourceNotFound::new);
        commentRepository.delete(comment);
        return ResponseEntity.ok(comment);
    }

}
