package com.example.pharmacy.service;

import com.example.pharmacy.DTO.CategoryDTO;
import com.example.pharmacy.DTO.ProductDTO;
import com.example.pharmacy.S3BucketName.S3Bucket;
import com.example.pharmacy.entity.Bucket;
import com.example.pharmacy.entity.Category;
import com.example.pharmacy.entity.Product;
import com.example.pharmacy.entity.User;
import com.example.pharmacy.repository.ProductRepository;
import org.apache.http.entity.ContentType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;
    private final BucketService bucketService;
    private final CategoryService categoryService;
    private final FileService fileService;

    public ProductServiceImpl(ProductRepository productRepository, UserService userService, BucketService bucketService, CategoryService categoryService, CategoryService categoryService1, FileService fileService) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.bucketService = bucketService;
        this.categoryService = categoryService1;
        this.fileService = fileService;
    }

    @Override
    public boolean save(ProductDTO productDTO) throws DataIntegrityViolationException {
        List<CategoryDTO> categoriesDTO = productDTO.getCategories();
        List<Category> categories = new ArrayList<>();
        for (CategoryDTO categoryDTO : categoriesDTO) {
            if (categoryService.getCategoryByTitle(categoryDTO.getTitle()) == null) {
                throw new RuntimeException("Category with title: " + categoryDTO.getTitle() + " not found");
            }else{
                categories.add(categoryService.getCategoryByTitle(categoryDTO.getTitle()));
            }
        }
        if (productDTO.getProductName() == null){
            throw new RuntimeException("Product name is empty");
        }
        if (productDTO.getProductDescription() == null){
            throw new RuntimeException("Product description is empty");
        }
        if (productDTO.getComposition() == null){
            throw new RuntimeException("Product composition is empty");
        }
        if (productDTO.getIndications()== null){
            throw new RuntimeException("Product indications is empty");
        }
        if (productDTO.getManifacturer() == null){
            throw new RuntimeException("Product manufacturer is empty");
        }
        if (productDTO.getPrice() == null){
            throw new RuntimeException("Product price is empty");
        }
        Product product = Product.builder()
                .productName(productDTO.getProductName())
                .productDescription(productDTO.getProductDescription())
                .composition(productDTO.getComposition())
                .indications(productDTO.getIndications())
                .categories(categories)
                .price(productDTO.getPrice())
                .manifacturer(productDTO.getManifacturer()).build();
        try {
            productRepository.save(product);
        } catch (DataIntegrityViolationException exception) {
            throw new RuntimeException("Can't save product");
        }
        return true;
    }

    @Override
    public List<ProductDTO> loadProductsByCategory(CategoryDTO categoryDTO) {
        Category category = categoryService.getCategoryByTitle(categoryDTO.getTitle());
        List<ProductDTO> productsDTO = productRepository.findAllByCategories(category).stream().map(this::productToProductDTO).collect(Collectors.toList());
        return productsDTO;

    }

    @Override
    public ProductDTO loadProductById(Long id) {
        Product product = productRepository.findProductById(id);
        if (product == null) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        return new ProductDTO(product.getId(), product.getProductName(),
                product.getProductDescription(),
                product.getPrice(),
                product.getManifacturer(),
                product.getComposition(),
                getCollectCategoryDTOByCategory(product.getCategories()),
                product.getIndications(),
                product.getImageLink());
    }

    @Override
    public boolean updateProductCategory(Long id, List<CategoryDTO> categoryDTOS) {
        Product product = productRepository.findProductById(id);
        List<Category> categories = product.getCategories();
        List<Category> newProductCategoriesList = categories == null ? new ArrayList<>() : new ArrayList<>(categories);
        for (CategoryDTO categoryDTO : categoryDTOS) {
            Category findCategory = categoryService.getCategoryByTitle(categoryDTO.getTitle());
            if (findCategory == null) {
                throw new RuntimeException("Category with title: " + categoryDTO.getTitle() + " not found");
            } else if (newProductCategoriesList.contains(findCategory)) {
                throw new RuntimeException("Product: " + product.getProductName() + " already have category: " + categoryDTO.getTitle());
            }
            newProductCategoriesList.add(findCategory);
        }

        product.setCategories(newProductCategoriesList);
        try {
            productRepository.save(product);
        } catch (DataIntegrityViolationException exception) {
            throw new RuntimeException("Can't update product");
        }
        return true;
    }

    @Override
    public void deleteProduct(Long id, String username) {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found - " + username);
        }
        Bucket bucket = user.getBucket();
        bucketService.deleteProduct(bucket, id);
    }

    @Override
    public ProductDTO loadProductByName(String name) {
        Product product = productRepository.findProductByProductName(name);
        if (product == null) {
            throw new RuntimeException("Product not found with name: " + name);
        }
        return new ProductDTO(product.getId(), product.getProductName(),
                product.getProductDescription(),
                product.getPrice(),
                product.getManifacturer(),
                product.getComposition(),
                getCollectCategoryDTOByCategory(product.getCategories()),
                product.getIndications(),
                product.getImageLink());
    }

    @Override
    public void deleteAllProductById(Long id, String username) {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found - " + username);
        }
        Bucket bucket = user.getBucket();
        bucketService.deleteAllProductById(bucket, id);
    }

    @Override
    @Transactional
    public void addToUserBucket(Long productId, String username) {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found - " + username);
        }
        Bucket bucket = user.getBucket();
        if (bucket == null) {
            Bucket newBucket = bucketService.createBucket(user, Collections.singletonList(productId));
            user.setBucket(newBucket
            );
            userService.save(user);
        } else {
            bucketService.addProducts(bucket, Collections.singletonList(productId));
        }
    }

    @Override
    public List<ProductDTO> loadAll() {
        return productRepository.findAll().stream().map(this::productToProductDTO).collect(Collectors.toList());
    }

    private List<CategoryDTO> getCollectCategoryDTOByCategory(List<Category> categories) {
        return categories.stream().map(categoryService::categoryToCategoryDTO).collect(Collectors.toList());
    }

    @Override
    public ProductDTO productToProductDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .composition(product.getComposition())
                .indications(product.getIndications())
                .productDescription(product.getProductDescription())
                .categories(getCollectCategoryDTOByCategory(product.getCategories()))
                .manifacturer(product.getManifacturer())
                .price(product.getPrice())
                .imageLink(product.getImageLink())
                .build();
    }

    @Override
    public void uploadImage(Long id, MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }
        if (!Arrays.asList(ContentType.IMAGE_PNG.getMimeType(), ContentType.IMAGE_JPEG.getMimeType(), ContentType.IMAGE_SVG.getMimeType(), ContentType.IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
            throw new RuntimeException("Cannot upload file with type: " + file.getContentType());
        }
        Product product = productRepository.findProductById(id);
        if (product == null) {
            throw new RuntimeException("Cannot find product with id: " + id);
        }
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        String path = String.format("%s/%s", S3Bucket.PROFILE_IMAGE.getBucketName(), id);
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
        try {
            fileService.save(path, filename, Optional.of(metadata), file.getInputStream());
            product.setImageLink(filename);
            productRepository.save(product);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] downloadImage(Long id) {
        Product product = productRepository.findProductById(id);
        if (product == null) {
            return new byte[0];
        }
        String path = String.format("%s/%s", S3Bucket.PROFILE_IMAGE.getBucketName(), id);
        String imageLink = product.getImageLink();
        if (imageLink != null) {
            return fileService.download(path, imageLink);
        } else {
            return new byte[0];
        }
    }
}
