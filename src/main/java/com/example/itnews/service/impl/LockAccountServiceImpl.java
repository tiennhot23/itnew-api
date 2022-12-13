package com.example.itnews.service.impl;

import com.example.itnews.dto.LockAccountDTO;
import com.example.itnews.dto.sqlmapping.ILockAccountDTO;
import com.example.itnews.entity.Account;
import com.example.itnews.entity.LockAccount;
import com.example.itnews.entity.LockAccountId;
import com.example.itnews.payloads.response.MException;
import com.example.itnews.repository.AccountRepository;
import com.example.itnews.repository.LockAccountRepository;
import com.example.itnews.security.exceptions.MRuntimeException;
import com.example.itnews.service.LockAccountService;
import org.springframework.http.HttpStatus;
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
    private final AccountRepository accountRepository;

    public LockAccountServiceImpl(LockAccountRepository lockAccountRepository, AccountRepository accountRepository) {
        this.lockAccountRepository = lockAccountRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Boolean isValidLockAccount(Integer idAccount) {
        LockAccount lockAccount = lockAccountRepository
                .findLockAccountByLockAccountIdIdAccountLock(idAccount)
                .orElse(null);
        Account account = accountRepository.findById(idAccount)
                .orElseThrow(() -> new MRuntimeException("Account not found", HttpStatus.NOT_FOUND));
        if (lockAccount == null || (lockAccount.getTimeEndLock() != null && lockAccount.getTimeEndLock().before(new Date()))) {
            if (lockAccount != null) lockAccountRepository.deleteByLockAccountIdIdAccountLock(idAccount);
            account.setStatus(0);
            accountRepository.save(account);
            return false;
        }
        return true;
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
