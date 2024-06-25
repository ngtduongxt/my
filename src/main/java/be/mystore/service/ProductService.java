package be.mystore.service;

import be.mystore.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAllProduct();

    Optional<Product> findOneProduct(long id);

    void createProduct(Product product);

    Product updateProduct(Product product);

    void deleteProduct(long id);
}
