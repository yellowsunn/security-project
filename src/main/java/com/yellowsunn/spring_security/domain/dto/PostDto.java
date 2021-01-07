package com.yellowsunn.spring_security.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// 게시글 DTO
@Data
public class PostDto {

    private String title;
    private String content;

    private List<Comment> comments = new ArrayList<>();

    private String writer;
    private String postTime;
    private Long hit;

    @Data
    private static class Comment {
        private String content;
        private Long time;
        private List<Comment> subComments = new ArrayList<>();
    }
}
