package com.example.sns.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String content;
    private Boolean draft;
    private LocalDateTime deletedAt;

    @OneToMany(fetch = LAZY, mappedBy = "article")
    private List<ArticleImages> articleImages = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "article")
    private List<Comment> comments = new ArrayList<>();

//    @OneToMany(mappedBy = "article")
//    private List<LikeArticle> likeArticles;

}
