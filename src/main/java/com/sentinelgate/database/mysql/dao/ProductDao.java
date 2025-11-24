package com.sentinelgate.database.mysql.dao;

import com.sentinelgate.database.mysql.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductDao extends JpaRepository<Product, Long> {

    Optional<Product> findFirstByName(String name);

}
