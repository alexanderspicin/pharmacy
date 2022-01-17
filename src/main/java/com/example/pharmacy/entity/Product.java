package com.example.pharmacy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 64, unique = true)
    private String product_name;

    @Column(length = 500)
    private String product_description;

    @Column(nullable = false)
    private int price;

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

    public Product(Long id, String product_name, String product_description, int price, String manifacturer, String composition, String indications) {
        this.id = id;
        this.product_name = product_name;
        this.product_description = product_description;
        this.price = price;
        this.manifacturer = manifacturer;
        this.composition = composition;
        this.indications = indications;
    }
}
