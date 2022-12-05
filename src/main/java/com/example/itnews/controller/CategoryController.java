package com.example.itnews.controller;

import com.example.itnews.entity.Category;
import com.example.itnews.payloads.response.MResponse;
import com.example.itnews.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public MResponse<List<Category>> getAllCategory() {
        return new MResponse("success", null, categoryService.getAllCategory());
    }

    @GetMapping("/{categoryId}")
    public Optional<Category> getCategoryById(@PathVariable("categoryId") Integer categoryId) {
        return categoryService.getCategory(categoryId);
    }

    @PostMapping
    public Category saveCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    @PutMapping
    public Category updateCategory(@RequestBody Category category) {
        return categoryService.updateCategory(category);
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable("categoryId") Integer categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}
