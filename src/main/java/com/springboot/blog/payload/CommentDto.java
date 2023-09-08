package com.springboot.blog.payload;

import com.springboot.blog.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private long id;
    private String name;
    private String email;
    private String body;
    private long parent_id;
    private List<CommentDto> reply;
}
