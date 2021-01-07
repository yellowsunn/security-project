package com.yellowsunn.spring_security.domain.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
@Getter
public class Board {

    @Id @GeneratedValue
    @Column(name = "board_id")
    public Long id;

    @CreatedDate
    @Column(updatable = false)
    public LocalDateTime createdDate;

    public String title;

    @Column(columnDefinition = "text")
    public String content;

    @ColumnDefault("0")
    public Long hit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    public Account account;

    @OneToMany(mappedBy = "board")
    public List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    public List<Image> images = new ArrayList<>();

    @PrePersist
    private void initHit() {
        if (hit == null) hit = 0L;
    }
}
