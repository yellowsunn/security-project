package com.yellowsunn.spring_security.domain.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 댓글 엔티티
 */

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
@Getter
public class Comment {

    @Id @GeneratedValue
    @JoinColumn(name = "comment_id")
    private Long id;

    @CreatedDate
    public LocalDateTime createdDate;

    private String content;

    private String orderNumber; // 메인댓글Id_서브댓글Id [ ex) 68_70 ]

    // 댓글의 댓글인 경우 기준이 되는 댓글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_comment_id")
    private Comment mainComment;

    // 댓글의 댓글
    @OneToMany(mappedBy = "mainComment", cascade = CascadeType.REMOVE)
    List<Comment> subComment = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String writer;

    public void updateOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    // 답글이 있을때 댓글 삭제하는 경우
    public void erase() {
        content = null;
        createdDate = null;
        writer = null;
    }
}
