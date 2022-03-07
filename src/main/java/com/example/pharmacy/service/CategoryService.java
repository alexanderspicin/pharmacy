package com.example.pharmacy.service;

import com.example.pharmacy.DTO.CategoryDTO;
import com.example.pharmacy.entity.Category;

import java.util.List;


public interface CategoryService {

    void addCategory(CategoryDTO categoryDTO);

    List<CategoryDTO> getAllCategory();

    CategoryDTO categoryToCategoryDTO(Category category);

    Category getCategoryByTitle(String title);
}
