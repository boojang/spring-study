package org.ktb.matajo.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ktb.matajo.dto.post.PostResponseDto;
import org.ktb.matajo.entity.Image;
import org.ktb.matajo.entity.Post;
import org.ktb.matajo.entity.PostTag;
import org.ktb.matajo.global.error.exception.GeneralException;
import org.ktb.matajo.global.error.exception.PostException;
import org.ktb.matajo.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    
    private final PostRepository postRepository;

    /**
     * 게시글 목록을 조회합니다.
     *
     * @param offset 결과 집합에서 건너뛸 항목 수
     * @param limit 반환되는 레코드 수 제한
     * @return 게시글 목록 DTO
     * @throws GeneralException 게시글이 없는 경우 예외 발생
     */
    @Transactional(readOnly = true)
    public List<PostResponseDto> getPostList(int offset, int limit) {
        // 요청 파라미터 검증
        if (offset < 0 || limit <= 0) {
            throw new GeneralException("invalid_offset_or_limit");
        }

        // 페이징 처리된 게시글 목록 조회
        Pageable pageable = PageRequest.of(offset / limit, limit);
        List<Post> posts = postRepository.findAllActivePostsOrderByCreatedAtDesc(pageable);

        if (posts.isEmpty()) {
            throw new PostException("not_found_posts_page");
        }

        // 엔티티를 DTO로 변환
        return posts.stream()
                .map(this::convertToPostResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Post 엔티티를 PostListResponseDto로 변환합니다.
     *
     * @param post Post 엔티티
     * @return PostListResponseDto
     */
    private PostResponseDto convertToPostResponseDto(Post post) {
        // 대표 이미지 URL 가져오기 (썸네일 이미지 또는 첫 번째 이미지)
        String mainImageUrl = post.getImageList().stream()
                .filter(Image::isThumbnailStatus)
                .findFirst()
                .map(Image::getImageUrl)
                .orElseGet(() -> post.getImageList().isEmpty() ? null : post.getImageList().get(0).getImageUrl());

        // 태그 목록 추출
        List<String> tags = post.getPostTagList().stream()
                .map(PostTag::getTag)
                .map(tag -> tag.getTagName())
                .collect(Collectors.toList());

        return PostResponseDto.builder()
                .post_id(post.getId())
                .post_title(post.getTitle())
                .post_main_image(mainImageUrl)
                .post_address(post.getAddress().getAddress())
                .prefer_price(post.getPreferPrice())
                .post_tags(tags)
                .build();
    }

}
