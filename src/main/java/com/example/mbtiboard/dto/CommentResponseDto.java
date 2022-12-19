package com.example.mbtiboard.dto;

import com.example.mbtiboard.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long num;
    private String username;

    private String content;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private Long likeCount=0l;

    public CommentResponseDto(Comment comment) {
        this.num = comment.getId();
        this.username = comment.getUser().getUsername();
        this.content = comment.getContent();
        this.createAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }

    public CommentResponseDto(Comment comment, Long commentLikeCnt) {
        this.num = comment.getId();
        this.username = comment.getUser().getUsername();
        this.content = comment.getContent();
        this.createAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.likeCount = commentLikeCnt;
    }

}