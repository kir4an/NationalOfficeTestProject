package org.example.nationalofficetestproject.service;

import org.example.nationalofficetestproject.dto.ProductDto;
import org.example.nationalofficetestproject.exception.ProductNotFoundException;
import org.example.nationalofficetestproject.mapper.ProductMapper;
import org.example.nationalofficetestproject.model.Product;
import org.example.nationalofficetestproject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Transactional
    public void saveProduct(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        productRepository.save(product);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProductDto getById(Long id) {
        Product product = getProductById(id);
        return productMapper.toDto(product);
    }

    @Transactional
    public void updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = getProductById(id);
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());

        productRepository.save(existingProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.toDto(products);
    }

    @Transactional
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }
}