package com.springboot.blog.service;

import com.springboot.blog.entity.Post;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);
    PostResponse getAll(int pageNo, int pageSize, String sortBy, String sortDir);
    PostDto getById(long id);
    void deletePost(long id);
    PostDto updatePost(PostDto postDto, long id);
    List<PostDto> getPostsByCategoryId(Long id);

}
