package com.pknu26.studygroup.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class BoardFile {
    private Long fileId;       // FILE_ID (PK)
    private Long boardId;      // BOARD_ID (FK)
    private String originalName;
    private String saveName;
    private String filePath;
    private LocalDateTime createdAt;
}