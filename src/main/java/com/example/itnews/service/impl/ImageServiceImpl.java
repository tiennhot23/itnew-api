package com.example.itnews.service.impl;

import com.example.itnews.entity.Category;
import com.example.itnews.entity.Image;
import com.example.itnews.repository.CategoryRepository;
import com.example.itnews.repository.ImageRepository;
import com.example.itnews.security.exceptions.MRuntimeException;
import com.example.itnews.service.CategoryService;
import com.example.itnews.service.ImageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    private final String TAG = "ImageServiceImpl";
    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Image save(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public void delete(Integer idImage) {
        imageRepository.deleteById(idImage);
    }

    @Override
    public Image get(Integer idImage) {
        return imageRepository.findById(idImage)
                .orElseThrow(() -> new MRuntimeException(TAG + ": image not found"));
    }

    @Override
    public Boolean isExist(Integer idImage) {
        return imageRepository.findById(idImage).isPresent();
    }

    @Override
    public List<Image> listImageInAccount(Integer idAccount) {
        return imageRepository.findAllByAccount_IdAccount(idAccount)
                .orElseGet(ArrayList::new);
    }
}
