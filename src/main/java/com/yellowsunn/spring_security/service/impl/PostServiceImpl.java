package com.yellowsunn.spring_security.service.impl;

import com.yellowsunn.spring_security.domain.dto.PostDto;
import com.yellowsunn.spring_security.domain.dto.SimplePostDto;
import com.yellowsunn.spring_security.domain.entity.Account;
import com.yellowsunn.spring_security.domain.entity.Board;
import com.yellowsunn.spring_security.domain.entity.Comment;
import com.yellowsunn.spring_security.domain.entity.Image;
import com.yellowsunn.spring_security.repository.AccountRepository;
import com.yellowsunn.spring_security.repository.BoardRepository;
import com.yellowsunn.spring_security.repository.ImageRepository;
import com.yellowsunn.spring_security.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

    @Override
    public HttpStatus post(PostDto postDto, List<MultipartFile> multipartFiles) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<Account> accountOptional = accountRepository.findByUsername(auth.getName());
        if (accountOptional.isEmpty()) return HttpStatus.UNAUTHORIZED;

        Map<String, String> imageNameUUID = new HashMap<>();
        // 이미지 이름을 UUID로 변경
        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                String imageName = multipartFile.getOriginalFilename();
                imageNameUUID.put(imageName, generateUUID(imageName));
            }
            changeContentImageName(postDto, imageNameUUID);
        }

        long count = boardRepository.count(); // 게시글 번호 계산을 위한 카운트 호출

        // Insert Database
        Board board = Board.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .no(count + 1)
                .writer(accountOptional.get().getUsername())
                .build();
        boardRepository.save(board);

        for (String imageName : postDto.getImages()) {
            imageRepository.save(Image.builder()
                    .name(imageName)
                    .board(board)
                    .build());
        }

        // 파일 업로드
        if(!uploadImageFile(multipartFiles, imageNameUUID)) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return HttpStatus.OK;
    }

    @Override
    public HttpStatus update(PostDto postDto, List<MultipartFile> multipartFiles, String serverImgUrl) {
        // 로그인한 사용자와 같은 사용자인지 확인
        HttpStatus errorStatus = checkSameUser(postDto.getWriter());
        if (errorStatus != null) return errorStatus;

        Optional<Board> boardOptional = boardRepository.findById(postDto.getId());
        if (boardOptional.isEmpty()) return HttpStatus.NOT_FOUND;

        // 추가된 이미지가 있는 경우
        Map<String, String> imageNameUUID = new HashMap<>();
        // 이미지 이름을 UUID로 변경
        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                String imageName = multipartFile.getOriginalFilename();
                imageNameUUID.put(imageName, generateUUID(imageName));
            }
            changeContentImageName(postDto, imageNameUUID);
        }

        Board board = boardOptional.get();
        board.changeTitle(postDto.getTitle());
        // 이미지에 서버 url이 붙은경우 제거
        board.changeContent(postDto.getContent().replaceAll(serverImgUrl, ""));

        for (String imageName : postDto.getImages()) {
            imageRepository.save(Image.builder()
                    .name(imageName)
                    .board(board)
                    .build());
        }

        // 파일 업로드
        if(!uploadImageFile(multipartFiles, imageNameUUID)) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return HttpStatus.OK;
    }

    @Override
    public HttpStatus delete(Long postId, String writer) {
        // 로그인한 사용자와 같은 사용자인지 확인
        HttpStatus errorStatus = checkSameUser(writer);
        if (errorStatus != null) return errorStatus;

        Optional<Board> boardOptional = boardRepository.findById(postId);
        if (boardOptional.isEmpty()) return HttpStatus.NOT_FOUND;

        Board board = boardOptional.get();
        deleteImageFile(board);
        boardRepository.delete(board);
        return HttpStatus.OK;
    }

    @Override
    public Optional<PostDto> findById(Long postId, String serverImgUrl) {

        Optional<Board> boardOptional = boardRepository.findById(postId);
        if (boardOptional.isEmpty()) return Optional.empty();
        Board board = boardOptional.get();
        board.updateHit(); // 조회수 증가

        String content = setContentImageUrl(board, serverImgUrl);
        String postTime = board.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));

        PostDto postDto = PostDto.builder()
                .id(board.getId())
                .writer(board.getWriter())
                .postTime(postTime)
                .hit(board.getHit())
                .title(board.getTitle())
                .content(content)
                .build();

        return Optional.ofNullable(postDto);
    }

    @Override
    public Page<SimplePostDto> findAll(String title, String username, Pageable pageable) {
        Page<Board> boardPage = boardRepository.findSimpleAll(title, username, pageable);

        return boardPage.map(board -> SimplePostDto.builder()
                .id(board.getId())
                .no(board.getNo())
                .writer(board.getWriter())
                .hit(board.getHit())
                .time(getPostTime(board.getCreatedDate()))
                .title(board.getTitle())
                .commentSize(board.getComments().size())
                .hasImage(board.getImages().stream().anyMatch(
                        image -> board.getContent().contains(image.getName())
                ))
                .build()
        );
    }

    // UUID 생성
    private String generateUUID(String imageName) {
        String[] split = imageName.split("[.]");
        String type = "." + split[split.length - 1];
        return UUID.randomUUID().toString() + type;
    }

    private void changeContentImageName(PostDto postDto, Map<String, String> imageNameUUID) {
        if (imageNameUUID.size() == 0) return;
        List<String> imageNames = new ArrayList<>();
        imageNameUUID.forEach((imageName, uuid) -> {
            String content = postDto.getContent();
            postDto.setContent(content.replace(imageName, uuid));
            imageNames.add(uuid);
        });
        postDto.setImages(imageNames);
    }

    private boolean uploadImageFile(List<MultipartFile> multipartFiles, Map<String, String> imageNameUUID) {
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

    private String getPostTime(LocalDateTime createdDate) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime today = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0, 0);
        String postTime;
        if (createdDate.isAfter(today)) {
            postTime = createdDate.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else {
            postTime = createdDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        }

        return postTime;
    }

    private String setContentImageUrl(Board board, String serverImgUrl) {
        List<String> images = board.getImages().stream().map(Image::getName).collect(Collectors.toList());
        String content = board.getContent();
        for (String imageName : images) {
            content = content.replace(imageName, serverImgUrl + imageName);
        }
        return content;
    }

    private HttpStatus checkSameUser(String writer) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<Account> accountOptional = accountRepository.findByUsername(auth.getName());
        // 인증되지 않은경우
        if (accountOptional.isEmpty()) return HttpStatus.UNAUTHORIZED;
        // 게시글 작성자와 수정하는 사용자가 같지 않은 경우
        if (!accountOptional.get().getUsername().equals(writer)) {
            return HttpStatus.FORBIDDEN;
        }
        return null;
    }
}
