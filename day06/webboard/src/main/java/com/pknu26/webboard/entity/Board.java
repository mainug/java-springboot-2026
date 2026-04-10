package com.pknu26.webboard.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long bno;  // 테이블 Board의 PK, bno. Long => h2에서 BIGINT

    @Column(length = 250)
    private String title;    // 게시판 제목

    @Column(length = 8000)
    private String content;  // 게시글 내용

    @CreatedDate // 생성일자
    @Column(updatable = false) // 최초 작성시 생성후 수정X
    private LocalDateTime createDate;  // 게시글 작성일
    // createDate 필드명이 JPA로 테이블화되면 create_date로 변경
    @LastModifiedDate // 수정될때마다 날짜 변경
    private LocalDateTime modifyDate;  // 게시글 수정일

}