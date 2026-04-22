package com.pknu26.studygroup.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pknu26.studygroup.dto.StudyPost;

@Mapper
public interface StudyPostMapper {

    List<StudyPost> findAll(@Param("offset") int offset, @Param("size") int size);

    int getTotalCount();

    StudyPost findById(Long postId);

    void insertPost(StudyPost studyPost);

    void deletePost(Long postId);

    void increaseViewCount(Long postId);

    void updatePost(StudyPost studyPost);

    // 260422. 상태 변경용 추가 메서드
    void updatePostStatus(@Param("postId") Long postId,
                          @Param("status") String status);

}
