package com.example.itnews.repository;

import com.example.itnews.dto.sqlmapping.ITagDTO;
import com.example.itnews.entity.FollowTag;
import com.example.itnews.entity.FollowTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowTagRepository extends JpaRepository<FollowTag, FollowTagId> {

    @Query(nativeQuery = true, value = "select id_tag as idTag, name, logo, total_post as totalPost, total_follower as totalFollower, status from (select T.*,  " +
            "            (select count(*) from post_tag PT, post P where T.id_tag=PT.id_tag and PT.id_post=P.id_post and P.status=1 and P.access=1) total_post, " +
            "            (select count(*) from follow_tag FT where T.id_tag=FT.id_tag) total_follower, " +
            "            (select exists(select * from follow_tag FT where T.id_tag=FT.id_tag and FT.id_account=:idAccountFind)) as status " +
            "            from tag T " +
            "            where (select exists(select * from follow_tag FT where T.id_tag=FT.id_tag and FT.id_account=:idAccountFollowTag))=true ) a")
    Optional<List<ITagDTO>> listStatus(@Param("idAccountFollowTag") Integer idAccountFollowTag, @Param("idAccountFind") Integer idAccountFind);

    @Query(nativeQuery = true, value = "select id_tag as idTag, name, logo, total_post as totalPost, total_follower as totalFollower, status from (select T.*,  " +
            "            (select count(*) from post_tag PT, post P where T.id_tag=PT.id_tag and PT.id_post=P.id_post and P.status=1 and P.access=1) total_post, " +
            "            (select count(*) from follow_tag FT where T.id_tag=FT.id_tag) total_follower, " +
            "            true as status " +
            "            from tag T " +
            "            where (select exists(select * from follow_tag FT where T.id_tag=FT.id_tag and FT.id_account=:idAccount))=true ) a")
    Optional<List<ITagDTO>> list(@Param("idAccount") Integer idAccount);

    void deleteAllByFollowTagId_IdAccount(Integer idAccount);
}