package com.example.itnews.service.impl;

import com.example.itnews.dto.sqlmapping.ICommentDTO;
import com.example.itnews.dto.sqlmapping.ITagDTO;
import com.example.itnews.entity.Comment;
import com.example.itnews.entity.Tag;
import com.example.itnews.repository.CommentRepository;
import com.example.itnews.repository.TagRepository;
import com.example.itnews.security.exceptions.MRuntimeException;
import com.example.itnews.service.CommentService;
import com.example.itnews.service.TagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final String TAG = "CommentServiceImpl";
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Boolean isExist(Integer idCmt) {
        return commentRepository.findById(idCmt).isPresent();
    }

    @Override
    public Comment get(Integer idCmt) {
        return commentRepository.findById(idCmt)
                .orElseThrow(() -> new MRuntimeException(TAG + ": Comment not found"));
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void delete(Integer idCmt) {
        commentRepository.deleteById(idCmt);
    }

    @Override
    public List<ICommentDTO> listCommentInPost(Integer idPost) {
        return commentRepository.listCommentInPost(idPost)
                .orElseGet(ArrayList::new);
    }

    @Override
    public List<ICommentDTO> listMainCommentInPost(Integer idPost) {
        return commentRepository.listMainCommentInPost(idPost)
                .orElseGet(ArrayList::new);
    }

    @Override
    public List<ICommentDTO> listReplyInComment(Integer idCmt) {
        return commentRepository.listReplyInComment(idCmt)
                .orElseGet(ArrayList::new);
    }

    @Override
    public List<ICommentDTO> listAllCommentInPost(Integer idPost) {
        return commentRepository.listAllCommentInPost(idPost)
                .orElseGet(ArrayList::new);
    }

    @Override
    public ICommentDTO selectId(Integer idCmt) {
        return commentRepository.selectId(idCmt)
                .orElseThrow(() -> new MRuntimeException(TAG + ": Comment not found"));
    }
}
