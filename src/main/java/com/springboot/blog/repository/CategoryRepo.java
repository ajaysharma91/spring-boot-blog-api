package com.springboot.blog.repository;

import com.springboot.blog.entity.CategoryE;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<CategoryE, Long> {
}
