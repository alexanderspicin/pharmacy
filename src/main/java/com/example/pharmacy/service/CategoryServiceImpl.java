package com.example.pharmacy.service;

import com.example.pharmacy.DTO.CategoryDTO;
import com.example.pharmacy.entity.Category;
import com.example.pharmacy.entity.Product;
import com.example.pharmacy.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        if(categoryRepository.findCategoryByTitle(categoryDTO.getTitle()) != null){
            throw new RuntimeException("Category with title: " + categoryDTO.getTitle() + " already exist");
        }
        Category category = Category.builder()
                .title(categoryDTO.getTitle())
                .build();
        categoryRepository.save(category);
    }

    @Override
    public List<CategoryDTO> getAllCategory() {
        return categoryRepository.findAll().stream().map(this::categoryToCategoryDTO).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO categoryToCategoryDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .title(category.getTitle())
                .build();
    }

    @Override
    public Category getCategoryByTitle(String title) {
        return categoryRepository.findCategoryByTitle(title);
    }

}
