package com.yellowsunn.spring_security.domain.entity;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentTest {

    @Autowired EntityManager em;

    @Test
    public void persistTest() {
        Comment comment = Comment.builder().content("comment").build();
        em.persist(comment);

        Comment subComment1 = Comment.builder().content("subcomment1").mainComment(comment).build();
        Comment subComment2 = Comment.builder().content("subcomment2").mainComment(comment).build();
        em.persist(subComment1);
        em.persist(subComment2);

        em.flush();
        em.clear();

        Comment findComment = em.find(Comment.class, comment.getId());

        List<String> findSubComments = findComment.getSubComment()
                .stream().map(Comment::getContent)
                .collect(Collectors.toList());

        assertThat(findSubComments)
                .contains(subComment1.getContent(), subComment2.getContent());
    }
}