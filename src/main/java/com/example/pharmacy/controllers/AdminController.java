package com.example.pharmacy.controllers;

import com.example.pharmacy.DTO.ProductDTO;
import com.example.pharmacy.DTO.UserDTO;
import com.example.pharmacy.service.ProductService;
import com.example.pharmacy.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole(ADMIN)")
public class AdminController {
    private final UserService userService;

    private final ProductService productService;

    public AdminController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/deleteProduct/{id}")
    @Transactional
    public ResponseEntity<Boolean> deleteProduct(@PathVariable(name = "id") Long id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println(exception);
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/createProduct")
    public ResponseEntity<String> saveProduct(@RequestBody ProductDTO productDTO) {
        if (productService.save(productDTO)) {
            return new ResponseEntity<>("Product successful saved", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Oops", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable(name = "id") Long id) {
        try {
            UserDTO userDTO = userService.loadUserById(id);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> usersDTO = userService.loadAll();
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }
}
