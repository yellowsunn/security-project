package com.yellowsunn.spring_security.domain.entity;

import lombok.*;

import javax.persistence.*;

/**
 * 사용자와 권한정보를 담은 엔티티
 */

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
@Getter
public class AccountRole {
    @Id @GeneratedValue
    @Column(name = "acount_role_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;

    public void changeRole(Role role) {
        if (this.role != role) {
            this.role = role;
        }
    }
}
