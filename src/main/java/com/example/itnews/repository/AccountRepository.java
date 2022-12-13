package com.example.itnews.repository;

import com.example.itnews.dto.sqlmapping.IAccountDTO;
import com.example.itnews.entity.Account;
import com.example.itnews.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    @Query("SELECT a FROM Account a WHERE a.accountName = ?1")
    Optional<Account> findByAccountName(String accountName);

    Optional<Account> findAccountByAccountName(String accountName);

    Optional<Account> findAccountByEmail(String email);

    @Query(nativeQuery = true, value = "select id_account as idAccount, id_role as idRole, " +
            "role as role, account_name as accountName, real_name as realName, email, gender, avatar, company, phone, " +
            "account_status as accountStatus, birth, create_date as createDate, total_post as totalPost, " +
            "total_follower as totalFollower, total_following as totalFollowing, total_view as totalView, " +
            "total_vote_up as totalVoteUp, total_vote_down as totalVoteDown from (SELECT A.id_account, R.id_role as id_role, R.name as role, A.account_name, " +
            "A.real_name, A.email, A.gender,A.avatar, A.company, A.phone, A.status as account_status, " +
            "TO_CHAR(A.birth, 'dd/mm/yyyy') AS birth, " +
            "TO_CHAR(A.create_date, 'dd/mm/yyyy') AS create_date, " +
            "(select count(*) from post P where A.id_account=P.id_account and P.status=1 and P.access=1) AS total_post, " +
            "(select count(*) from follow_account FA where A.id_account=FA.id_follower) as total_follower, " +
            "(select count(*) from follow_account FA where A.id_account=FA.id_following) as total_following, " +
            "(select sum(view) from post P where P.id_account=A.id_account) as total_view, " +
            "   (select sum(count_vote) from (select count(V.id_post) as count_vote " +
            "       from post P, vote V " +
            "       where P.id_account= :id and V.id_post=P.id_post and V.type=1 " +
            "       group by P.id_post) as CV) as total_vote_up, " +
            "(select sum(count_vote) from (select count(V.id_post) as count_vote"  +
            "   from post P, vote V " +
            "   where P.id_account= :id and V.id_post=P.id_post and V.type=0 " +
            "   group by P.id_post) as CV) as total_vote_down " +
            "FROM account A " +
            "INNER JOIN role R ON A.id_role=R.id_role " +
            "WHERE A.id_account= :id) as a")
    Optional<IAccountDTO> selectId(@Param("id") Integer id);

    @Query(nativeQuery = true, value = "select id_account as idAccount, id_role as idRole, " +
            "role as role, account_name as accountName, real_name as realName, email, gender, avatar, company, phone, status, " +
            "account_status as accountStatus, birth, create_date as createDate, total_post as totalPost, " +
            "total_follower as totalFollower, total_following as totalFollowing, total_view as totalView, " +
            "total_vote_up as totalVoteUp, total_vote_down as totalVoteDown from (SELECT A.id_account, R.id_role as id_role, R.name as role, A.account_name, " +
            "A.real_name, A.email, A.gender,A.avatar, A.company, A.phone, A.status as account_status, " +
            "TO_CHAR(A.birth, 'dd/mm/yyyy') AS birth, " +
            "TO_CHAR(A.create_date, 'dd/mm/yyyy') AS create_date, " +
            "(select exists(select * from follow_account where id_follower=:idAccount and id_following=:idUser)) as status, " +
            "(select count(*) from post P where A.id_account=P.id_account and P.status=1 and P.access=1) AS total_post, " +
            "(select count(*) from follow_account FA where A.id_account=FA.id_follower) as total_follower, " +
            "(select count(*) from follow_account FA where A.id_account=FA.id_following) as total_following, " +
            "(select sum(view) from post P where P.id_account=A.id_account) as total_view, " +
            "   (select sum(count_vote) from (select count(V.id_post) as count_vote " +
            "       from post P, vote V " +
            "       where P.id_account= :idAccount and V.id_post=P.id_post and V.type=1 " +
            "       group by P.id_post) as CV) as total_vote_up, " +
            "(select sum(count_vote) from (select count(V.id_post) as count_vote"  +
            "   from post P, vote V " +
            "   where P.id_account= :idAccount and V.id_post=P.id_post and V.type=0 " +
            "   group by P.id_post) as CV) as total_vote_down " +
            "FROM account A " +
            "INNER JOIN role R ON A.id_role=R.id_role " +
            "WHERE A.id_account= :idAccount) as a")
    Optional<IAccountDTO> selectIdStatus(@Param("idAccount") Integer idAccount, @Param("idUser") Integer idUser);

    @Query(nativeQuery = true, value = "select id_account as idAccount from account order by idAccount desc")
    Optional<List<IAccountDTO>> selectAllId();

    @Query(nativeQuery = true, value = "select id_account as idAccount from account where (lower(account_name) like :search or lower(real_name) like :search) LIMIT 10 OFFSET :offset")
    Optional<List<IAccountDTO>> getSearch(@Param("search") String search, @Param("offset") Integer offset);
}
