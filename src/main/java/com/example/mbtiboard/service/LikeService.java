package com.example.mbtiboard.service;

import com.example.mbtiboard.dto.MsgResponseDto;
import com.example.mbtiboard.entity.*;
import com.example.mbtiboard.repository.LikeCommentRepository;
import com.example.mbtiboard.repository.CommentRepository;
import com.example.mbtiboard.repository.LikePostRepository;
import com.example.mbtiboard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikePostRepository likePostRepository;
    private final LikeCommentRepository likeCommentRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public MsgResponseDto likePost(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        LikePost likePost = likePostRepository.findByUserAndPost(user, post);
        if (likePost == null) {
            LikePost likesPost = new LikePost(user, post);
            likePostRepository.save(likesPost);
            return new MsgResponseDto("게시글 좋아요", HttpStatus.OK.value());
        } else {
            likePostRepository.deleteById(likePost.getId());
            return new MsgResponseDto("게시글 좋아요 취소", HttpStatus.OK.value());
        }
    }

    @Transactional
    public MsgResponseDto likeComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );

        LikeComment likeComment = likeCommentRepository.findByUserAndComment(user, comment);
        if (likeComment == null) {
            LikeComment likesComment = new LikeComment(user, comment);
            likeCommentRepository.save(likesComment);
            return new MsgResponseDto("댓글 좋아요", HttpStatus.OK.value());
        } else {
            likeCommentRepository.deleteById(likeComment.getId());
            return new MsgResponseDto("댓글 좋아요 취소", HttpStatus.OK.value());
        }
    }
}
