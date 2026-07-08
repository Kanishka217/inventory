package com.ys.service;

import com.ys.model.Firm;
import com.ys.model.Product;
import com.ys.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllForFirm(Integer firmId) {
        return productRepository.findByFirmIdOrderByName(firmId);
    }

    public Product save(Product p, Firm firm) {
        if (p.getId() == null || p.getId().isEmpty()) p.setId(UUID.randomUUID().toString());
        p.setFirm(firm);
        return productRepository.save(p);
    }

    public void updateStock(String id, int newStock) {
        Product p = productRepository.findById(id).orElseThrow();
        p.setStock(newStock);
        productRepository.save(p);
    }

    /** Used when a bill is saved — reduces stock, never below zero. */
    public void decrementStock(String id, double qty) {
        Product p = productRepository.findById(id).orElse(null);
        if (p == null) return; // item might have been deleted since — skip rather than fail the whole bill
        p.setStock((int) Math.max(0, p.getStock() - qty));
        productRepository.save(p);
    }

    public void delete(String id) {
        productRepository.deleteById(id);
    }
}
