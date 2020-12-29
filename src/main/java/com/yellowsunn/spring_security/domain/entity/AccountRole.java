package com.yellowsunn.spring_security.domain.entity;

import lombok.*;

import javax.persistence.*;

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

    public boolean changeRole(Role role) {
        if (this.role != role) {
            this.role = role;
            return true;
        } else {
            return false;
        }
    }
}
