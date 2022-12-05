package com.example.itnews.repository;

import com.example.itnews.dto.sqlmapping.IPostDTO;
import com.example.itnews.dto.sqlmapping.IVoteDTO;
import com.example.itnews.entity.Post;
import com.example.itnews.entity.Vote;
import com.example.itnews.entity.VoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, VoteId> {

    @Query(nativeQuery = true, value = "select count(*) as totalVoteUp from vote V, post P " +
            "        where P.id_account=:idAccount and V.id_post=P.id_post and V.type=1")
    Optional<IVoteDTO> getTotalVoteUp(@Param("idAccount") Integer idAccount);

    @Query(nativeQuery = true, value = "select count(*) as totalVoteDown from vote V, post P " +
            "        where P.id_account=:idAccount and V.id_post=P.id_post and V.type=0")
    Optional<IVoteDTO> getTotalVoteDown(@Param("idAccount") Integer idAccount);

    @Query("select v from Vote v where v.voteId.idPost=?1 and v.type=1")
    Optional<List<Vote>> getUpVotes(Integer idPost);

    @Query("select v from Vote v where v.voteId.idPost=?1 and v.type=0")
    Optional<List<Vote>> getDownVotes(Integer idPost);

}