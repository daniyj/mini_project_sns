package com.example.sns.repository;

import com.example.sns.entity.Feed;
import com.example.sns.entity.FeedImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedImagesRepository extends JpaRepository<FeedImage,Long> {
    Optional<FeedImage> findTopByFeedId(Long feedId);

   List<FeedImage> findAllByFeedId(Long feedId);

    List<FeedImage> findAllByFeed(Feed feed);
}
