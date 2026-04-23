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
    public String list(@RequestParam(defaultValue = "eatDate") String orderBy, 
                    Model model) {
        // 1. 매퍼를 통해 정렬된 리스트를 가져옵니다.
        List<FoodDTO> foods = foodMapper.selectAll(orderBy);
        
        // 2. 뷰(Thymeleaf)로 데이터를 전달합니다.
        model.addAttribute("foods", foods);
        model.addAttribute("currentOrder", orderBy);
        
        return "food/list"; // templates/food/list.html
    }

    // 음식 정보 등록 실행
    @PostMapping("/food/add")
    public String addFood(@ModelAttribute FoodDTO foodDTO) {
        foodMapper.insertFood(foodDTO);
        return "redirect:/food/list";
    }

    @GetMapping("/food/get/{id}")
    @ResponseBody // 페이지 이동이 아니라 데이터만 JSON으로 보냄
    public FoodDTO getFood(@PathVariable Long id) {
        return foodMapper.selectFoodById(id);
    }

    // 수정한 데이터를 저장
    @PostMapping("/food/update/{id}")
    public String updateFood(@PathVariable("id") Long id, @ModelAttribute FoodDTO foodDTO) {
        foodDTO.setId(id); // 경로에서 받은 ID를 DTO에 세팅
        foodMapper.updateFood(foodDTO);
        return "redirect:/food/list";
    }

    @GetMapping("/food/delete/{id}")
    public String deleteFood(@PathVariable("id") Long id) {
        foodMapper.deleteFood(id);
        return "redirect:/food/list";
    }

}