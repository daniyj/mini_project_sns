package com.example.sns.repository;

import com.example.sns.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed,Long> {
    List<Feed> findAllByUserId(Long userId);
}
