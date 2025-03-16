package org.ktb.matajo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ktb.matajo.dto.post.PostResponseDto;
import org.ktb.matajo.global.common.CommonResponse;
import org.ktb.matajo.service.post.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//로깅 기능
@Slf4j
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // javaDoc
    /**
     * 게시글 목록을 조회합니다.
     *
     * @param offset 결과 집합에서 건너뛸 항목 수
     * @param limit 반환되는 레코드 수 제한
     * @return 게시글 목록
     */
    @GetMapping
    public ResponseEntity<CommonResponse<List<PostResponseDto>>> getPostList(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {
        
//        log.info("게시글 목록 조회 요청: offset={}, limit={}", offset, limit);
        
        List<PostResponseDto> postList = postService.getPostList(offset, limit);
        
        return ResponseEntity.ok(CommonResponse.success("get_posts_success", postList));
    }
} 