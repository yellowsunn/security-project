package com.yellowsunn.spring_security.domain.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BoardTest {
    @Autowired EntityManager em;

    @Test
    void persistTest() {
        Account account = Account.builder().username("test").password("test").build();
        em.persist(account);

        Board board = Board.builder()
                .account(account)
                .title("title")
                .content("content")
                .build();

        em.persist(board);

        assertThat(board.getHit()).isEqualTo(0L);
        assertThat(board.getCreatedDate()).isNotNull();
    }
}