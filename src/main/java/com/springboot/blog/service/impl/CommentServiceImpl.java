package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
    Comment comment = mapToEntity(commentDto);
    Post post  = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","Id",String.valueOf(postId)));
                 comment.setPost(post);
    Comment newComment = commentRepository.save(comment);
    return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getAllComments(long postId) {
        List<CommentDto> comments = commentRepository.findByPostId(postId).stream().map((comment1)->mapToDto(comment1)).collect(Collectors.toList());
        return comments;
    }

    @Override
    public CommentDto getCommentById(long postId, long id) {
        Post post  = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","Id",String.valueOf(postId)));

        Comment comment = commentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Comment","Id",String.valueOf(id)));
        if (!Objects.equals(comment.getPost().getId(), post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comments does not belongs to post");
        }
            return mapToDto(comment);
    }

    @Override
    public void deleteComment(long postId, long id) {
        Post post  = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","Id",String.valueOf(postId)));

        Comment comment = commentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Comment","Id",String.valueOf(id)));
        if (!Objects.equals(comment.getPost().getId(), post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comments does not belongs to post");
        }
        commentRepository.delete(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long id, CommentDto commentDto) {
        Post post  = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","Id",String.valueOf(postId)));

        Comment comment = commentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Comment","Id",String.valueOf(id)));
        if (!Objects.equals(comment.getPost().getId(), post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comments does not belongs to post");
        }
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        Comment updateComment = commentRepository.save(comment);
        return mapToDto(updateComment);
    }

    private CommentDto mapToDto(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());
        return commentDto;
    }
    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());
        return comment;
    }
}
