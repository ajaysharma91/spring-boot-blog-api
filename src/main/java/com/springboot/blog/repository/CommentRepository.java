package com.springboot.blog.repository;

import com.springboot.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPostId(long id);
    List<Comment> findByParentId(long id);
}
