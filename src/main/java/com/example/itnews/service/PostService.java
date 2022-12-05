package com.example.itnews.service;

import com.example.itnews.dto.sqlmapping.IPostDTO;
import com.example.itnews.entity.Post;
import com.example.itnews.entity.Tag;

import java.util.List;

public interface PostService {
    Post getPostById(Integer idPost);
    Post save(Post post);
    Post update(Post post);
    void delete(Integer idPost);
    IPostDTO selectId(Integer idPost);
    IPostDTO selectIdForUser(Integer idPost, Integer idUser);
    Integer getTotalViewOfAccountPosts(Integer idAccount);

    List<Tag> selectTagsOfPost(Integer idPost);
    List<Integer> getListPostIdOfAccount(Integer idAccount, Integer page);
    List<Integer> getListPostIdOfTag(Integer idTag, Integer page);
    List<Integer> getDraftPosts(Integer idAccount, Integer page);
    List<Integer> getPublicPosts(Integer idAccount, Integer page);
    List<Integer> getUnlistedPosts(Integer idAccount, Integer page);
    List<Integer> getPostsForBrowse(Integer page);
    List<Integer> getSpamPost(Integer page);
    List<Integer> getSearch(String search, Integer page);
    List<Integer> getNewestPost(Integer page);
    List<Integer> getTrendingPost(Integer page);
    List<Integer> getFollowing(Integer idAccount, Integer page);
}
