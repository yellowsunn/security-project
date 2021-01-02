package com.yellowsunn.spring_security.domain.dto;

import lombok.Data;

@Data
public class SyncDto {
    private String username;
    private boolean isChanged;
    private boolean isDeleted;
}
