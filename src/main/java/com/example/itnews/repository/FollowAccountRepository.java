package com.example.itnews.repository;

import com.example.itnews.entity.FollowAccount;
import com.example.itnews.entity.FollowAccountId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowAccountRepository extends JpaRepository<FollowAccount, FollowAccountId> {

    Optional<List<FollowAccount>> findFollowAccountsByFollowAccountId_IdFollowerOrderByFollowTimeDesc(Integer idFollower);
    Optional<List<FollowAccount>> findFollowAccountsByFollowAccountId_IdFollowingOrderByFollowTimeDesc(Integer idFollowing);

    void deleteAllByFollowAccountId_IdFollower(Integer idAccountFollower);
}