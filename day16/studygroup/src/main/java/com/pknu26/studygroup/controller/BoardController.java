package com.pknu26.studygroup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pknu26.studygroup.dto.Board;
import com.pknu26.studygroup.dto.BoardFile;
import com.pknu26.studygroup.dto.LoginUser;
import com.pknu26.studygroup.dto.PageRequest;
import com.pknu26.studygroup.mapper.BoardFileMapper;
import com.pknu26.studygroup.service.BoardService;
import com.pknu26.studygroup.service.ReplyService;
import com.pknu26.studygroup.validation.BoardForm;
import com.pknu26.studygroup.validation.ReplyForm;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

// Controller에서 중요한 것은 mapping url. 메서드명 중요치않음
@Controller
@RequestMapping("/board") 
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private ReplyService replyService;  // 댓글도 가져와야 함

    @Autowired
    private BoardFileMapper boardFileMapper;

    // 목록, 페이징 용 변경
    @GetMapping("/list")
    public String list(@ModelAttribute PageRequest pageRequest, Model model) {
        model.addAttribute("response", this.boardService.readBoardList(pageRequest));
        return "/board/list"; // board 폴더 밑에 위치한 list.html을 리턴하라
    }
    
    // 목록상세보기    
    @GetMapping("/detail/{boardId}")
    public String detail(@PathVariable("boardId") Long boardId, Model model) {
        Board board = this.boardService.readBoardById(boardId);

        model.addAttribute("board", board);
        
        // [추가] 첨부파일 리스트 가져오기
        // HTML에서 th:each="file : ${boardFiles}"로 사용하므로 이름을 맞춰줍니다.
        model.addAttribute("boardFiles", this.boardFileMapper.findByBoardId(boardId));

        // 댓글 리스트 추가
        model.addAttribute("replyList", this.replyService.getReplyListByBoardId(boardId));

        ReplyForm replyForm = new ReplyForm();
        replyForm.setBoardId(boardId);
        model.addAttribute("replyForm", replyForm);

        return "/board/detail";
    }

    // 글쓰기 GET
    // #PRC01 - 웹 브라우저에서 URL로 요청
    @GetMapping("/create")  // http://localhost:8080/board/create
    public String showCreateForm(Model model, HttpSession session) {
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/user/login"; // 로그인안한 사람은 게시판 글 못씀
        }

        // 로그인한 계정 정보를 html 전달
        BoardForm boardForm = new BoardForm();
        // 세션에서 넘어온 정보 할당
        boardForm.setWriter(loginUser.getName());
        boardForm.setWriterId(loginUser.getLoginId()); 

        model.addAttribute("boardForm", boardForm);

        // #PRC02 - 입력값 검증 BoardForm을 읽어온 후, /board/form.html에 model로 전달
        return "/board/form"; // .html 작성안함. /baord/form.html 화면 띄움
    }

    // 글쓰기 POST
    // #PRC04 - 폼화면에서 저장버튼 누르면 발생.
    // BoardForm 입력검증 클래스, BindingResult 검증처리
    // 글쓰기 POST 수정
    @PostMapping("/create") 
    public String create(
            @Valid BoardForm boardForm, 
            BindingResult bindingResult, 
            HttpSession session,
            // [추가] HTML의 name="files"와 매칭
            @RequestParam(value = "files", required = false) List<MultipartFile> files 
    ) throws Exception {
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/user/login";
        }        

        if (bindingResult.hasErrors()) {
            return "/board/form";
        }

        // [체크] 파일이 잘 들어오는지 로그로 먼저 확인
        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    System.out.println("업로드 파일명: " + file.getOriginalFilename());
                }
            }
        }

        this.boardService.createBoard(boardForm, files);
        return "redirect:/board/list";
    }


    @GetMapping("/edit/{boardId}")
    public String showEditForm(@PathVariable("boardId") Long boardId, Model model, HttpSession session) {
        // 1. 로그인 여부 확인 (세션 처리)
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/user/login";
        }

        // 2. 수정할 기존 게시글 데이터 조회
        Board board = this.boardService.readBoardById(boardId);
        
        // 3. 본인 글인지 권한 확인 (작성자와 로그인한 사용자가 같은지 체크)
        if (!board.getWriter().equals(loginUser.getLoginId())) {
            // 본인 글이 아닐 경우 리스트로 리다이렉트하거나 에러 처리
            return "redirect:/board/list";
        }

        // 4. [핵심] 기존에 업로드된 파일 목록 조회
        // BoardFileMapper를 통해 해당 게시글 번호에 종속된 파일들을 가져옵니다.
        List<BoardFile> boardFiles = this.boardFileMapper.findByBoardId(boardId);

        // 5. 화면의 th:object="${boardForm}"에 채워줄 데이터 세팅
        BoardForm boardForm = new BoardForm();
        boardForm.setBoardId(board.getBoardId());
        boardForm.setTitle(board.getTitle());
        boardForm.setContent(board.getContent());
        boardForm.setWriter(board.getName()); // 화면에 표시될 이름
        boardForm.setWriterId(board.getWriter()); // 실제 계정 ID

        // 6. 모델에 데이터 담기
        model.addAttribute("boardForm", boardForm);
        model.addAttribute("boardFiles", boardFiles); // 화면(form.html)에서 반복문으로 출력할 데이터

        return "/board/form";
    }

    // 글수정 POST 수정
    @PostMapping("/edit/{boardId}")
    public String edit(
            @PathVariable("boardId") Long boardId,
            @Valid @ModelAttribute("boardForm") BoardForm boardForm,
            BindingResult bindingResult,
            @RequestParam(value = "files", required = false) List<MultipartFile> files
    ) throws Exception { // <--- 이 부분을 추가하세요!
        
        if (bindingResult.hasErrors()) {
            return "/board/form";
        }

        boardForm.setBoardId(boardId);
        this.boardService.updateBoard(boardForm, files); // 이제 에러가 사라집니다.
        
        return "redirect:/board/detail/" + boardId;
    }

    // 글삭제 POST
    @PostMapping("/delete/{boardId}")
    public String delete(@PathVariable("boardId") Long boardId) {
        this.boardService.deleteBoard(boardId);
        return "redirect:/board/list";
    }

    @PostMapping("/deleteFile/{fileId}")
    @ResponseBody
    public String deleteFile(@PathVariable("fileId") Long fileId) {
        try {
            this.boardService.removeFile(fileId); // 우리가 만든 파일 삭제 로직 호출
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }
}