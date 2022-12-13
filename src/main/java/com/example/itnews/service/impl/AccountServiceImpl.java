package com.example.itnews.service.impl;

import com.example.itnews.dto.sqlmapping.IAccountDTO;
import com.example.itnews.entity.Account;
import com.example.itnews.repository.AccountRepository;
import com.example.itnews.security.exceptions.MRuntimeException;
import com.example.itnews.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final String TAG = "AccountServiceImpl";
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public Account getAccountByUsername(String accountName) {
        return accountRepository.findByAccountName(accountName)
                .orElseThrow(() -> new MRuntimeException("Account not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public Account getAccountById(Integer id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new MRuntimeException("Account not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Boolean isAccountExisted(Account account) {
        return accountRepository.findAccountByAccountName(account.getAccountName()).isPresent() ||
                accountRepository.findAccountByEmail(account.getEmail()).isPresent();
    }

    @Override
    public Boolean isAccountExisted(Integer idAccount) {
        return accountRepository.findById(idAccount).isPresent();
    }

    @Override
    public IAccountDTO getAllInformation(Integer id) {
        return accountRepository.selectId(id)
                .orElseThrow(() -> new MRuntimeException("Account not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public IAccountDTO getAllInformationWithStatus(Integer idAccount, Integer idUser) {
        return accountRepository.selectIdStatus(idAccount, idUser)
                .orElseThrow(() -> new MRuntimeException("Account not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Integer> getListIdAccount() {
        return accountRepository.selectAllId()
                .orElseGet(ArrayList::new)
                .stream().map(IAccountDTO::getIdAccount).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getSearch(String search, Integer page) {
        return accountRepository.getSearch("%" + search + "%", (page - 1) * 10)
                .orElseGet(ArrayList::new)
                .stream().map(IAccountDTO::getIdAccount).collect(Collectors.toList());
    }
}
