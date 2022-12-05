package com.example.itnews.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LockAccountId implements Serializable {

    @Column(name = "id_account_lock")
    private Integer idAccountLock;

    @Column(name = "id_account_boss")
    private Integer idAccountBoss;

    @Override
    public String toString() {
        return "LockAccountId{" +
                "idAccountLock=" + idAccountLock +
                ", idAccountBoss=" + idAccountBoss +
                '}';
    }
}
