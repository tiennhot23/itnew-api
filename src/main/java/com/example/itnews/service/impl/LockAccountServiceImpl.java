package com.example.itnews.service.impl;

import com.example.itnews.dto.LockAccountDTO;
import com.example.itnews.dto.sqlmapping.ILockAccountDTO;
import com.example.itnews.entity.LockAccount;
import com.example.itnews.entity.LockAccountId;
import com.example.itnews.payloads.response.MException;
import com.example.itnews.repository.LockAccountRepository;
import com.example.itnews.service.LockAccountService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LockAccountServiceImpl implements LockAccountService {

    private final String TAG = "LockAccountServiceImpl";
    private final LockAccountRepository lockAccountRepository;

    public LockAccountServiceImpl(LockAccountRepository lockAccountRepository) {
        this.lockAccountRepository = lockAccountRepository;
    }

    @Override
    public Boolean isValidLockAccount(Integer idAccount) {
        LockAccount lockAccount = lockAccountRepository
                .findLockAccountByLockAccountIdIdAccountLock(idAccount)
                .orElse(null);
        return lockAccount != null && lockAccount.getTimeEndLock().after(new Date());
    }

    @Override
    public LockAccount lock(Integer idAccountLock,
                                   Integer idAccountBoss,
                                   LockAccountDTO lockAccountDTO) {
        return lockAccountRepository.save(
                new LockAccount(new LockAccountId(idAccountLock, idAccountBoss),
                        lockAccountDTO.getReason(),
                        lockAccountDTO.getHoursLock()));
    }

    @Override
    public void unlock(Integer idAccountLock) {
        lockAccountRepository.deleteByLockAccountIdIdAccountLock(idAccountLock);
    }

    @Override
    public List<ILockAccountDTO> selectAll() {
        return lockAccountRepository.selectAll()
                .orElseGet(ArrayList::new);
    }
}
