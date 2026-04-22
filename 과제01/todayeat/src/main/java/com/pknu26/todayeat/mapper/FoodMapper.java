package com.pknu26.todayeat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pknu26.todayeat.dto.FoodDTO;

@Mapper
public interface FoodMapper {

    void insertFood(FoodDTO food);

    List<FoodDTO> selectAll(String orderBy);
    
}