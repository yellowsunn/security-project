package com.yellowsunn.spring_security.controller;

import com.yellowsunn.spring_security.domain.dto.PostDto;
import com.yellowsunn.spring_security.service.PostService;
import com.yellowsunn.spring_security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class TestController {

    private final UserService userService;
    private final PostService postService;

//    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public void test(@RequestParam("image") MultipartFile multipartFiles) throws IOException {
//        FileOutputStream fos = new FileOutputStream(multipartFiles.getOriginalFilename());
//        fos.write(multipartFiles.getBytes());
//        fos.flush();
//        fos.close();
//        log.info(multipartFiles.getName());
//    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String test(@RequestParam("image") List<MultipartFile> multipartFiles) throws IOException {
        for (MultipartFile file : multipartFiles) {
            log.info(file.getOriginalFilename());
        }
        FileOutputStream fos = new FileOutputStream("./src/main/resources/static/" + multipartFiles.get(0).getOriginalFilename());
        fos.write(multipartFiles.get(0).getBytes());
        fos.flush();
        fos.close();
        return "http://localhost:8080/image/" + multipartFiles.get(0).getOriginalFilename();
    }

    @PostMapping("/api/test")
    public void test(@RequestBody PostDto postDto) {
        postService.post(postDto, null);
    }
}
