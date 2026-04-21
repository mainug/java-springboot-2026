package com.pknu26.todayeat.controller;

import com.pknu26.todayeat.dto.FoodDTO;
import com.pknu26.todayeat.mapper.FoodMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodMapper foodMapper;

    // 음식 목록 조회
    @GetMapping("/list")
    public String list(Model model) {
        List<FoodDTO> foods = foodMapper.selectAllFoods();
        model.addAttribute("foods", foods);
        return "food/list";
    }

    // 등록 페이지 이동
    @GetMapping("/add")
    public String addForm() {
        return "food/add";
    }

    // 음식 정보 등록 실행
    @PostMapping("/add")
    public String addFood(@ModelAttribute FoodDTO foodDTO) {
        foodMapper.insertFood(foodDTO);
        return "redirect:/food/list";
    }
}
