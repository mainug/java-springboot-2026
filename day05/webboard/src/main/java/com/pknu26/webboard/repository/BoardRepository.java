package com.pknu26.webboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pknu26.webboard.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

    

}
