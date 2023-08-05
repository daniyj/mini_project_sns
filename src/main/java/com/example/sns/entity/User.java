package com.example.sns.entity;

import com.example.sns.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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
    @Setter
    private String profileImgUrl;

    @OneToMany(fetch = LAZY, mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "user")
    private List<Feed> feeds = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "user")
    private List<LikeFeed> likeFeeds = new ArrayList<>(); // 유저가 누른 좋아요

    // follow, friends 추후 다시
//    @ManyToMany
//    private List<UserFollows> follows;
//
//    @ManyToMany
//    private List<UserFriends> friends;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}
