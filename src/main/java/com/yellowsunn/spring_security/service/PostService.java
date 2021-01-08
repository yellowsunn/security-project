package com.yellowsunn.spring_security.service;

import com.yellowsunn.spring_security.domain.dto.PostDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    HttpStatus post(PostDto postDto, List<MultipartFile> multipartFiles);

    void delete(Long postId);
}
