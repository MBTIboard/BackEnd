package com.example.mbtiboard.dto;


import com.example.mbtiboard.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class PostResponseDto {

    private Long id;
    private String title;
    private String username;
    private String content;
    private String userMbti;
    private String cateMbti;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int likeNum;
    private List<CommentResponseDto> commentList = new ArrayList<>();

    public PostResponseDto(Post post){
        this.id = post.getId();
        this.username = post.getUser().getUsername();
        this.userMbti = post.getUser().getUserMbti();
        this.cateMbti = post.getCateMbti();
        this.title  = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.likeNum = post.getPostLikeList().size();
        this.commentList = post.getCommentList().stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }

}
