package com.example.itnews.service.impl;

import com.example.itnews.dto.sqlmapping.ITagDTO;
import com.example.itnews.entity.FollowAccount;
import com.example.itnews.entity.FollowAccountId;
import com.example.itnews.entity.FollowTag;
import com.example.itnews.entity.FollowTagId;
import com.example.itnews.repository.FollowAccountRepository;
import com.example.itnews.repository.FollowTagRepository;
import com.example.itnews.security.exceptions.MRuntimeException;
import com.example.itnews.service.FollowAccountService;
import com.example.itnews.service.FollowTagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FollowAccountServiceImpl implements FollowAccountService {

    private final String TAG = "FollowAccountServiceImpl";
    private final FollowAccountRepository followAccountRepository;

    public FollowAccountServiceImpl(FollowAccountRepository followAccountRepository) {
        this.followAccountRepository = followAccountRepository;
    }

    @Override
    public List<FollowAccount> getListFollowerOfAccount(Integer idAccount) {
        return followAccountRepository.findFollowAccountsByFollowAccountId_IdFollowerOrderByFollowTimeDesc(idAccount)
                .orElseGet(ArrayList::new);
    }

    @Override
    public List<FollowAccount> getListFollowingOfAccount(Integer idAccount) {
        return followAccountRepository.findFollowAccountsByFollowAccountId_IdFollowingOrderByFollowTimeDesc(idAccount)
                .orElseGet(ArrayList::new);
    }

    @Override
    public Boolean isExist(Integer idAccountFollower, Integer idAccountFollowing) {
        return followAccountRepository.findById(new FollowAccountId(idAccountFollower, idAccountFollowing))
                .isPresent();
    }

    @Override
    public FollowAccount get(Integer idAccountFollower, Integer idAccountFollowing) {
        return followAccountRepository.findById(new FollowAccountId(idAccountFollower, idAccountFollowing))
                .orElseThrow(() -> new MRuntimeException(TAG + ": Follow account not found"));
    }

    @Override
    public FollowAccount save(FollowAccount followAccount) {
        return followAccountRepository.save(followAccount);
    }

    @Override
    public void delete(Integer idAccountFollower, Integer idAccountFollowing) {
        followAccountRepository.deleteById(new FollowAccountId(idAccountFollower, idAccountFollowing));
    }

    @Override
    public void deleteAll(Integer idAccountFollower) {
        followAccountRepository.deleteAllByFollowAccountId_IdFollower(idAccountFollower);
    }
}
