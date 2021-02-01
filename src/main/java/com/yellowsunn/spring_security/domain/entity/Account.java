package com.yellowsunn.spring_security.domain.entity;

import lombok.*;

import javax.persistence.*;

/**
 * 계정 정보를 담은 엔티티
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
@Getter
@EqualsAndHashCode(of = "username")
public class Account {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    public void changePassword(String password) {
        this.password = password;
    }
}
