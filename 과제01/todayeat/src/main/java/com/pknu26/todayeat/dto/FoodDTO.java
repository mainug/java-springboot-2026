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
    private LocalDate eatDate;
    private LocalDateTime createdAt;
}