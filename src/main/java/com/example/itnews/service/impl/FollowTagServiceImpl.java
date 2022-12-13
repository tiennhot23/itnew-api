package com.example.itnews.service.impl;

import com.example.itnews.dto.sqlmapping.ITagDTO;
import com.example.itnews.entity.FollowTag;
import com.example.itnews.entity.FollowTagId;
import com.example.itnews.entity.Post;
import com.example.itnews.repository.BookmarkRepository;
import com.example.itnews.repository.FollowTagRepository;
import com.example.itnews.security.exceptions.MRuntimeException;
import com.example.itnews.service.BookmarkService;
import com.example.itnews.service.FollowTagService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FollowTagServiceImpl implements FollowTagService {

    private final String TAG = "FollowTagServiceImpl";
    private final FollowTagRepository followTagRepository;

    public FollowTagServiceImpl(FollowTagRepository followTagRepository) {
        this.followTagRepository = followTagRepository;
    }

    @Override
    public List<ITagDTO> getListFollowTag(Integer idAccount) {
        return followTagRepository.list(idAccount)
                .orElseGet(ArrayList::new);
    }

    @Override
    public List<ITagDTO> getListFollowTag(Integer idAccountFollowTag, Integer idAccountFind) {
        return followTagRepository.listStatus(idAccountFollowTag, idAccountFind)
                .orElseGet(ArrayList::new);
    }

    @Override
    public Boolean isExist(Integer idAccount, Integer idTag) {
        return followTagRepository.findById(new FollowTagId(idAccount, idTag))
                .isPresent();
    }

    @Override
    public FollowTag get(Integer idAccount, Integer idTag) {
        return followTagRepository.findById(new FollowTagId(idAccount, idTag))
                .orElseThrow(() -> new MRuntimeException("Follow tag  not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public FollowTag save(FollowTag followTag) {
        return followTagRepository.save(followTag);
    }

    @Override
    public void delete(Integer idAccount, Integer idTag) {
        followTagRepository.deleteById(new FollowTagId(idAccount, idTag));
    }

    @Override
    public void deleteAll(Integer idAccount) {
        followTagRepository.deleteAllByFollowTagId_IdAccount(idAccount);
    }
}
