package com.yellowsunn.spring_security.domain.entity;

import lombok.*;

import javax.persistence.*;

/**
 * 이미지의 이름정보를 담은 엔티티
 *  - 게시글을 삭제할때 이미지도 삭제하기 위해서 사용
 */
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
}
