package com.yellowsunn.spring_security.domain.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;

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

    @Test
    void deleteTest() {
        Account account = Account.builder().username("test").password("test").build();
        em.persist(account);

        Board board = Board.builder()
                .account(account)
                .title("title")
                .content("content")
                .build();
        em.persist(board);

        em.persist(Image.builder()
                .name("test_image1.png")
                .board(board)
                .build());

        em.persist(Image.builder()
                .name("test_image2.jpg")
                .board(board)
                .build());

        Comment comment = Comment.builder().content("comment")
                .board(board)
                .build();
        em.persist(comment);

        Comment subComment1 = Comment.builder().content("subcomment1").mainComment(comment).build();
        Comment subComment2 = Comment.builder().content("subcomment2").mainComment(comment).build();
        em.persist(subComment1);
        em.persist(subComment2);

        em.flush();
        em.clear();

        Board findBoard = em.find(Board.class, board.getId());
        em.remove(findBoard);

        em.flush();
    }
}