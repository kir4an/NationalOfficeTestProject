package org.example.nationalofficetestproject.service;

import org.example.nationalofficetestproject.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    void saveProduct(ProductDto productDto);

    ProductDto getById(Long id);

    List<ProductDto> getAllProducts();

    void deleteProduct(Long id);

    void updateProduct(Long id, ProductDto productDto);

}
