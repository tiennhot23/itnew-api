package com.example.itnews.service;

import com.example.itnews.entity.Verification;

public interface VerificationService {
    Verification addVerification(Integer idAccount, String code);
    Verification checkValidVerification(Integer idAccount);
    void deleteVerification(Integer idVerification);
}
