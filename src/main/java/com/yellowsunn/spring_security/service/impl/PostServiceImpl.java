package com.yellowsunn.spring_security.service.impl;

import com.yellowsunn.spring_security.domain.dto.PostDto;
import com.yellowsunn.spring_security.domain.entity.Account;
import com.yellowsunn.spring_security.domain.entity.Board;
import com.yellowsunn.spring_security.domain.entity.Image;
import com.yellowsunn.spring_security.repository.AccountRepository;
import com.yellowsunn.spring_security.repository.BoardRepository;
import com.yellowsunn.spring_security.repository.ImageRepository;
import com.yellowsunn.spring_security.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final BoardRepository boardRepository;
    private final ImageRepository imageRepository;
    private final AccountRepository accountRepository;
    @Value("${file.upload.directory}")
    private String uploadPath;
    private Map<String, String> imageNameUUID = new HashMap<>();

    @Override
    public HttpStatus post(PostDto postDto, List<MultipartFile> multipartFiles) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<Account> accountOptional = accountRepository.findByUsername(auth.getName());
        if (accountOptional.isEmpty()) throw new IllegalStateException("Invalid user");

        // 이미지 이름을 UUID로 변경
        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                String imageName = multipartFile.getOriginalFilename();
                imageNameUUID.put(imageName, generateUUID(imageName));
            }
        }
        changeContentImageName(postDto);

        // Insert Database
        Board board = Board.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .account(accountOptional.get())
                .build();
        boardRepository.save(board);

        for (String imageName : postDto.getImages()) {
            imageRepository.save(Image.builder()
                    .name(imageName)
                    .board(board)
                    .build());
        }

        // 파일 업로드
        if(!uploadImageFile(multipartFiles)) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return HttpStatus.OK;
    }

    @Override
    public void delete(Long postId) {
        Optional<Board> boardOptional = boardRepository.findById(postId);
        if (boardOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid post id");
        }

        Board board = boardOptional.get();
        deleteImageFile(board);
        boardRepository.delete(board);
    }

    // UUID 생성
    private String generateUUID(String imageName) {
        String[] split = imageName.split("[.]");
        String type = "." + split[split.length - 1];
        return UUID.randomUUID().toString() + type;
    }

    private void changeContentImageName(PostDto postDto) {
        if (imageNameUUID.size() == 0) return;
        List<String> imageNames = new ArrayList<>();
        imageNameUUID.forEach((imageName, uuid) -> {
            String content = postDto.getContent();
            postDto.setContent(content.replace(imageName, uuid));
            imageNames.add(uuid);
        });
        postDto.setImages(imageNames);
    }

    private boolean uploadImageFile(List<MultipartFile> multipartFiles) {
        if (multipartFiles != null) {
            File folder = new File(uploadPath);
            if (!folder.exists()) {
                boolean makeDir = folder.mkdirs();
                if (!makeDir) {
                    return false;
                }
            }
            for (MultipartFile multipartFile : multipartFiles) {
                String uuid = imageNameUUID.get(multipartFile.getOriginalFilename());
                try {
                    FileOutputStream fos = new FileOutputStream(uploadPath + uuid);
                    fos.write(multipartFile.getBytes());
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return true;
    }

    private void deleteImageFile(Board board) {
        List<String> imageNames = board.getImages().stream()
                .map(Image::getName)
                .collect(Collectors.toList());

        for (String imageName : imageNames) {
            File file = new File(uploadPath + imageName);
            if (file.exists()) {
                if (!file.delete()) {
                    throw new IllegalStateException("Failed to delete file");
                }
            }
        }
    }
}
