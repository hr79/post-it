package com.example.postItBackend.domain.post;

import com.example.postItBackend.common.response.ApiResponse;
import com.example.postItBackend.domain.post.dto.PostRequestDto;
import com.example.postItBackend.domain.post.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false, value = "page") int pageNo) {
        PageRequest pageRequest = PageRequest.of(pageNo, 20, Sort.by(Sort.Direction.DESC,"id"));
        List<PostResponseDto> responseDtoList = postService.getAllPosts(pageRequest);

        return ResponseEntity.ok().body(ApiResponse.success(responseDtoList, HttpStatus.OK.value(), "글 목록 가져오기 성공"));
    }

    // 글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deletePost(@PathVariable("boardId") Long id,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        PostResponseDto dto = postService.deletePost(id, userDetails);

        return ResponseEntity.ok(ApiResponse.success(dto,HttpStatus.OK.value(), "게시글이 삭제되었습니다."));
    }

    // 글 작성
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequestDto postRequestDto,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        PostResponseDto postResponseDto = postService.savePost(postRequestDto, userDetails);

        return ResponseEntity.ok().body(ApiResponse.success(postResponseDto,HttpStatus.CREATED.value(), "글 작성 완료"));
    }

    // 글 상세
    @GetMapping("/{boardId}")
    public ResponseEntity<?> getPost(@PathVariable("boardId") Long id) {

        PostResponseDto postResponseDto = postService.getPost(id);

        return  ResponseEntity.ok().body(ApiResponse.success(postResponseDto, HttpStatus.OK.value(), "게시글 불러오기 성공"));
    }

    // 글 수정
    @PatchMapping("/{boardId}")
    public ResponseEntity<?> updatePost(@PathVariable("boardId") Long id,
                                                   @RequestBody PostRequestDto postRequestDto,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        PostResponseDto dto = postService.updatePost(id, postRequestDto, userDetails);

        return ResponseEntity.ok().body(ApiResponse.success(dto, HttpStatus.OK.value(), "글 수정 완료"));
    }
}
