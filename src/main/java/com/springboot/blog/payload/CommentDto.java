package com.springboot.blog.payload;

import com.springboot.blog.entity.Comment;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class CommentDto {
    private long id;
    private String name;
    private String email;
    private String body;
    private long parent_id;
    private CommentDto parent;
    private List<CommentDto> reply;
}
