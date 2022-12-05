package com.example.itnews.repository;

import com.example.itnews.dto.sqlmapping.ICommentDTO;
import com.example.itnews.dto.sqlmapping.ITagDTO;
import com.example.itnews.entity.Comment;
import com.example.itnews.entity.Tag;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query(nativeQuery = true, value = "select id_account as idAccount, real_name as realName, " +
            "account_name as accountName, id_role as idRole, avatar, id_cmt as idCmt, " +
            "id_cmt_parent as idCmtParent, status, content, day, time " +
            "from (select a.id_account, a.real_name, a.account_name, a.id_role,  " +
            "        a.avatar, c.id_cmt, c.content, c.id_cmt_parent, c.status,  " +
            "        TO_CHAR(c.date_time, 'dd/mm/yyyy') AS day,  " +
            "        TO_CHAR(c.date_time, 'hh24:mi') AS time  " +
            "        from comment c, account a   " +
            "        WHERE c.id_account = a.id_account and   " +
            "        id_post = :idPost   " +
            "        ORDER BY id_cmt_parent, date_time) a")
    Optional<List<ICommentDTO>> listCommentInPost(@Param("idPost") Integer idPost);

    @Query(nativeQuery = true, value = "select id_account as idAccount, real_name as realName, " +
            "account_name as accountName, id_role as idRole, avatar, id_cmt as idCmt, " +
            "id_cmt_parent as idCmtParent, status, content, day, time " +
            "from (select a.id_account, a.real_name, a.account_name, a.id_role,  " +
            "        a.avatar, c.id_cmt, c.content, c.id_cmt_parent, c.status,  " +
            "        TO_CHAR(c.date_time, 'dd/mm/yyyy') AS day,  " +
            "        TO_CHAR(c.date_time, 'hh24:mi') AS time  " +
            "        from comment c, account a   " +
            "        WHERE c.id_account = a.id_account and   " +
            "        c.id_cmt = c.id_cmt_parent and  " +
            "        id_post = :idPost   " +
            "        ORDER BY id_cmt_parent, date_time) a")
    Optional<List<ICommentDTO>> listMainCommentInPost(@Param("idPost") Integer idPost);

    @Query(nativeQuery = true, value = "select id_account as idAccount, real_name as realName, " +
            "account_name as accountName, id_role as idRole, avatar, id_cmt as idCmt, " +
            "id_cmt_parent as idCmtParent, status, content, day, time " +
            "from (select a.id_account, a.real_name, a.account_name, a.id_role,  " +
            "        a.avatar, c.id_cmt, c.content, c.id_cmt_parent, c.status,  " +
            "        TO_CHAR(c.date_time, 'dd/mm/yyyy') AS day,  " +
            "        TO_CHAR(c.date_time, 'hh24:mi') AS time  " +
            "        from comment c, account a   " +
            "        WHERE c.id_account = a.id_account and   " +
            "        c.id_cmt_parent = :idCmt  " +
            "        ORDER BY id_cmt_parent, date_time) a")
    Optional<List<ICommentDTO>> listReplyInComment(@Param("idCmt") Integer idCmt);

    @Query(nativeQuery = true, value = "select id_account as idAccount, real_name as realName, " +
            "account_name as accountName, id_role as idRole, avatar, id_cmt as idCmt, " +
            "id_cmt_parent as idCmtParent, status, content, day, time " +
            "from (select a.id_account, a.real_name, a.account_name, a.id_role,  " +
            "        a.avatar, c.id_cmt, c.content, c.id_cmt_parent, c.status,  " +
            "        TO_CHAR(c.date_time, 'dd/mm/yyyy') AS day,  " +
            "        TO_CHAR(c.date_time, 'hh24:mi') AS time  " +
            "        from comment c, account a   " +
            "        WHERE c.id_account = a.id_account and   " +
            "        id_post = :idPost   " +
            "        ORDER BY id_cmt_parent, date_time) a")
    Optional<List<ICommentDTO>> listAllCommentInPost(@Param("idPost") Integer idPost);

    @Query(nativeQuery = true, value = "select id_account as idAccount, real_name as realName, " +
            "account_name as accountName, id_role as idRole, avatar, id_cmt as idCmt, " +
            "id_cmt_parent as idCmtParent, status, content, day, time " +
            "from (select a.id_account, a.real_name, a.account_name, a.id_role,  " +
            "        a.avatar, c.id_cmt, c.content, c.id_cmt_parent, c.status,  " +
            "        TO_CHAR(c.date_time, 'dd/mm/yyyy') AS day,  " +
            "        TO_CHAR(c.date_time, 'hh24:mi') AS time  " +
            "        from comment c, account a   " +
            "        WHERE c.id_account = a.id_account and   " +
            "        c.id_cmt = :idCmt) a")
    Optional<ICommentDTO> selectId(@Param("idCmt") Integer idCmt);
}
