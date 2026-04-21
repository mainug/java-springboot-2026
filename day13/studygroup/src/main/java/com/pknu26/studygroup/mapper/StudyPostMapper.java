package com.pknu26.studygroup.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pknu26.studygroup.dto.StudyPost;

@Mapper
public interface StudyPostMapper {

    List<StudyPost> findAll();

    StudyPost findById(Long postId);

    void insertPost(StudyPost studyPost);

    void deletePost(Long postId);

    void increaseViewCount(Long postId);

    void updatePost(StudyPost studyPost);

}
