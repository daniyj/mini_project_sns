package com.example.sns.repository;

import com.example.sns.entity.FeedImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedImagesRepository extends JpaRepository<FeedImages,Long> {
}
