package com.yellowsunn.spring_security.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 댓글 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class CommentDto {

    private Long commentId;

    private String text; // 댓글 내용

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long postId; // 게시글 아이디

    private Long mainCommentId; // 서브 댓글인경우에만 메인 댓글의 commentId를 등록

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CommentDto> subComment = new ArrayList<>();
}
