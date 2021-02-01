package com.yellowsunn.spring_security.domain.dto;

import lombok.Data;

/**
 * ADMIN 페이지에서 사용자 정보의 변경을 웹소켓으로 실시간 동기화하기위한 DTO
 */

@Data
public class SyncDto {
    private String username;
    private boolean isChanged; // 사용자의 정보가 변경된 경우
    private boolean isDeleted; // 사용자가 삭제된 경우
}
