package com.project.shopapp.services;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exception.DataNotFoundException;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;

    Product getProductById(long id) throws Exception;

    Page<Product> getAllProducts(PageRequest pageRequest);

    Product updateProduct(long id, ProductDTO productDTO) throws Exception;

    void deleteProduct(long id);

    boolean existsByName(String name);

    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception;
}
