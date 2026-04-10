package com.pknu26.webboard.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.pknu26.webboard.entity.Board;
import com.pknu26.webboard.service.BoardService;

@Controller
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BoardService boardService;

    @GetMapping("/home")
    public String home(Model model) {
        logger.info("/home 실행");
        List<Board> boardList = this.boardService.getBoardList();
        // 최신순 정렬이나 개수 제한은 서비스 레벨에서 처리하는 것이 좋지만, 
        // 현재는 전체 리스트를 가져와서 보여줍니다.
        model.addAttribute("boardList", boardList);
        return "home";
    }
}
