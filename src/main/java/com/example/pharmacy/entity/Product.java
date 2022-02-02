package com.example.pharmacy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "product_name", nullable = false, length = 64, unique = true)
    private String productName;

    @Column(name = "product_description", length = 500)
    private String productDescription;

    @Column(nullable = false)
    private Float price;

    @Column(length = 64)
    private String manifacturer;

    @Column(length = 500)
    private String composition;

    @Column(length = 500)
    private String indications;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;

}
