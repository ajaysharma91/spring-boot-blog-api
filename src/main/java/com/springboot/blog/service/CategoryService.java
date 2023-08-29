package com.springboot.blog.service;

import com.springboot.blog.payload.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(CategoryDto categoryDto);
    void delete(long id);
    CategoryDto findByIdCategory(long id);
    List<CategoryDto> getAll();
    CategoryDto updateCategory(CategoryDto categoryDto, Long id);
}
