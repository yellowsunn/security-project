package com.yellowsunn.spring_security.domain.entity;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
class ImageTest {

    @Autowired EntityManager em;

    @Test
    void persistTest() {
        String imageName = "image_name.png";

        Image image = Image.builder()
                .name(imageName)
                .build();

        em.persist(image);

        em.flush();
        em.clear();

        Image findImage = em.find(Image.class, image.getId());

        log.info(findImage.getName());

        assertThat(findImage.getName())
                .isNotEmpty()
                .isNotEqualTo(imageName);
    }
}