package com.sentinelgate.contollers;

import com.sentinelgate.database.mysql.entity.Product;
import com.sentinelgate.manager.ProductManager;
import com.sentinelgate.request.AddProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class ProductController {

    @Autowired
    ProductManager productManager;

    @GetMapping("/{name}")
    public ResponseEntity<Product> getProduct(@PathVariable("name") String productName) {
        return productManager.getProduct(productName);
    }

    @PostMapping(value = {"", "/"})
    public ResponseEntity<Product> addProduct(@RequestBody AddProductRequest request) {
        return productManager.addProduct(request);
    }

}
