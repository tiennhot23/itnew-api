package com.example.itnews.service;

import com.example.itnews.dto.LockAccountDTO;
import com.example.itnews.dto.sqlmapping.ILockAccountDTO;
import com.example.itnews.entity.LockAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface LockAccountService {
    Boolean isValidLockAccount(Integer idAccount);
    LockAccount lock(Integer idAccountLock, Integer idAccountBoss, LockAccountDTO lockAccountDTO);
    void unlock(Integer idAccountLock);

    List<ILockAccountDTO> selectAll();
}
