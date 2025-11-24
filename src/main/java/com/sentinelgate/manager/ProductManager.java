package com.sentinelgate.manager;

import com.sentinelgate.database.mysql.dao.ProductDao;
import com.sentinelgate.database.mysql.entity.Product;
import com.sentinelgate.request.AddProductRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductManager {

    @Autowired
    ProductDao productDao;

    Logger log = LoggerFactory.getLogger(ProductManager.class);

    public ResponseEntity<Product> getProduct(String productName) {
        try {
            log.info("Request to get product: {}", productName);

            if (productName == null || productName.isBlank()) {
                log.warn("getProduct called with empty productName");
                return ResponseEntity.badRequest().build();
            }

            Optional<Product> productOptional = productDao.findFirstByName(productName);

            if (productOptional.isPresent()) {
                return ResponseEntity.ok(productOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (Exception e) {
            log.error("Error while getting product, err: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<Product> addProduct(AddProductRequest request) {
        try {
            // Basic null check
            if (request == null) {
                log.warn("addProduct called with null request");
                return ResponseEntity.badRequest().build();
            }

            // Validate required fields (adjust validations to your domain)
            String name = request.getName();
            if (name.isBlank()) {
                log.warn("addProduct called with empty name");
                return ResponseEntity.badRequest().body(null);
            }


             if (request.getPrice() < 0) {
                 return ResponseEntity.badRequest().build();
             }

            // Check if product with same name already exists -> avoid duplicates
            Optional<Product> existingProduct = productDao.findFirstByName(name);
            if (existingProduct.isPresent()) {
                log.info("Product with name '{}' already exists (id={}), returning 409", name, existingProduct.get().getId());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            // Map AddProductRequest -> Product entity
            Product product = Product.builder()                 // assuming Lombok @Builder on Product
                    .name(name.trim())
                    .description(request.getDesc())
                    .price(request.getPrice())               // adjust field names/types to your entity
                    .build();

            // Persist to DB
            product = productDao.save(product);

            return ResponseEntity.status(HttpStatus.CREATED).body(product);

        } catch (Exception ex) {
            log.error("Error while adding product: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
