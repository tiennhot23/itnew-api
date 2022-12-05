package com.example.itnews.repository;

import com.example.itnews.dto.sqlmapping.IPostDTO;
import com.example.itnews.dto.sqlmapping.ITagDTO;
import com.example.itnews.entity.Post;
import com.example.itnews.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query(nativeQuery = true, value = "select id_tag as idTag, name, logo, total_post as totalPost," +
            "               total_follower as totalFollower, status from (select T.*,  " +
            "                (select count(*) from post_tag PT, post P where T.id_tag=PT.id_tag and PT.id_post=P.id_post and P.status=1 and P.access=1) total_post, " +
            "                (select count(*) from follow_tag FT where T.id_tag=FT.id_tag) total_follower, " +
            "                false as status " +
            "                from tag T order by T.id_tag) a")
    Optional<List<ITagDTO>> selectAll();

    @Query(nativeQuery = true, value = "select id_tag as idTag, name, logo, total_post as totalPost, " +
            "               total_follower as totalFollower, status from (select T.*,  " +
            "                (select count(*) from post_tag PT, post P where T.id_tag=PT.id_tag and PT.id_post=P.id_post and P.status=1 and P.access=1) total_post, " +
            "                (select count(*) from follow_tag FT where T.id_tag=FT.id_tag) total_follower, " +
            "                (select exists(select * from follow_tag FT where T.id_tag=FT.id_tag and FT.id_account=:idAccount)) as status " +
            "                from tag T order by T.id_tag) a")
    Optional<List<ITagDTO>> selectAllByAccount(@Param("idAccount") Integer idAccount);

    @Query(nativeQuery = true, value = "select id_tag as idTag from tag where lower(name) like :search limit 10 offset :offset")
    Optional<List<ITagDTO>> getSearch(@Param("search") String search, @Param("offset") Integer offset);

    Boolean existsByName(String name);
    Optional<Tag> findTagByName(String name);

    @Query(nativeQuery = true, value = "select id_tag as idTag, name, logo, total_post as totalPost, + " +
            "               total_follower as totalFollower, status from (select T.*,  " +
            "                (select count(*) from post_tag PT, post P where T.id_tag=$1 and T.id_tag=PT.id_tag and PT.id_post=P.id_post and P.status=1 and P.access=1) total_post, " +
            "                (select count(*) from follow_tag FT where T.id_tag=$1 and T.id_tag=FT.id_tag) total_follower, " +
            "                false as status " +
            "                from tag T " +
            "                where T.id_tag=:id) a")
    Optional<ITagDTO> selectId(@Param("id") Integer id);

    @Query(nativeQuery = true, value = "select id_tag as idTag, name, logo, total_post as totalPost, + " +
            "            total_follower as totalFollower, status from (select T.*,  " +
            "                (select count(*) from post_tag PT, post P where T.id_tag=$1 and T.id_tag=PT.id_tag and PT.id_post=P.id_post and P.status=1 and P.access=1) total_post, " +
            "                (select count(*) from follow_tag FT where T.id_tag=$1 and T.id_tag=FT.id_tag) total_follower, " +
            "                (select exists(select * from follow_tag FT where T.id_tag=:idTag and T.id_tag=FT.id_tag and FT.id_account=:idAccount)) as status " +
            "                from tag T " +
            "                where T.id_tag=:idTag) a")
    Optional<ITagDTO> selectIdByAccount(@Param("idTag") Integer idTag, @Param("idAccount") Integer idAccount);

    @Query(nativeQuery = true, value = "select count(id_post) as totalPost from post_tag where id_tag = :idTag")
    Optional<ITagDTO> countPostsOfTag(@Param("idTag") Integer idTag);
}
