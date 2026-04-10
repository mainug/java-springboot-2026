package com.pknu26.webboard.validation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardForm {

    @Size(max = 250)
    @NotBlank(message = "제목을 작성하세요")
    private String title;

    @Size(max = 8000)
    @NotBlank(message = "내용을 작성하세요")
    private String content;

}