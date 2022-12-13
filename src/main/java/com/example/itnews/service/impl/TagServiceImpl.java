package com.example.itnews.service.impl;

import com.example.itnews.dto.sqlmapping.ITagDTO;
import com.example.itnews.entity.Post;
import com.example.itnews.entity.Tag;
import com.example.itnews.repository.BookmarkRepository;
import com.example.itnews.repository.TagRepository;
import com.example.itnews.security.exceptions.MRuntimeException;
import com.example.itnews.service.BookmarkService;
import com.example.itnews.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final String TAG = "TagServiceImpl";
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<ITagDTO> selectAllByAccount(Integer idAccount) {
        return tagRepository.selectAllByAccount(idAccount)
                .orElseGet(ArrayList::new);
    }

    @Override
    public List<ITagDTO> selectAll() {
        return tagRepository.selectAll()
                .orElseGet(ArrayList::new);
    }

    @Override
    public List<ITagDTO> getSearch(String search, Integer page) {
        return tagRepository.getSearch("%" + search + "%", (page - 1) * 10)
                .orElseGet(ArrayList::new);
    }

    @Override
    public Boolean hasId(Integer idTag) {
        return tagRepository.existsById(idTag);
    }

    @Override
    public Boolean hasName(String nameTag) {
        return tagRepository.existsByName(nameTag);
    }

    @Override
    public ITagDTO selectId(Integer idTag) {
        return tagRepository.selectId(idTag)
                .orElseThrow(() -> new MRuntimeException("Tag  not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public ITagDTO selectIdByAccount(Integer idTag, Integer idAccount) {
        return tagRepository.selectIdByAccount(idTag, idAccount)
                .orElseThrow(() -> new MRuntimeException("Tag  not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public Tag get(Integer idTag) {
        return tagRepository.findById(idTag)
                .orElseThrow(() -> new MRuntimeException("Tag  not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public Tag get(String nameTag) {
        return tagRepository.findTagByName(nameTag)
                .orElseThrow(() -> new MRuntimeException("Tag  not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public Tag add(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag update(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public void delete(Integer idTag) {
        tagRepository.deleteById(idTag);
    }

    @Override
    public Integer countPostsOfTag(Integer idTag) {
        return tagRepository.countPostsOfTag(idTag)
                .orElseThrow(() -> new MRuntimeException("Tag  not found", HttpStatus.NOT_FOUND))
                .getTotalPost();
    }
}
