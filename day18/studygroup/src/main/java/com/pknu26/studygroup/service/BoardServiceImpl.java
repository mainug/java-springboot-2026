package com.pknu26.studygroup.service;

import java.io.File;
// import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pknu26.studygroup.config.FileProperties;
import com.pknu26.studygroup.dto.Board;
import com.pknu26.studygroup.dto.BoardFile;
import com.pknu26.studygroup.dto.PageRequest;
import com.pknu26.studygroup.dto.PageResponse;
import com.pknu26.studygroup.mapper.BoardFileMapper;
import com.pknu26.studygroup.mapper.BoardMapper;
import com.pknu26.studygroup.validation.BoardForm;

import lombok.RequiredArgsConstructor;

// BoardService 인터페이스 구현 클래스
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;
    private final BoardFileMapper boardFileMapper;
    private final FileProperties fileProperties;

    // #PRC08 - 구현체에서 데이터 입력을 처리하는 서비스(비즈니스 로직)
    @Override
    public void createBoard(BoardForm boardForm, List<MultipartFile> files) throws Exception {
        // 1. Board 객체 생성 및 값 세팅
        Board board = new Board();
        board.setTitle(boardForm.getTitle());
        board.setContent(boardForm.getContent());
        board.setWriter(boardForm.getWriterId());

        // 2. Board 저장 (selectKey에 의해 board객체의 boardId 필드에 시퀀스 값이 채워짐)
        this.boardMapper.insertBoard(board);

        // 3. 파일 처리
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    // 물리 파일 저장 로직 (기존 saveFile 활용)
                    String originalName = file.getOriginalFilename();
                    String saveName = UUID.randomUUID().toString() + "_" + originalName;
                    file.transferTo(new File(fileProperties.getUploadDir() + saveName));

                    // 4. BoardFile 객체 생성 및 DB 저장
                    BoardFile boardFile = new BoardFile();
                    boardFile.setBoardId(board.getBoardId()); // 자동 채워진 ID 사용!
                    boardFile.setOriginalName(originalName);
                    boardFile.setSaveName(saveName);
                    boardFile.setFilePath(fileProperties.getAccessUrl() + saveName);

                    this.boardFileMapper.insertFile(boardFile);
                }
            }
        }
    }

    // 페이징용으로 전체 변경
    @Override
    public PageResponse<Board> readBoardList(PageRequest pageRequest) {
        List<Board> boardList = 
            this.boardMapper.findAll(pageRequest.getOffset(), pageRequest.getSize());

        int totalCount = this.boardMapper.getTotalCount();

        // return this.boardMapper.findAll();
        return new PageResponse<>(boardList, totalCount, pageRequest.getPage(), pageRequest.getSize());
    }

    @Override
    public Board readBoardById(Long boardId) {
        return this.boardMapper.findById(boardId);
    }

    @Override
    public void deleteBoard(Long boardId) {
        // 1. 삭제할 게시글에 첨부된 파일 목록을 DB에서 가져옴
        List<BoardFile> files = this.boardFileMapper.findByBoardId(boardId);

        // 2. 실제 물리적 파일 삭제
        if (files != null && !files.isEmpty()) {
            String uploadPath = fileProperties.getUploadDir();
            for (BoardFile file : files) {
                File physicalFile = new File(uploadPath + file.getSaveName());
                if (physicalFile.exists()) {
                    physicalFile.delete(); // 하드디스크에서 파일 삭제
                    System.out.println("파일 삭제 완료: " + file.getSaveName());
                }
            }
        }

        // 3. DB에서 게시글 삭제 (이때 CASCADE 설정으로 BOARD_FILE 테이블 데이터도 같이 삭제됨)
        this.boardMapper.deleteBoard(boardId);
    }

    @Override
    public void removeFile(Long fileId) {
        // 1. DB에서 파일 정보 조회 (저장된 이름을 알아야 삭제 가능)
        BoardFile boardFile = boardFileMapper.findByFileId(fileId);
        
        if (boardFile != null) {
            // 2. 실제 물리적 파일 삭제
            String fullPath = fileProperties.getUploadDir() + boardFile.getSaveName();
            File file = new File(fullPath);
            if (file.exists()) {
                file.delete();
            }
            
            // 3. DB 데이터 삭제
            boardFileMapper.deleteFile(fileId);
        }
    }

    @Override
    public void updateBoard(BoardForm boardForm, List<MultipartFile> files) throws Exception {
        // 1. 게시글 기본 정보 업데이트
        Board board = new Board();
        board.setBoardId(boardForm.getBoardId());
        board.setTitle(boardForm.getTitle());
        board.setContent(boardForm.getContent());
        board.setWriter(boardForm.getWriterId());

        this.boardMapper.updateBoard(board);

        // 2. 새로운 첨부파일이 있다면 추가 저장 (createBoard와 동일한 로직)
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String originalName = file.getOriginalFilename();
                    String saveName = UUID.randomUUID().toString() + "_" + originalName;
                    file.transferTo(new File(fileProperties.getUploadDir() + saveName));

                    BoardFile boardFile = new BoardFile();
                    boardFile.setBoardId(board.getBoardId()); // 이미 존재하는 boardId 사용
                    boardFile.setOriginalName(originalName);
                    boardFile.setSaveName(saveName);
                    boardFile.setFilePath(fileProperties.getAccessUrl() + saveName);

                    this.boardFileMapper.insertFile(boardFile);
                }
            }
        }
    }
}
