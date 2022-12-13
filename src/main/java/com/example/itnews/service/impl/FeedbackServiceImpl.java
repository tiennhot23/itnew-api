package com.example.itnews.service.impl;

import com.example.itnews.dto.sqlmapping.IFeedbackDTO;
import com.example.itnews.entity.Feedback;
import com.example.itnews.repository.FeedbackRepository;
import com.example.itnews.security.exceptions.MRuntimeException;
import com.example.itnews.service.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final String TAG = "FeedbackServiceImpl";
    private final FeedbackRepository feedbackRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public Feedback save(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public Boolean isExist(Integer idFeedback) {
        return feedbackRepository.findById(idFeedback).isPresent();
    }

    @Override
    public IFeedbackDTO selectID(Integer idFeedback) {
        return feedbackRepository.selectID(idFeedback)
                .orElseThrow(() -> new MRuntimeException("feedback  not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<IFeedbackDTO> selectAll() {
        return feedbackRepository.selectAll().orElseGet(ArrayList::new);
    }

    @Override
    public List<IFeedbackDTO> selectUnread() {
        return feedbackRepository.selectUnread().orElseGet(ArrayList::new);
    }

    @Override
    public void readFeedback(Integer idFeedback) {
        Feedback feedback = feedbackRepository.findById(idFeedback)
                .orElseThrow(() -> new MRuntimeException("feedback  not found", HttpStatus.NOT_FOUND));
        feedback.setStatus(1);
        feedbackRepository.save(feedback);
    }

    @Override
    public void unreadFeedback(Integer idFeedback) {
        Feedback feedback = feedbackRepository.findById(idFeedback)
                .orElseThrow(() -> new MRuntimeException("feedback  not found", HttpStatus.NOT_FOUND));
        feedback.setStatus(0);
        feedbackRepository.save(feedback);
    }

    @Override
    public void delete(Integer idFeedback) {
        feedbackRepository.deleteById(idFeedback);
    }
}
