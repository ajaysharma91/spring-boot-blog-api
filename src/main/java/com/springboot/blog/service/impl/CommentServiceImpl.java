package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", String.valueOf(postId)));
        comment.setPost(post);
        if (commentDto.getParent_id() != 0) {
            Comment pcomment = commentRepository.findById(commentDto.getParent_id()).orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", String.valueOf(commentDto.getParent_id())));
            comment.setParent(pcomment);
        }
        Comment newComment = commentRepository.save(comment);
        return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getAllComments(long postId) {
//        List<CommentDto> comments = commentRepository.findByPostId(postId).stream().map((comment1)->mapToDto(comment1)).collect(Collectors.toList());
        List<Comment> comments = commentRepository.findByPostId(postId);
        List<CommentDto> list = new ArrayList<>();
        for (int i = comments.size() - 1; i >= 0; i--) {

            if (comments.get(i).getParent() != null) {
                final long id = comments.get(i).getId();
                Comment com = getCommentId(postId, comments.get(i).getParent().getId());
                CommentDto dto1 = null;
                for (CommentDto dto:list){
                    if(dto.getId() == id){
                        dto1 = dto;
                    }
                }

                System.out.println("COM2 "+dto1);
                System.out.println("LIST "+list.toString());
                CommentDto comD = mapToDto(com);

                if (dto1 != null) {
                    comD.setParent(dto1);
                    list.remove(dto1);
                } else {
                    comD.setParent(mapToDto(comments.get(i)));
                }
//                comments.re(comments.get(i));
//                CommentDto child = mapToDto(comments.get(i));
//                comD.setParent(child);
                list.add(comD);
            } else {
                CommentDto dto1 = null;

                for (CommentDto dto:list){
                    if(dto.getId() == comments.get(i).getId()){
                        dto1 = dto;
                    }
                }
                if(dto1 == null){
                    list.add(mapToDto(comments.get(i)));
                }
            }
        }
        System.out.println("Comments: " + list);
        List<CommentDto> temp = list;
        for (int i=0;i<temp.size()-1;i++){
            for(int j=0;j< temp.size()-1 && j != i;j++){
                if(temp.get(i).getId() == temp.get(j).getId()){
                    if(temp.get(i).getParent() == null){
                        list.remove(i);
                    }else{
                        list.remove(j);
                    }
                }
            }
        }
        return list;
    }

    private Comment getCommentId(long postId, long id) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", String.valueOf(postId)));

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", String.valueOf(id)));
        if (!Objects.equals(comment.getPost().getId(), post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comments does not belongs to post");
        }
        return comment;
    }

    @Override
    public CommentDto getCommentById(long postId, long id) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", String.valueOf(postId)));

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", String.valueOf(id)));
        if (!Objects.equals(comment.getPost().getId(), post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comments does not belongs to post");
        }
//
        System.out.println("Comment " + comment);
        List<Comment> comments = commentRepository.findByParentId(id);
//        if(comments.size()>0){
//            for(Comment com:comments){
//                List<Comment> comment1 = commentRepository.findByParentId(com.getId());
//                if(comment1 != null){
//                    com.setReply(comment1);
//                }
//            }
//        }
        CommentDto commentDto = mapToDto(comment);
        commentDto.setReply(comments.stream().map(reply -> mapToDto(reply)).collect(Collectors.toList()));
        return commentDto;
    }

    @Override
    public void deleteComment(long postId, long id) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", String.valueOf(postId)));

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", String.valueOf(id)));
        if (!Objects.equals(comment.getPost().getId(), post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comments does not belongs to post");
        }
        commentRepository.delete(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long id, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", String.valueOf(postId)));

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", String.valueOf(id)));
        if (!Objects.equals(comment.getPost().getId(), post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comments does not belongs to post");
        }
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        Comment updateComment = commentRepository.save(comment);
        return mapToDto(updateComment);
    }

    private CommentDto mapToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());
        if (comment.getParent() != null)
            commentDto.setParent_id(comment.getParent().getId());
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());
        return comment;
    }
}
