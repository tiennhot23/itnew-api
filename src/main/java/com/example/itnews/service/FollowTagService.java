package com.example.itnews.service;

import com.example.itnews.dto.sqlmapping.ITagDTO;
import com.example.itnews.entity.FollowTag;
import com.example.itnews.entity.FollowTagId;
import com.example.itnews.entity.Post;

import java.util.List;

public interface FollowTagService {
    List<ITagDTO> getListFollowTag(Integer idAccount);
    List<ITagDTO> getListFollowTag(Integer idAccountFollowTag, Integer idAccountFind);

    Boolean isExist(Integer idAccount, Integer idTag);
    FollowTag get(Integer idAccount, Integer idTag);
    FollowTag save(FollowTag followTag);
    void delete(Integer idAccount, Integer idTag);
    void deleteAll(Integer idAccount);
}
