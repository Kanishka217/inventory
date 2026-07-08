package com.ys.controller;

import com.ys.model.Product;
import com.ys.service.AuthService;
import com.ys.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController extends BaseController {

    private final ProductService productService;
    private final AuthService authService;

    public ProductController(ProductService productService, AuthService authService) {
        this.productService = productService;
        this.authService = authService;
    }

    @Override
    protected AuthService authService() { return authService; }

    @GetMapping
    public List<Product> list(HttpSession session) {
        return productService.getAllForFirm(firmId(session));
    }

    @PostMapping
    public Map<String, Object> save(@RequestBody Product p, HttpSession session, HttpServletResponse resp) {
        if (p.getName() == null || p.getName().trim().isEmpty()) {
            resp.setStatus(400);
            return Map.of("status", "error", "message", "Product name required");
        }
        if (p.getSelling() <= 0) {
            resp.setStatus(400);
            return Map.of("status", "error", "message", "Enter selling price");
        }
        Product saved = productService.save(p, firm(session));
        return Map.of("status", "ok", "product", saved);
    }

    @PostMapping(params = "action=stock")
    public Map<String, Object> adjustStock(@RequestBody Map<String, Object> body) {
        String id = (String) body.get("id");
        int stock = ((Number) body.get("stock")).intValue();
        productService.updateStock(id, stock);
        return Map.of("status", "ok");
    }

    @DeleteMapping
    public Map<String, Object> delete(@RequestParam String id) {
        productService.delete(id);
        return Map.of("status", "ok");
    }
}
