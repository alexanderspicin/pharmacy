package com.example.pharmacy.repository;

import com.example.pharmacy.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoryByTitle(String title);
}
