package com.pknu26.studygroup.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pknu26.studygroup.dto.BoardFile;

@Mapper
public interface BoardFileMapper {
    void insertFile(BoardFile BoardFile);
    List<BoardFile> findByBoardId(Long boardId);
    // 특정 파일의 정보를 가져올 때 (실제 파일을 지우기 위해 경로가 필요함)
    BoardFile findByFileId(Long fileId);
    void deleteFile(Long fileId);
}
