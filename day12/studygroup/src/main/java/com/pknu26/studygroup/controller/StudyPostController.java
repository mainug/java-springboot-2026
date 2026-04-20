package com.pknu26.studygroup.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pknu26.studygroup.dto.LoginUser;
import com.pknu26.studygroup.dto.StudyPost;
import com.pknu26.studygroup.service.CategoryService;
import com.pknu26.studygroup.service.StudyPostService;
import com.pknu26.studygroup.validation.StudyPostForm;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/studypost")
@RequiredArgsConstructor
public class StudyPostController {

    private final StudyPostService studyPostService;

    private final CategoryService categoryService;

    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/user/login"; // 로그인안한 사람은 게시판 글 못씀
        }

        // TODO : 로그인한 사용자면 볼수 있도록 변경
        List<StudyPost> postList = this.studyPostService.getPostList();

        model.addAttribute("postList", postList);
        return "/post/list";  // templates/post/list.html 
    }
    
    @GetMapping("/create")
    public String createForm(Model model, HttpSession session) {
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/user/login"; // 로그인안한 사람은 게시판 글 못씀
        }

        StudyPostForm studyPostForm = new StudyPostForm();

        model.addAttribute("studyPostForm", studyPostForm);
        // 콤보 박스용
        model.addAttribute("categoryList", this.categoryService.getCategoryList());

        return "/post/form"; // templates/post/form.html
    }

    @PostMapping("/create")
    public String create(@Valid StudyPostForm studyPostForm, BindingResult bindingResult, HttpSession session, Model model) {
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/user/login"; // 로그인안한 사람은 게시판 글 못씀
        }

        if (bindingResult.hasErrors()) {
            // 콤보 박스용
            model.addAttribute("categoryList", this.categoryService.getCategoryList());
            return "/post/form";
        }

        studyPostForm.setUserId(loginUser.getUserId()); // 세션에 로그인 한 사용자 아이디       
        
        studyPostService.createPost(studyPostForm);
        return "redirect:/studypost/list";
    }

    @GetMapping("/detail/{postId}")
    public String detail(@PathVariable Long postId, Model model, HttpSession session) {
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/user/login";
        }

        StudyPost post = this.studyPostService.getPostDetail(postId);

        if (post == null) {
            return "redirect:/studypost/list";
        }

        model.addAttribute("post", post);
        return "/post/detail";   // templates/post/detail.html
    }

    @PostMapping("/delete/{postId}")
    public String delete(@PathVariable Long postId,
                        HttpSession session) {
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/user/login";
        }

        StudyPost post = this.studyPostService.getPostDetail(postId);

        if (post == null) {
            return "redirect:/studypost/list";
        }

        // 작성자만 삭제 가능
        if (!post.getUserId().equals(loginUser.getUserId())) {
            return "redirect:/studypost/detail/" + postId;
        }

        this.studyPostService.deletePost(postId);

        return "redirect:/studypost/list";
    }

    @GetMapping("/edit/{postId}")
    public String editForm(@PathVariable Long postId,
                        Model model,
                        HttpSession session) {
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/user/login";
        }

        StudyPost post = this.studyPostService.getPostDetail(postId);

        if (post == null) {
            return "redirect:/studypost/list";
        }

        // 작성자만 수정 가능
        if (!post.getUserId().equals(loginUser.getUserId())) {
            return "redirect:/studypost/detail/" + postId;
        }

        StudyPostForm studyPostForm = new StudyPostForm();
        studyPostForm.setPostId(post.getPostId());
        studyPostForm.setCategoryId(post.getCategoryId());
        studyPostForm.setTitle(post.getTitle());
        studyPostForm.setMaxMembers(post.getMaxMembers());
        studyPostForm.setContent(post.getContent());

        model.addAttribute("studyPostForm", studyPostForm);
        model.addAttribute("categoryList", this.categoryService.getCategoryList());

        return "/post/form";
    }

    @PostMapping("/edit/{postId}")
    public String edit(@PathVariable Long postId,
                    @Valid StudyPostForm studyPostForm,
                    BindingResult bindingResult,
                    Model model,
                    HttpSession session) {
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/user/login";
        }

        StudyPost post = this.studyPostService.getPostDetail(postId);

        if (post == null) {
            return "redirect:/studypost/list";
        }

        // 작성자만 수정 가능
        if (!post.getUserId().equals(loginUser.getUserId())) {
            return "redirect:/studypost/detail/" + postId;
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryList", this.categoryService.getCategoryList());
            return "/post/form";
        }

        studyPostForm.setPostId(postId);
        studyPostForm.setUserId(loginUser.getUserId());

        this.studyPostService.updatePost(studyPostForm);

        return "redirect:/studypost/detail/" + postId;
    }    

}
