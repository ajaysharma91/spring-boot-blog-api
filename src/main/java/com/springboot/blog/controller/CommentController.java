package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto,
                                                    @PathVariable(value = "postId") long postId) {
        CommentDto commentDto1 = commentService.createComment(postId, commentDto);
        return new ResponseEntity<>(commentDto1, HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getAllComments(@PathVariable(value = "postId") long postId) {
        List<CommentDto> commentDtos = commentService.getAllComments(postId);
        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> getByCommentId(@PathVariable(value = "postId") long postId,
                                                     @PathVariable(value = "id") long id) {
        CommentDto commentDtos = commentService.getCommentById(postId, id);
        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") long postId,
                                                @PathVariable(value = "id") long id) {
        try {
            commentService.deleteComment(postId, id);
        } catch (Exception e) {
            return new ResponseEntity<>("Not Deleted due to " + e, HttpStatus.OK);
        }
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(@RequestBody CommentDto commentDto,
                                                    @PathVariable(value = "postId") long postId,
                                                    @PathVariable(value = "id") long id) {
        CommentDto commentDto1 = commentService.updateComment(postId, id, commentDto);
        return new ResponseEntity<>(commentDto1, HttpStatus.OK);
    }
}
