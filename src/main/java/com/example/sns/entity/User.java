package com.example.sns.entity;

import com.example.sns.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username; // loginId

    @Column(nullable = false)
    private String password;

    private String email;
    private String phone;

    private String profile_img;

    @OneToMany(fetch = LAZY, mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(fetch = LAZY, mappedBy = "user")
    private List<Article> articles;

    @OneToMany(fetch = LAZY, mappedBy = "user")
    private List<LikeArticle> likeArticles; // 유저가 누른 좋아요

    // follow, friends 추후 다시
//    @ManyToMany
//    private List<UserFollows> follows;
//
//    @ManyToMany
//    private List<UserFriends> friends;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}
