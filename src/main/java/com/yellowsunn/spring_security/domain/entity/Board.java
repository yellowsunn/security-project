package com.yellowsunn.spring_security.domain.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 게시판 엔티티
 */

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
@Getter
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private Long no;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    private String title;

    @Column(columnDefinition = "mediumtext")
    private String content;

    @ColumnDefault("0")
    private Long hit;

    private String writer;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Image> images = new ArrayList<>();

    @PrePersist
    private void initHit() {
        if (hit == null) hit = 0L;
    }

    public void updateHit() {
        this.hit += 1;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
