package com.springboot.blog.service.impl;

import com.springboot.blog.entity.CategoryE;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.repository.CategoryRepo;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImp implements CategoryService {
   private CategoryRepo categoryRepo;
   private PostRepository postRepository;

    public CategoryServiceImp(CategoryRepo categoryRepo, PostRepository postRepository) {
        this.categoryRepo = categoryRepo;
        this.postRepository = postRepository;
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        CategoryE category = mapToEntity(categoryDto);
        CategoryE categoryE = categoryRepo.save(category);
        return mapToDto(categoryE);
    }

    @Override
    public void delete(long id) {
        CategoryE category = categoryRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Category","Id",String.valueOf(id)));
        categoryRepo.delete(category);
    }

    @Override
    public CategoryDto findByIdCategory(long id) {
        CategoryE category = categoryRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Category","Id",String.valueOf(id)));
        List<Post> posts = postRepository.findByCategoryId(category.getId());
        System.out.println("Posts: "+posts);
//        category.setPosts(posts);
        CategoryDto categoryDto = mapToDto(category);
        categoryDto.setPosts(posts);
        return categoryDto;
    }

    @Override
    public List<CategoryDto> getAll() {
        List<CategoryE> categories = categoryRepo.findAll();
        List<CategoryDto> categoryDtos = categories.stream().map(cat->mapToDto(cat)).collect(Collectors.toList());
        return categoryDtos;
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long id) {
        CategoryE category = categoryRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Category","Id",String.valueOf(id)));
        category.setDescription(categoryDto.getDescription());
        category.setName(categoryDto.getName());
        CategoryE categoryE = categoryRepo.save(category);
        return mapToDto(categoryE);
    }

    private CategoryDto mapToDto(CategoryE categoryE){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(categoryE.getId());
        categoryDto.setName(categoryE.getName());
        categoryDto.setDescription(categoryE.getDescription());
//        categoryDto.setPosts((List<Post>) categoryE.getPosts());
        return categoryDto;
    }
    private CategoryE mapToEntity(CategoryDto categoryDto){
        CategoryE categoryE = new CategoryE();
        categoryE.setName(categoryDto.getName());
        categoryE.setDescription(categoryDto.getDescription());
        return categoryE;
    }
}
