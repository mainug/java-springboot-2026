package com.pknu26.studygroup.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Comment {

    private Long commentId;
    private Long postId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 조인 조회용
    private String writerName;

}
