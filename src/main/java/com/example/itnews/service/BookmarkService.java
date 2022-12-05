package com.example.itnews.service;

import com.example.itnews.dto.sqlmapping.IPostDTO;
import com.example.itnews.entity.Bookmark;
import com.example.itnews.entity.BookmarkId;
import com.example.itnews.entity.Post;
import com.example.itnews.entity.Tag;

import java.util.List;

public interface BookmarkService {
    List<IPostDTO> getListPostBookmark(Integer idAccount, Integer page);

    Bookmark get(Integer idAccount, Integer idPost);
    Bookmark save(Bookmark bookmark);
    void delete(Integer idAccount, Integer idPost);
    Boolean isExist(Integer idAccount, Integer idPost);
}
