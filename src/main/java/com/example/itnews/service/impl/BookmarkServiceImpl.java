package com.example.itnews.service.impl;

import com.example.itnews.dto.sqlmapping.IPostDTO;
import com.example.itnews.entity.Bookmark;
import com.example.itnews.entity.BookmarkId;
import com.example.itnews.entity.Post;
import com.example.itnews.entity.Tag;
import com.example.itnews.repository.BookmarkRepository;
import com.example.itnews.repository.PostRepository;
import com.example.itnews.security.exceptions.MRuntimeException;
import com.example.itnews.service.BookmarkService;
import com.example.itnews.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    private final String TAG = "BookmarkServiceImpl";
    private final BookmarkRepository bookmarkRepository;

    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    @Override
    public List<IPostDTO> getListPostBookmark(Integer idAccount, Integer page) {
        List<IPostDTO> p = bookmarkRepository.getListBookmarkPost(idAccount, (page - 1) * 10)
                .orElseGet(ArrayList::new);
        return p;
    }

    @Override
    public Bookmark get(Integer idAccount, Integer idPost) {
        return bookmarkRepository.findById(new BookmarkId(idAccount, idPost))
                .orElseThrow(() -> new MRuntimeException("Bookmark  not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public Bookmark save(Bookmark bookmark) {
        return bookmarkRepository.save(bookmark);
    }

    @Override
    public void delete(Integer idAccount, Integer idPost) {
        bookmarkRepository.deleteById(new BookmarkId(idAccount, idPost));
    }

    @Override
    public Boolean isExist(Integer idAccount, Integer idPost) {
        return bookmarkRepository.findById(new BookmarkId(idAccount, idPost))
                .isPresent();
    }
}
