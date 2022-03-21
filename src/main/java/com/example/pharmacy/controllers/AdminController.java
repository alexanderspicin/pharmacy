package com.example.pharmacy.controllers;

import com.example.pharmacy.DTO.CategoryDTO;
import com.example.pharmacy.DTO.ProductDTO;
import com.example.pharmacy.DTO.UserDTO;

import com.example.pharmacy.service.CategoryService;
import com.example.pharmacy.service.ProductService;
import com.example.pharmacy.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.List;

@RestController("/admin")
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin
public class AdminController {
    private final UserService userService;

    private final ProductService productService;

    private final CategoryService categoryService;

    public AdminController(UserService userService, ProductService productService, CategoryService categoryService) {
        this.userService = userService;
        this.productService = productService;
        this.categoryService = categoryService;
    }


    @DeleteMapping("/deleteUser/{id}")
    @Transactional
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") Long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>("User deleted", HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>("User with id: " + id + " not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/createProduct")
    public ResponseEntity<String> saveProduct(@RequestBody ProductDTO productDTO) {
        try {
            productService.save(productDTO);
            return new ResponseEntity<>("Product successful saved", HttpStatus.OK);
        }catch (RuntimeException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> usersDTO = userService.loadAll();
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }

    @PostMapping("/category/create")
    public ResponseEntity<String> createCategory(@RequestBody CategoryDTO categoryDTO){
        try {
            categoryService.addCategory(categoryDTO);
        }catch (RuntimeException exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>("create category: " + categoryDTO.getTitle(), HttpStatus.OK);
    }

    @PostMapping(path = "/{productId}/uploadImage",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImageToProduct(@PathVariable("productId") Long id, @RequestParam("file") MultipartFile file) {
        try {
            productService.uploadImage(id, file);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PutMapping("/updateProductCategories/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable("id") Long id, @RequestBody List<CategoryDTO> categoryDTOS){
        try{
            productService.updateProductCategory(id,categoryDTOS);
            return new ResponseEntity<>("Category updated", HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }
}
