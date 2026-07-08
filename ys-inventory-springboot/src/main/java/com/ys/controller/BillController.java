package com.ys.controller;

import com.ys.model.Bill;
import com.ys.service.AuthService;
import com.ys.service.BillService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bills")
public class BillController extends BaseController {

    private final BillService billService;
    private final AuthService authService;

    public BillController(BillService billService, AuthService authService) {
        this.billService = billService;
        this.authService = authService;
    }

    @Override
    protected AuthService authService() { return authService; }

    @GetMapping
    public List<Bill> list(HttpSession session) {
        return billService.getAllForFirm(firmId(session));
    }

    @GetMapping(params = "id")
    public Bill getOne(@RequestParam String id, HttpSession session, HttpServletResponse resp) {
        Optional<Bill> bill = billService.getAllForFirm(firmId(session)).stream()
                .filter(b -> b.getId().equals(id)).findFirst();
        if (bill.isEmpty()) {
            resp.setStatus(404);
            return null;
        }
        return bill.get();
    }

    @PostMapping
    public Map<String, Object> save(@RequestBody Bill bill, HttpSession session, HttpServletResponse resp) {
        if (bill.getItems() == null || bill.getItems().isEmpty()) {
            resp.setStatus(400);
            return Map.of("status", "error", "message", "Add at least one product");
        }
        // Server decrements stock and saves the bill in one atomic transaction —
        // see BillService.saveBillAndDecrementStock's @Transactional.
        Bill saved = billService.saveBillAndDecrementStock(bill, firm(session));
        return Map.of("status", "ok", "bill", saved);
    }

    @DeleteMapping
    public Map<String, Object> delete(@RequestParam String id) {
        billService.delete(id);
        return Map.of("status", "ok");
    }
}
