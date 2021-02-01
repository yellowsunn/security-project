package com.yellowsunn.spring_security.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// 게시글 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class PostDto {

    private Long id;
    private String title;
    private String content;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> images = new ArrayList<>();

    private String writer;
    private String postTime;
    private Long hit;
}
