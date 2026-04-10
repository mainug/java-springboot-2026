package com.pknu26.webboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pknu26.webboard.entity.Board;
import com.pknu26.webboard.repository.BoardRepository;

@RequestMapping("/board") // 공통 URL http://localhost:8080/board
@Controller
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    // BoardRepository와 같은 객체를 불러오면 Controller에서 사용할 수 있도록 주입(DI)
    // public BoardController(BoardRepository boardRepository) {
    //     this.boardRepository = boardRepository;
    // }

    // Model은 파라미터만 지정하면 사용가능
    @GetMapping("/list") // 상세 URL http://localhost:8080/board/list
    public String showList(Model model) {
        List<Board> boardList = this.boardRepository.findAll(); // Board 테이블 데이터 리스트

        model.addAttribute("boardList", boardList); // HTML로 보낼 모델데이터 설정
        return "board_list"; // board_list.html 파일 필요
    }

    @GetMapping("/detail/{bno}")
    public String showDetail(Model model, @PathVariable("bno") Long bno) {
        Board board = this.boardRepository.getById(bno); // ID로 게시글 조회
        model.addAttribute("board", board);
        return "board_detail"; // board_detail.html로 이동
    }

}