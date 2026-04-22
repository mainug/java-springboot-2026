package com.pknu26.studygroup.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class StudyPost {

    private Long postId;
    private Long userId;
    private Long categoryId;
    private String title;
    private String content;
    private Integer maxMembers;
    private String status;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 조인 조회용
    private String writerName;
    private String categoryName;
    
}
