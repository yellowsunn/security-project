package com.yellowsunn.spring_security.domain.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
@Getter
@EqualsAndHashCode(of = "name")
public class Role {
    @Id @GeneratedValue
    @Column(name = "role_id")
    private Long id;

    @Column(name = "role_name", unique = true)
    private String name;
}
