package com.example.itnews.service;


import com.example.itnews.dto.sqlmapping.IAccountDTO;
import com.example.itnews.entity.Account;

import java.util.List;

public interface AccountService {
    Account getAccountByUsername(String accountName);
    Account getAccountById(Integer id);
    Account saveAccount(Account account);
    Account updateAccount(Account account);
    Boolean isAccountExisted(Account account);
    Boolean isAccountExisted(Integer idAccount);

    IAccountDTO getAllInformation(Integer id);
    IAccountDTO getAllInformationWithStatus(Integer idAccount, Integer idUser);

    List<Integer> getListIdAccount();
    List<Integer> getSearch(String search, Integer page);
}
