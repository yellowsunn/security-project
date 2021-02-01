package com.yellowsunn.spring_security.domain.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 채팅 엔티티
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
@Getter
public class Chat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @CreatedDate
    public LocalDateTime createdDate;

    private String writer;

    private String text;

    // 특정날짜의 첫번째 데이터인지 체크
    private boolean isDayFirst;

    public void updateDayFirst() {
        isDayFirst = true;
    }
}
