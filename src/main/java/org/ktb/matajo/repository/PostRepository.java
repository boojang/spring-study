package org.ktb.matajo.repository;

import org.ktb.matajo.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    /**
     * 게시글 목록을 Pageable을 이용하여 조회합니다.
     * deletedAt이 null인 게시글만 조회합니다 (삭제되지 않은 게시글).
     * 생성일 기준 내림차순(최신순)으로 정렬합니다.
     * 
     * @param pageable 페이징 정보
     * @return 게시글 목록
     */
    @Query("SELECT p FROM Post p WHERE p.deletedAt IS NULL ORDER BY p.createdAt DESC")
    List<Post> findAllActivePostsOrderByCreatedAtDesc(Pageable pageable);
} 