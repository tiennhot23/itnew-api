package com.example.itnews.service;

import com.example.itnews.entity.Post;
import com.example.itnews.entity.Vote;

import java.util.List;

public interface VoteService {
    Integer getTotalVoteUp(Integer idAccount);
    Integer getTotalVoteDown(Integer idAccount);
    List<Vote> getUpVotes(Integer idPost);
    List<Vote> getDownVotes(Integer idPost);
    Boolean isExist(Integer idAccount, Integer idPost);

    Vote getVote(Integer idAccount, Integer idPost);
    Vote save(Vote vote);
    void delete(Integer idAccount, Integer idPost);
}
