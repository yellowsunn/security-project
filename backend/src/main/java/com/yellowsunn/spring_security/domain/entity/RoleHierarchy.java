package com.yellowsunn.spring_security.domain.entity;

import lombok.*;

import javax.persistence.*;

/**
 * 권한 계층구조 엔티티
 * ROLE_USER, ROLE_MANGER, ROLE_ADMIN 순으로 계층구조를 가진다.
 *  - ex) ROLE_ADMIN 의 부모는 ROLE_MANAGER 이고, ROLE_MANAGER 의 부모는 ROLE_USER 이다
 * 스프링 시큐리티에서 권한을 부여할때 DB에서 모든 부모를 조회한다음 권한을 설정한다.
 *  - ADMIN 계정은 ROLE_ADMIN, ROLE_MANAGER, ROLE_USER 권한을 모두 갖는다
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
@Getter
public class RoleHierarchy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_hierarchy_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id")
    private RoleHierarchy parent;
}
