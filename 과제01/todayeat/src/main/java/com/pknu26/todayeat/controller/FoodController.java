package com.pknu26.todayeat.controller;

import com.pknu26.todayeat.dto.FoodDTO;
import com.pknu26.todayeat.mapper.FoodMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FoodController {

    private final FoodMapper foodMapper;

    // 랜딩 페이지 -> 목록으로 이동
    @GetMapping("/")
    public String home() {
        return "redirect:/food/list";
    }

    // 음식 목록 조회
    @GetMapping("/food/list")
    public String list(Model model) {
        List<FoodDTO> foods = foodMapper.selectAllFoods();
        model.addAttribute("foods", foods);
        return "food/list";
    }

    // 등록 페이지 이동
    @GetMapping("/food/add")
    public String addForm() {
        return "food/add";
    }

    // 음식 정보 등록 실행
    @PostMapping("/food/add")
    public String addFood(@ModelAttribute FoodDTO foodDTO) {
        foodMapper.insertFood(foodDTO);
        return "redirect:/food/list";
    }
}