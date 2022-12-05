package com.example.itnews.service;

import com.example.itnews.dto.sqlmapping.IFeedbackDTO;
import com.example.itnews.entity.Category;
import com.example.itnews.entity.Feedback;

import java.util.List;
import java.util.Optional;

public interface FeedbackService {
    Feedback save(Feedback feedback);
    Boolean isExist(Integer idFeedback);
    IFeedbackDTO selectID(Integer idFeedback);
    List<IFeedbackDTO> selectAll();
    List<IFeedbackDTO> selectUnread();
    void readFeedback(Integer idFeedback);
    void unreadFeedback(Integer idFeedback);
    void delete(Integer idFeedback);
}
