package com.pknu26.studygroup.dto.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiLoginResponse {

    private String tokenType;
    private String accessToken;

    private Long userId;
    private String loginId;
    private String name;
    private String role;
}