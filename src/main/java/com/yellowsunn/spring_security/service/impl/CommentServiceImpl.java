package com.yellowsunn.spring_security.service.impl;

import com.yellowsunn.spring_security.domain.dto.CommentDto;
import com.yellowsunn.spring_security.domain.entity.Account;
import com.yellowsunn.spring_security.domain.entity.Board;
import com.yellowsunn.spring_security.domain.entity.Comment;
import com.yellowsunn.spring_security.repository.AccountRepository;
import com.yellowsunn.spring_security.repository.BoardRepository;
import com.yellowsunn.spring_security.repository.CommentRepository;
import com.yellowsunn.spring_security.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final AccountRepository accountRepository;

    @Override
    public HttpStatus postComment(CommentDto commentDto) {
        // 현재 로그인된 사용자를 찾는다.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<Account> accountOptional = accountRepository.findByUsername(auth.getName());
        if (accountOptional.isEmpty()) throw new IllegalStateException("Invalid user");

        // 댓글이 작성되는 게시글을 찾는다.
        Optional<Board> boardOptional = boardRepository.findById(commentDto.getPostId());
        if (boardOptional.isEmpty()) return HttpStatus.NOT_FOUND;

        Comment.CommentBuilder builder = Comment.builder()
                .account(accountOptional.get())
                .content(commentDto.getText())
                .board(boardOptional.get());

        // 댓글의 댓글인 경우
        if (commentDto.getMainCommentId() != null) {
            Optional<Comment> commentOptional = commentRepository.findById(commentDto.getMainCommentId());
            if (commentOptional.isEmpty()) return HttpStatus.NOT_FOUND;

            builder.mainComment(commentOptional.get());
        }

        Comment comment = builder.build();
        Comment saveComment = commentRepository.save(comment);

        // orderNumber : '메인 댓글 Id'_'서브 댓글 Id'
        String orderNumber = String.valueOf(saveComment.getId());
        if (saveComment.getMainComment() != null) {
            orderNumber = saveComment.getMainComment().getId() + "_" + saveComment.getId();
        }
        // orderNumber 업데이트
        comment.updateOrderNumber(orderNumber);

        return HttpStatus.OK;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentDto> findByPostId(Long postId, Pageable pageable) {
        // id에 해당하는 게시글을 불러온다.
        Optional<Board> boardOptional = boardRepository.findById(postId);
        if (boardOptional.isEmpty()) return null;

        Page<Comment> commentPage = commentRepository.findCustomByBoard(boardOptional.get(), pageable);
        return commentPage.map(comment ->
                CommentDto.builder()
                        .commentId(comment.getId())
                        .mainCommentId(comment.getMainComment() != null ? comment.getMainComment().getId() : null)
                        .writer(comment.getAccount().getUsername())
                        .time(getTime(comment.getCreatedDate()))
                        .text(comment.getContent())
                        .subComment(comment.getSubComment().stream().map(subComment ->
                                CommentDto.builder()
                                        .commentId(subComment.getId())
                                        .writer(subComment.getAccount().getUsername())
                                        .time(getTime(subComment.getCreatedDate()))
                                        .text(subComment.getContent())
                                        .build()
                        ).collect(Collectors.toList()))
                        .build()
        );
    }

    // 시간 변환
    private String getTime(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }
}


