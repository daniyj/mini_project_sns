package com.example.sns.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="feed_id")
    private Feed feed;

    private String imageUrl;
}
