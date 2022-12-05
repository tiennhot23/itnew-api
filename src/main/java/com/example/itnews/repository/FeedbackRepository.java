package com.example.itnews.repository;

import com.example.itnews.dto.sqlmapping.IFeedbackDTO;
import com.example.itnews.entity.Category;
import com.example.itnews.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    @Query(nativeQuery = true, value = "select id_feedback as idFeedback, id_account as idAccount," +
            "account_name as accountName, real_name as realName, subject, content, " +
            "date_time as dateTime, status, time, day from (SELECT f.*, a.email, a.account_name, a.real_name,  " +
            "            TO_CHAR(date_time, 'hh24:mi') as time,  " +
            "            TO_CHAR(date_time, 'dd/mm/yyyy') as day " +
            "            FROM feedback f, account a " +
            "            WHERE id_feedback = :idFeedback AND f.id_account = a.id_account) a")
    Optional<IFeedbackDTO> selectID(@Param("idFeedback") Integer idFeedback);

    @Query(nativeQuery = true, value = "select id_feedback as idFeedback, id_account as idAccount," +
            "account_name as accountName, real_name as realName, subject, content, " +
            "date_time as dateTime, status, time, day from (SELECT f.*, a.email, a.account_name, a.real_name,  " +
            "            TO_CHAR(date_time, 'hh24:mi') as time,  " +
            "            TO_CHAR(date_time, 'dd/mm/yyyy') as day " +
            "            FROM feedback f, account a " +
            "            WHERE f.id_account = a.id_account ORDER BY f.date_time) a")
    Optional<List<IFeedbackDTO>> selectAll();

    @Query(nativeQuery = true, value = "select id_feedback as idFeedback, id_account as idAccount," +
            "account_name as accountName, real_name as realName, subject, content, " +
            "date_time as dateTime, status, time, day from (SELECT f.*, a.email, a.account_name, a.real_name,  " +
            "            TO_CHAR(date_time, 'hh24:mi') as time,  " +
            "            TO_CHAR(date_time, 'dd/mm/yyyy') as day " +
            "            FROM feedback f, account a " +
            "            WHERE f.id_account = a.id_account AND f.status = 0 ORDER BY f.date_time) a")
    Optional<List<IFeedbackDTO>> selectUnread();
}
