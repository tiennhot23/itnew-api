package com.example.itnews.service;

import com.example.itnews.dto.sqlmapping.ICommentDTO;
import com.example.itnews.dto.sqlmapping.ITagDTO;
import com.example.itnews.entity.Comment;
import com.example.itnews.entity.Tag;

import java.util.List;

public interface CommentService {
    Boolean isExist(Integer idCmt);
    Comment get(Integer idCmt);
    Comment save(Comment comment);
    void delete(Integer idCmt);
    List<ICommentDTO> listCommentInPost(Integer idPost);
    List<ICommentDTO> listMainCommentInPost(Integer idPost);
    List<ICommentDTO> listReplyInComment(Integer idCmt);
    List<ICommentDTO> listAllCommentInPost(Integer idPost);
    ICommentDTO selectId(Integer idCmt);
}
