package com.ys.repository;

import com.ys.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByFirmIdOrderByName(Integer firmId);
    // Spring reads this method name and builds the SQL itself:
    // SELECT * FROM products WHERE firm_id=? ORDER BY name
}
