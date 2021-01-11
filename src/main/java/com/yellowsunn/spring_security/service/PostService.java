package com.yellowsunn.spring_security.service;

import com.yellowsunn.spring_security.domain.dto.PostDto;
import com.yellowsunn.spring_security.domain.dto.SimplePostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface PostService {

    HttpStatus post(PostDto postDto, List<MultipartFile> multipartFiles);

    void delete(Long postId);

    Optional<PostDto> findById(Long postId, String serverImgUrl);

    Page<SimplePostDto> findAll(String title, String username, Pageable pageable);
}
