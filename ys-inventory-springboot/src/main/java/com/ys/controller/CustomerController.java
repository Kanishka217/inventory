package com.ys.controller;

import com.ys.model.Customer;
import com.ys.service.AuthService;
import com.ys.service.CustomerService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class CustomerController extends BaseController {

    private final CustomerService customerService;
    private final AuthService authService;

    public CustomerController(CustomerService customerService, AuthService authService) {
        this.customerService = customerService;
        this.authService = authService;
    }

    @Override
    protected AuthService authService() { return authService; }

    @GetMapping
    public List<Customer> list(HttpSession session) {
        return customerService.getAllForFirm(firmId(session));
    }

    @PostMapping
    public Map<String, Object> save(@RequestBody Customer c, HttpSession session, HttpServletResponse resp) {
        if (c.getName() == null || c.getName().trim().isEmpty()
                || c.getPhone() == null || c.getPhone().trim().isEmpty()) {
            resp.setStatus(400);
            return Map.of("status", "error", "message", "Name and phone required");
        }
        Customer saved = customerService.save(c, firm(session));
        return Map.of("status", "ok", "customer", saved);
    }

    @DeleteMapping
    public Map<String, Object> delete(@RequestParam String id) {
        customerService.delete(id);
        return Map.of("status", "ok");
    }
}
