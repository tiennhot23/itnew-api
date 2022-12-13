package com.example.itnews.service.impl;

import com.example.itnews.config.AppConstants;
import com.example.itnews.entity.Account;
import com.example.itnews.entity.Verification;
import com.example.itnews.payloads.response.MException;
import com.example.itnews.payloads.response.MResponse;
import com.example.itnews.repository.AccountRepository;
import com.example.itnews.repository.VerificationRepository;
import com.example.itnews.security.exceptions.MRuntimeException;
import com.example.itnews.service.VerificationService;
import com.example.itnews.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class VerificationServiceImpl implements VerificationService {

    private static final String TAG = "VerificationServiceImpl";
    @Autowired
    private VerificationRepository verificationRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Verification addVerification(Integer idAccount, String code) {
        Account account = accountRepository.findById(idAccount)
                .orElseThrow(() -> new MRuntimeException("Account  not found", HttpStatus.NOT_FOUND));
        return verificationRepository.save(
                Verification.builder().account(account)
                    .code(code)
                    .createTime(new Date())
                    .endTime(Utils.addMinutesToJavaUtilDate(new Date(), AppConstants.codeConfirmMinutes))
                    .build()
        );
    }

    @Override
    public Verification checkValidVerification(Integer idAccount) {
        Verification verification = verificationRepository.findByIdAccount(idAccount)
                .orElseThrow(() -> new MRuntimeException("Verification  not found", HttpStatus.NOT_FOUND));
        if (verification.getEndTime().before(new Date())) {
            verificationRepository.delete(verification);
            throw new MRuntimeException("VerificationInvalid: Mã xác nhận không hợp lệ hoặc đã hết hạn", HttpStatus.BAD_REQUEST);
        }
        return verification;
    }

    @Override
    public void deleteVerification(Integer idVerification) {
        verificationRepository.deleteById(idVerification);
    }
}
