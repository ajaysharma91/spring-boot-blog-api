package com.springboot.blog.payload;

import com.springboot.blog.entity.User;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostDto {
    private long id;
    @NotNull
    @NotEmpty
    @Size(min = 2, message = "provide at least 2 charactor")
    private String title;
    private String description;
    private String content;
    private Long categoryId;
    private Date postDate;
    private UserDetailsDto user;
    private CategoryDto category;
}
