package com.example.itnews.repository;

import com.example.itnews.entity.Category;
import com.example.itnews.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    Optional<List<Image>> findAllByAccount_IdAccount(Integer idAccount);
}
