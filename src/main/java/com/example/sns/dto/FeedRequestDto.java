package com.example.sns.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FeedRequestDto {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    @NotBlank(message = "내용은 필수입니다.")
    private String content;
    // 추후 확인 필요
//    private List<String> ImageUrl;
}
