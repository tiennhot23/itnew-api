package com.example.itnews.service;

import com.example.itnews.entity.FollowAccount;
import com.example.itnews.entity.FollowTag;
import com.example.itnews.entity.Post;

import java.util.List;

public interface FollowAccountService {
    List<FollowAccount> getListFollowerOfAccount(Integer idAccount);
    List<FollowAccount> getListFollowingOfAccount(Integer idAccount);

    Boolean isExist(Integer idAccountFollower, Integer idAccountFollowing);
    FollowAccount get(Integer idAccountFollower, Integer idAccountFollowing);
    FollowAccount save(FollowAccount followAccount);
    void delete(Integer idAccountFollower, Integer idAccountFollowing);
    void deleteAll(Integer idAccountFollower);
}
