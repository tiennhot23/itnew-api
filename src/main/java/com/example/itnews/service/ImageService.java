package com.example.itnews.service;

import com.example.itnews.entity.Category;
import com.example.itnews.entity.Image;

import java.util.List;
import java.util.Optional;

public interface ImageService {
    Image save(Image image);
    void delete(Integer idImage);
    Image get(Integer idImage);
    Boolean isExist(Integer idImage);
    List<Image> listImageInAccount(Integer idAccount);
}
