package org.example.nationalofficetestproject.mapper;

import org.example.nationalofficetestproject.dto.ProductDto;
import org.example.nationalofficetestproject.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "id")
    Product toEntity(ProductDto productDto);

    @Mapping(target = "id", ignore = true)
    ProductDto toDto(Product product);

    @Mapping(target = "id", ignore = true)
    List<ProductDto> toDto(List<Product> products);
}