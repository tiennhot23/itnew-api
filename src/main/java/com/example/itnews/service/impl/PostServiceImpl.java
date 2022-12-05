package com.example.itnews.service.impl;

import com.example.itnews.dto.sqlmapping.IAccountDTO;
import com.example.itnews.dto.sqlmapping.IPostDTO;
import com.example.itnews.entity.Account;
import com.example.itnews.entity.Post;
import com.example.itnews.entity.Tag;
import com.example.itnews.repository.AccountRepository;
import com.example.itnews.repository.PostRepository;
import com.example.itnews.security.exceptions.MRuntimeException;
import com.example.itnews.service.PostService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final String TAG = "PostServiceImpl";
    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post getPostById(Integer idPost) {
        return postRepository.findById(idPost)
                .orElseThrow(() -> new MRuntimeException(TAG + ": PostNotFound"));
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post update(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void delete(Integer idPost) {
        postRepository.deleteById(idPost);
    }

    @Override
    public IPostDTO selectId(Integer idPost) {
        return postRepository.selectId(idPost)
                .orElseThrow(() -> new MRuntimeException(TAG + ": PostNotFound"));
    }

    @Override
    public IPostDTO selectIdForUser(Integer idPost, Integer idUser) {
        return postRepository.selectIdForUser(idPost, idUser)
                .orElseThrow(() -> new MRuntimeException(TAG + ": PostNotFound"));
    }

    @Override
    public Integer getTotalViewOfAccountPosts(Integer idAccount) {
        return postRepository.getTotalView(idAccount).orElse(0);
    }

    @Override
    public List<Tag> selectTagsOfPost(Integer idPost) {
        return postRepository.findById(idPost)
                .orElseThrow(() -> new MRuntimeException(TAG + ": Post not found"))
                .getTags();
    }

    @Override
    public List<Integer> getListPostIdOfAccount(Integer idAccount, Integer page) {
        return postRepository.getListPostIdOfAccount(idAccount, (page - 1) * 10)
                .orElseGet(ArrayList::new)
                .stream().map(IPostDTO::getIdPost).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getListPostIdOfTag(Integer idTag, Integer page) {
        return postRepository.getListPostIdOfTag(idTag, (page - 1) * 10)
                .orElseGet(ArrayList::new)
                .stream().map(IPostDTO::getIdPost).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getDraftPosts(Integer idAccount, Integer page) {
        return postRepository.getDraftPosts(idAccount, (page - 1) * 10)
                .orElseGet(ArrayList::new)
                .stream().map(IPostDTO::getIdPost).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getPublicPosts(Integer idAccount, Integer page) {
        return postRepository.getPublicPosts(idAccount, (page - 1) * 10)
                .orElseGet(ArrayList::new)
                .stream().map(IPostDTO::getIdPost).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getUnlistedPosts(Integer idAccount, Integer page) {
        return postRepository.getUnlistedPosts(idAccount, (page - 1) * 10)
                .orElseGet(ArrayList::new)
                .stream().map(IPostDTO::getIdPost).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getPostsForBrowse(Integer page) {
        return postRepository.getPostsForBrowse((page - 1) * 10)
                .orElseGet(ArrayList::new)
                .stream().map(IPostDTO::getIdPost).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getSpamPost(Integer page) {
        return postRepository.getSpamPost((page - 1) * 10)
                .orElseGet(ArrayList::new)
                .stream().map(IPostDTO::getIdPost).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getSearch(String search, Integer page) {
        return postRepository.getSearchPost(search, (page - 1) * 10)
                .orElseGet(ArrayList::new)
                .stream().map(IPostDTO::getIdPost).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getNewestPost(Integer page) {
        return postRepository.getNewestPost((page - 1) * 10)
                .orElseGet(ArrayList::new)
                .stream().map(IPostDTO::getIdPost).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getTrendingPost(Integer page) {
        return postRepository.getTrending((page - 1) * 10)
                .orElseGet(ArrayList::new)
                .stream().map(IPostDTO::getIdPost).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getFollowing(Integer idAccount, Integer page) {
        return postRepository.getFollowing(idAccount, (page - 1) * 10)
                .orElseGet(ArrayList::new)
                .stream().map(IPostDTO::getIdPost).collect(Collectors.toList());
    }
}
