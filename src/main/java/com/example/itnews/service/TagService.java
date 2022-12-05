package com.example.itnews.service;

import com.example.itnews.dto.sqlmapping.ITagDTO;
import com.example.itnews.entity.Post;
import com.example.itnews.entity.Tag;

import java.util.List;

public interface TagService {
    List<ITagDTO> selectAllByAccount(Integer idAccount);
    List<ITagDTO> selectAll();
    List<ITagDTO> getSearch(String search, Integer page);
    Boolean hasId(Integer idTag);
    Boolean hasName(String nameTag);
    ITagDTO selectId(Integer idTag);
    ITagDTO selectIdByAccount(Integer idTag, Integer idAccount);
    Tag get(Integer idTag);
    Tag get(String nameTag);
    Tag add(Tag tag);
    Tag update(Tag tag);
    void delete(Integer idTag);
    Integer countPostsOfTag(Integer idTag);
}
