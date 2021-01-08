package com.yellowsunn.spring_security.controller.api;

import com.yellowsunn.spring_security.domain.dto.PostDto;
import com.yellowsunn.spring_security.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final PostService postService;
    @Value("${file.upload.directory}")
    private String uploadPath;
    private Map<String, String> imageNameUUID = new HashMap<>();

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(PostDto postDto, @RequestParam(value = "imageFile", required = false) List<MultipartFile> multipartFiles) throws IOException {
        HttpStatus httpStatus = postService.post(postDto, multipartFiles);
        return new ResponseEntity<>(httpStatus);
    }

    @DeleteMapping("/delete/{postId}")
    public void delete(@PathVariable("postId") Long postId) {
        // 권한 비교
        // if (권한이 맞아야)
        postService.delete(postId);
    }

    @GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> showImage(@PathVariable("imageName") String imageName) throws IOException {
        FileInputStream fis = new FileInputStream(uploadPath + imageName);
        byte[] bytes = fis.readAllBytes();
        fis.close();
        return new ResponseEntity<>(bytes, HttpStatus.OK);
    }
}
