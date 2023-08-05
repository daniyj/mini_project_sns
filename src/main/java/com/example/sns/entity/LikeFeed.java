package com.example.sns.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
// 진행중
public class LikeFeed { // 게시글의 좋아요
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="feed_id")
    private Feed feed; // 좋아요가 추가된 게시글

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user; // 좋아요를 누른 유저
}
