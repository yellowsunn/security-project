package com.yellowsunn.spring_security.repository;

import com.yellowsunn.spring_security.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
