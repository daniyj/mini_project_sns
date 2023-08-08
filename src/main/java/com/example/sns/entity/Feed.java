package com.example.sns.entity;

import com.example.sns.dto.FeedRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String content;
    private Boolean draft; // true:초안, false:작성완료
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "feed")
    private List<FeedImage> feedImages = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "feed")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "feed")
    private List<LikeFeed> likeFeeds = new ArrayList<>();

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
