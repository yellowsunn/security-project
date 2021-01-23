package com.yellowsunn.spring_security.controller.api;

import com.yellowsunn.spring_security.domain.dto.CommentDto;
import com.yellowsunn.spring_security.domain.dto.PostDto;
import com.yellowsunn.spring_security.domain.dto.SimplePostDto;
import com.yellowsunn.spring_security.service.CommentService;
import com.yellowsunn.spring_security.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final PostService postService;
    private final CommentService commentService;
    @Value("${file.upload.directory}")
    private String uploadPath;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(PostDto postDto, @RequestParam(value = "imageFile", required = false) List<MultipartFile> multipartFiles) throws IOException {
        HttpStatus httpStatus = postService.post(postDto, multipartFiles);
        return new ResponseEntity<>(httpStatus);
    }

    @PostMapping("/comment/upload")
    public ResponseEntity<?> commentUpload(@RequestBody CommentDto commentDto) {
        HttpStatus httpStatus = commentService.postComment(commentDto);
        return new ResponseEntity<>(httpStatus);
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(PostDto postDto, @RequestParam(value = "imageFile", required = false) List<MultipartFile> multipartFiles,
                                    HttpServletRequest request) {
        HttpStatus httpStatus = postService.update(postDto, multipartFiles, getServerImgUrl(request));
        return new ResponseEntity<>(httpStatus);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody PostDto postDto) {
        HttpStatus httpStatus = postService.delete(postDto.getId(), postDto.getWriter());
        return new ResponseEntity<>(httpStatus);
    }

    @DeleteMapping("/comment/delete")
    public ResponseEntity<?> commentDelete(@RequestBody CommentDto commentDto) {
        HttpStatus httpStatus = commentService.deleteByCommentId(commentDto.getCommentId(), commentDto.getWriter());
        return new ResponseEntity<>(httpStatus);
    }

    @GetMapping("")
    public Page<SimplePostDto> list(@RequestParam @Nullable String title,
                                    @RequestParam("writer") @Nullable String username,
                                    @PageableDefault(size = 10) Pageable pageable) {
        return postService.findAll(title, username, pageable);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable("postId") Long postId, HttpServletRequest request) {
        Optional<PostDto> postDtoOptional = postService.findById(postId, getServerImgUrl(request));
        if (postDtoOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(postDtoOptional.get(), HttpStatus.OK);
    }

    @GetMapping("/comment/{postId}")
    public Page<CommentDto> commentList(@PathVariable("postId") Long postId, @PageableDefault(size = 100) Pageable pageable) {
        return commentService.findByPostId(postId, pageable);
    }

    @GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> showImage(@PathVariable("imageName") String imageName) throws IOException {
        FileInputStream fis = new FileInputStream(uploadPath + imageName);
        byte[] bytes = fis.readAllBytes();
        fis.close();
        return new ResponseEntity<>(bytes, HttpStatus.OK);
    }

    private String getServerImgUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/api/board/image/";
    }
}
