package com.example.itnews.repository;

import com.example.itnews.dto.sqlmapping.IPostDTO;
import com.example.itnews.entity.Bookmark;
import com.example.itnews.entity.BookmarkId;
import com.example.itnews.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {



    @Query(nativeQuery = true, value = "select id_post as idPost, id_account as idAccount, title, content, last_modified as lastModified, created, view, status, access, " +
            "time_created as timeCreated, day_created as dayCreated, time_last_modified as timeLastModified, " +
            "day_last_modified as dayLastModified, bookmark_status as bookmarkStatus, total_comment as totalComment, " +
            "vote_type as voteType, total_vote_down as totalVoteDown, total_vote_up as totalVoteUp, total_bookmark as totalBookmark " +
            "from (SELECT P.*,TO_CHAR(created, 'hh24:mi') as time_created, " +
            "TO_CHAR(created, 'dd/mm/yyyy') as day_created, " +
            "        TO_CHAR(last_modified, 'hh24:mi') as time_last_modified,  " +
            "        TO_CHAR(last_modified, 'dd/mm/yyyy') as day_last_modified,  " +
            "        false as bookmark_status,  " +
            "        (select count(*) from comment C where C.id_post=:id) as total_comment,  " +
            "        null as vote_type,  " +
            "        (select count(*) from vote V where V.id_post=:id and V.type=0) as total_vote_down,  " +
            "        (select count(*) from vote V where V.id_post=:id and V.type=1) as total_vote_up,  " +
            "        (select count(*) from bookmark B where B.id_post=:id) as total_bookmark  " +
            "        FROM post P  " +
            "        WHERE P.id_post=:id) a")
    Optional<IPostDTO> selectId(@Param("id") Integer id);

    @Query(nativeQuery = true, value = "select id_post as idPost, id_account as idAccount, title, content, last_modified as lastModified, created, view, status, access  " +
            "from (SELECT P.* FROM post P " +
            "            INNER JOIN bookmark B " +
            "            ON P.id_post=B.id_post " +
            "            WHERE B.id_account=:idAccount order by B.bookmark_time desc LIMIT 10 OFFSET :offset) a")
    Optional<List<IPostDTO>> getListBookmarkPost(@Param("idAccount") Integer idAccount, @Param("offset") Integer offset);
}
