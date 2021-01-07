package com.yellowsunn.spring_security.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
@Getter
public class Image {

    @Id @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @PrePersist
    private void changeName() {
        String[] split = name.split("[.]");
        String type = "." + split[split.length - 1];
        this.name = UUID.randomUUID().toString() + type;
    }
}
