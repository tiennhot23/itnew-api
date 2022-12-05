package com.example.itnews.repository;

import com.example.itnews.dto.sqlmapping.ILockAccountDTO;
import com.example.itnews.entity.LockAccount;
import com.example.itnews.entity.LockAccountId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LockAccountRepository extends JpaRepository<LockAccount, LockAccountId> {
    Optional<LockAccount> findLockAccountByLockAccountIdIdAccountLock(Integer idAccount);


    @Query(nativeQuery = true, value = "select id_account_lock as idAccountLock, " +
            "id_account_boss as idAccountBoss, reason, time_start_lock as timeStartLock, " +
            "hours_lock as hoursLock, time_end_lock as timeEndLock, " +
            "boss_name as bossName, boss_account as bossAccount, " +
            "locker_account as lockerAccount, locker_name as lockerName, day_start as dayStart, " +
            "time_start as timeStart, day_end as dayEnd, time_end as timeEnd " +
            "from (WITH lock_boss AS(SELECT l.*, a.real_name AS boss_name, " +
            "   a.account_name AS boss_account " +
            "   FROM lock_account l, account a " +
            "   WHERE l.id_account_boss = a.id_account) " +
            "SELECT l.*, a.real_name AS locker_name, " +
            "a.account_name AS locker_account, " +
            "TO_CHAR(l.time_start_lock, 'dd/mm/yyyy') AS day_start, " +
            "TO_CHAR(l.time_start_lock, 'hh24:mi') AS time_start, " +
            "TO_CHAR(l.time_end_lock, 'dd/mm/yyyy') AS day_end, " +
            "TO_CHAR(l.time_end_lock, 'hh24:mi') AS time_end " +
            "FROM lock_boss l, account a " +
            "WHERE l.id_account_lock = a.id_account) a")
    Optional<List<ILockAccountDTO>> selectAll();

    void deleteByLockAccountIdIdAccountLock(Integer idAccountLock);
}
