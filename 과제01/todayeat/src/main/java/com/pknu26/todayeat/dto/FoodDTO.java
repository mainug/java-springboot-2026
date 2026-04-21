package com.pknu26.todayeat.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class FoodDTO {
    private Long id;
    private String name;
    private String category;
    private Integer rating;
    private String memo;
    private LocalDate eatDate; // 음식 먹은 날짜
    private LocalDateTime createdAt; // 작성 일자
}