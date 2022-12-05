package com.example.itnews.repository;

import com.example.itnews.dto.sqlmapping.IPostDTO;
import com.example.itnews.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

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

    @Query(nativeQuery = true, value = "select id_post as idPost, id_account as idAccount, title, content, last_modified as lastModified, created, view, status, access, " +
            "time_created as timeCreated, day_created as dayCreated, time_last_modified as timeLastModified, " +
            "day_last_modified as dayLastModified, bookmark_status as bookmarkStatus, total_comment as totalComment, " +
            "vote_type as voteType, total_vote_down as totalVoteDown, total_vote_up as totalVoteUp, total_bookmark as totalBookmark " +
            "from (SELECT P.*, " +
            "        TO_CHAR(created, 'hh24:mi') as time_created, " +
            "        TO_CHAR(created, 'dd/mm/yyyy') as day_created, " +
            "        TO_CHAR(last_modified, 'hh24:mi') as time_last_modified, " +
            "        TO_CHAR(last_modified, 'dd/mm/yyyy') as day_last_modified, " +
            "        (select exists(select * from bookmark where id_post=:idPost and id_account=:idUser)) as bookmark_status, " +
            "        (select count(*) from comment C where C.id_post=:idPost) as total_comment, " +
            "        (select type from vote where id_post=:idPost and id_account=:idUser) as vote_type, " +
            "        (select count(*) from vote V where V.id_post=:idPost and V.type=0) as total_vote_down, " +
            "        (select count(*) from vote V where V.id_post=:idPost and V.type=1) as total_vote_up, " +
            "        (select count(*) from bookmark B where B.id_post=:idPost) as total_bookmark " +
            "        FROM post P " +
            "        WHERE P.id_post=:idPost) a")
    Optional<IPostDTO> selectIdForUser(@Param("idPost") Integer idPost, @Param("idUser") Integer idUser);

    @Query(nativeQuery = true, value = "SELECT id_post as idPost FROM post WHERE id_account=:idAccount AND status=1 AND access=1 " +
            "ORDER BY idPost DESC LIMIT 10 OFFSET :offset")
    Optional<List<IPostDTO>> getListPostIdOfAccount(@Param("idAccount") Integer idAccount, @Param("offset") Integer offset);

    @Query(nativeQuery = true, value = "SELECT id_post as idPost FROM post WHERE id_account=:idAccount AND access=1 " +
            "ORDER BY idPost DESC LIMIT 10 OFFSET :offset")
    Optional<List<IPostDTO>> getPublicPosts(@Param("idAccount") Integer idAccount, @Param("offset") Integer offset);

    @Query(nativeQuery = true, value = "SELECT id_post as idPost FROM post WHERE id_account=:idAccount AND access=2 " +
            "ORDER BY idPost DESC LIMIT 10 OFFSET :offset")
    Optional<List<IPostDTO>> getUnlistedPosts(@Param("idAccount") Integer idAccount, @Param("offset") Integer offset);

    @Query(nativeQuery = true, value = "SELECT id_post as idPost FROM post WHERE status=0 AND access=1 " +
            "ORDER BY created DESC LIMIT 10 OFFSET :offset")
    Optional<List<IPostDTO>> getPostsForBrowse(@Param("offset") Integer offset);

    @Query(nativeQuery = true, value = "SELECT id_post as idPost FROM post WHERE status=2 AND access=1 " +
            "ORDER BY created DESC LIMIT 10 OFFSET :offset")
    Optional<List<IPostDTO>> getSpamPost(@Param("offset") Integer offset);

    @Query(nativeQuery = true, value = "SELECT id_post as idPost FROM post WHERE status=1 AND access=1 " +
            "ORDER BY created DESC LIMIT 10 OFFSET :offset")
    Optional<List<IPostDTO>> getNewestPost(@Param("offset") Integer offset);


    @Query(nativeQuery = true, value = "select id_post as idPost from (SELECT p.id_post, count(p.id_post) as rating FROM post p, vote v " +
            "where p.id_post=v.id_post group by p.id_post order by rating desc " +
            "LIMIT 10 OFFSET :offset) a")
    Optional<List<IPostDTO>> getTrending(@Param("offset") Integer offset);

    @Query("select sum(p.view) from Post p where p.account.idAccount=?1")
    Optional<Integer> getTotalView(Integer idAccount);


    @Query(nativeQuery = true, value = "SELECT P.id_post as idPost " +
            "            FROM post p " +
            "            JOIN post_tag PT ON P.id_post = PT.id_post " +
            "            WHERE PT.id_tag=:idTag AND P.access=1 AND P.status=1 " +
            "            ORDER BY P.created DESC " +
            "            LIMIT 10 OFFSET :offset")
    Optional<List<IPostDTO>> getListPostIdOfTag(@Param("idTag") Integer idTag, @Param("offset") Integer offset);

    @Query(nativeQuery = true, value = "SELECT id_post as idPost FROM post WHERE id_account=:idAccount AND access=0 ORDER BY idPost DESC  LIMIT 10 OFFSET :offset ")
    Optional<List<IPostDTO>> getDraftPosts(@Param("idAccount") Integer idAccount, @Param("offset") Integer offset);

    @Query(nativeQuery = true, value = "select id_post as idPost from post " +
            "            where status=1 and access=1 and (lower(title) like :search or lower(content) like :search) " +
            "            LIMIT 10 OFFSET :offset")
    Optional<List<IPostDTO>> getSearchPost(@Param("search") String search, @Param("offset") Integer offset);

    @Query(nativeQuery = true, value = "select id_post as idPost, id_account as idAccount, title, content, last_modified as lastModified, created, view, status, access " +
            "from  ((SELECT P.* " +
            "                FROM post P " +
            "                INNER JOIN post_tag PT ON P.id_post=PT.id_post " +
            "                WHERE PT.id_tag IN ( " +
            "                    SELECT FT.id_tag " +
            "                    FROM follow_tag FT " +
            "                    WHERE FT.id_account=:idAccount " +
            "                    ) AND P.status=1 AND P.access=1  " +
            "                ) " +
            "                UNION " +
            "                (SELECT P.* " +
            "                    FROM post P " +
            "                    INNER JOIN follow_account F ON F.id_follower=P.id_account " +
            "                    WHERE F.id_following=:idAccount AND P.status=1 AND P.access=1  " +
            "                ) " +
            "                ORDER BY created DESC " +
            "                LIMIT 10 OFFSET :offset ) a")
    Optional<List<IPostDTO>> getFollowing(@Param("idAccount") Integer idAccount, @Param("offset") Integer offset);
}
