package com.example.itnews.security;

import com.example.itnews.entity.Account;
import com.example.itnews.payloads.response.MException;
import com.example.itnews.repository.AccountRepository;
import com.example.itnews.security.exceptions.MRuntimeException;
import com.example.itnews.service.AccountService;
import com.example.itnews.service.LockAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecureUserService implements UserDetailsService {
    private final String TAG = "SecureUserService";
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private LockAccountService lockAccountService;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Account account = accountRepository.findByAccountName(username)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
        if (lockAccountService.isValidLockAccount(account.getIdAccount())) {
            throw new MRuntimeException("Tài khoản đang bị khoá", HttpStatus.BAD_REQUEST);
        }
        return SecureUserDetails.build(account);
    }


}
