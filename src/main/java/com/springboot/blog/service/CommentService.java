package com.springboot.blog.service;

import com.springboot.blog.payload.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(long postId, CommentDto commentDto);
    List<CommentDto> getAllComments(long postId);
    CommentDto getCommentById(long postId, long id);
    void deleteComment(long postId, long id);
    CommentDto updateComment(long postId, long id, CommentDto commentDto);
}
