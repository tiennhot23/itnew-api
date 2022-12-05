package com.example.itnews.service;

import com.example.itnews.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategory();
    Optional<Category> getCategory(Integer categoryId);
    Category addCategory(Category category);
    Category updateCategory(Category category);
    void deleteCategory(int categoryId);
}
