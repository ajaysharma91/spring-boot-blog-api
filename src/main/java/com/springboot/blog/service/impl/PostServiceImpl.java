package com.springboot.blog.service.impl;

import com.springboot.blog.entity.CategoryE;
import com.springboot.blog.entity.Post;
import com.springboot.blog.entity.User;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.payload.UserDetailsDto;
import com.springboot.blog.repository.CategoryRepo;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private CategoryRepo categoryRepo;
    private UserRepository userRepository;
    @Override
    public PostDto createPost(PostDto postDto) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("User Details-"+user.getUsername());
        User user1 = userRepository.findByUsernameOrEmail(user.getUsername(),user.getUsername()).orElseThrow(()->new ResourceNotFoundException("User","NameorEmai",user.getUsername()));
        System.out.println("User1 Details-"+user1);

        CategoryE category = categoryRepo.findById(postDto.getCategoryId()).
                orElseThrow(()->new ResourceNotFoundException("Category","Id",String.valueOf(postDto.getCategoryId())));
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        post.setCategory(category);
        post.setUser(user1);
        post.setPostDate(new Date());
        System.out.println("Post "+post);
        Post newPost = postRepository.save(post);
        System.out.println("PostR "+newPost);
        PostDto postDto1 =new PostDto();
        postDto1.setTitle(newPost.getTitle());
        postDto1.setDescription(newPost.getDescription());
        postDto1.setContent(newPost.getContent());
        postDto1.setId(newPost.getId());
        postDto1.setCategoryId(newPost.getCategory().getId());
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setEmail(newPost.getUser().getEmail());
        userDetailsDto.setUsername(newPost.getUser().getUsername());
        postDto1.setUser(userDetailsDto);
        postDto1.setPostDate(newPost.getPostDate());
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(newPost.getCategory().getName());
        categoryDto.setDescription(newPost.getCategory().getDescription());
        categoryDto.setId(newPost.getCategory().getId());
        postDto1.setCategory(categoryDto);
        return postDto1;
    }
    @Override
    public PostResponse getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
        Page<Post> posts = postRepository.findAll(pageable);

        List<Post> listOfPosts = posts.getContent();
        List<PostDto> content =  listOfPosts.stream().map(post->mapToDto(post)).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());
        postResponse.setTotalElements(posts.getTotalElements());
        return postResponse;
    }

    @Override
    public PostDto getById(long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","Id",String.valueOf(id)));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        CategoryE category = categoryRepo.findById(postDto.getCategoryId()).
                orElseThrow(()->new ResourceNotFoundException("Category","Id",String.valueOf(postDto.getCategoryId())));
        Post newPost = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","Id",String.valueOf(id)));
        newPost.setTitle(postDto.getTitle());
        newPost.setDescription(postDto.getDescription());
        newPost.setContent(postDto.getContent());
        newPost.setCategory(category);
        Post postUp = postRepository.save(newPost);
        return mapToDto(postUp);
    }

    @Override
    public List<PostDto> getPostsByCategoryId(Long id) {
        CategoryE category = categoryRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Category","Id",String.valueOf(id)));
        List<Post> posts = postRepository.findByCategoryId(id);
        return posts.stream().map(post->mapToDto(post)).collect(Collectors.toList());
    }

    @Override
    public void deletePost(long id) {
        Post newPost = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","Id",String.valueOf(id)));
        postRepository.delete(newPost);
    }

    private PostDto mapToDto(Post post){
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setContent(post.getContent());
        dto.setCategoryId(post.getCategory().getId());
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setEmail(post.getUser().getEmail());
        userDetailsDto.setUsername(post.getUser().getUsername());
        dto.setUser(userDetailsDto);
        dto.setPostDate(post.getPostDate());
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(post.getCategory().getName());
        categoryDto.setDescription(post.getCategory().getDescription());
        categoryDto.setId(post.getCategory().getId());
        dto.setCategory(categoryDto);
        return dto;
    }

    public PostServiceImpl(PostRepository postRepository,
                           CategoryRepo categoryRepo,
                           UserRepository userRepository) {
        this.postRepository = postRepository;
        this.categoryRepo = categoryRepo;
        this.userRepository = userRepository;
    }
}
