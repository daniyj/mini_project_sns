package com.example.sns.dto;

import com.example.sns.entity.Comment;
import lombok.Data;

import java.util.List;

@Data
public class FeedInfoDto {
    private String title;
    private String content;
    private List<String> feedImageUrls;
    private List<CommentDto> comments;
    private Long likesCount;
}
