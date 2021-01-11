package com.yellowsunn.spring_security.service.impl;

import com.yellowsunn.spring_security.domain.dto.CommentDto;
import com.yellowsunn.spring_security.domain.entity.Board;
import com.yellowsunn.spring_security.domain.entity.Comment;
import com.yellowsunn.spring_security.repository.BoardRepository;
import com.yellowsunn.spring_security.repository.CommentRepository;
import com.yellowsunn.spring_security.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Override
    public HttpStatus postComment(CommentDto commentDto) {

        Optional<Board> boardOptional = boardRepository.findById(commentDto.getPostId());
        if (boardOptional.isEmpty()) return HttpStatus.NOT_FOUND;

        Comment.CommentBuilder builder = Comment.builder()
                .content(commentDto.getText())
                .board(boardOptional.get());

        if (commentDto.getMainCommentId() != null) {
            Optional<Comment> commentOptional = commentRepository.findById(commentDto.getMainCommentId());
            if (commentOptional.isEmpty()) return HttpStatus.NOT_FOUND;

            builder.mainComment(commentOptional.get());
        }

        Comment comment = builder.build();
        Comment saveComment = commentRepository.save(comment);

        String orderNumber = String.valueOf(saveComment.getId());
        if (saveComment.getMainComment() != null) {
            orderNumber = saveComment.getMainComment().getId() + "_" + saveComment.getId();
        }
        comment.updateOrderNumber(orderNumber);

        return HttpStatus.OK;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentDto> findByPostId(Long postId, Pageable pageable) {
        Optional<Board> boardOptional = boardRepository.findById(postId);
        if (boardOptional.isEmpty()) return null;

        Page<Comment> commentPage = commentRepository.findCustomByBoard(boardOptional.get(), pageable);
        return commentPage.map(comment ->
            CommentDto.builder()
                    .commentId(comment.getId())
                    .mainCommentId(comment.getMainComment() != null ? comment.getMainComment().getId() : null)
                    .text(comment.getContent())
                    .subComment(comment.getSubComment().stream().map(subComment ->
                            CommentDto.builder()
                                    .commentId(subComment.getId())
                                    .text(subComment.getContent())
                                    .build()
                    ).collect(Collectors.toList()))
                    .build()
        );
    }
}
