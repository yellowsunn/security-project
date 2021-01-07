package com.yellowsunn.spring_security.domain.dto;

import lombok.Data;

// 간단한 게시글 정보를 담은 DTO
@Data
public class SimplePostDto {

    private String title;
    private int commentSize;
    private String writer;
    private Long hit;
    private String time;
    private Boolean hasImage;
}
