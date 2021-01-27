package com.yellowsunn.spring_security.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDto {

    private Long id;

    private String writer;
    private String text;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "a hh:mm", timezone = "Asia/Seoul")
    private LocalDateTime date;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 M월 d일 eeee", timezone = "Asia/Seoul")
    private LocalDateTime fullDate;
    // 특정날짜의 첫번째 데이터인지 체크
    private boolean isDayFirst;
}
