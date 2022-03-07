package com.example.pharmacy.controllers;

import com.example.pharmacy.DTO.CategoryDTO;
import com.example.pharmacy.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/category")
@RequestMapping("/category")
@CrossOrigin
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<CategoryDTO>> getAllCategory(){
        List<CategoryDTO> categoryDTOS = categoryService.getAllCategory();
        return new ResponseEntity<>(categoryDTOS, HttpStatus.OK);
    }
}
