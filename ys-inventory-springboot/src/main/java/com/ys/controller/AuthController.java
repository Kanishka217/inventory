package com.ys.controller;

import com.ys.model.Firm;
import com.ys.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(params = "action=login")
    public Map<String, Object> login(@RequestBody AuthRequest req, HttpSession session, HttpServletResponse resp) {
        Optional<Firm> firm = authService.authenticate(req.getUsername(), req.getPassword());
        if (firm.isEmpty()) {
            resp.setStatus(401);
            return err("Invalid username or password");
        }
        session.setAttribute("firmId", firm.get().getId());
        return ok(firm.get());
    }

    @PostMapping(params = "action=register")
    public Map<String, Object> register(@RequestBody AuthRequest req, HttpSession session, HttpServletResponse resp) {
        if (req.getUsername() == null || req.getUsername().isEmpty()
                || req.getPassword() == null || req.getPassword().length() < 4) {
            resp.setStatus(400);
            return err("Username required, password must be 4+ chars");
        }
        if (authService.usernameTaken(req.getUsername())) {
            resp.setStatus(409);
            return err("Username already taken");
        }
        Firm firm = new Firm();
        firm.setUsername(req.getUsername());
        firm.setFirmName(req.getFirmName());
        firm.setPropName(req.getPropName());
        firm.setAddress(req.getAddress());
        firm.setPhone(req.getPhone());
        firm.setGstin(req.getGstin());
        firm.setEmail(req.getEmail());

        Firm saved = authService.register(firm, req.getPassword());
        session.setAttribute("firmId", saved.getId());
        return ok(saved);
    }

    @PostMapping(params = "action=logout")
    public Map<String, Object> logout(HttpSession session) {
        session.invalidate();
        return Map.of("status", "ok");
    }

    @GetMapping(params = "action=me")
    public Map<String, Object> me(HttpSession session, HttpServletResponse resp) {
        Object firmId = session.getAttribute("firmId");
        if (firmId == null) {
            resp.setStatus(401);
            return err("Not logged in");
        }
        return ok(authService.getById((Integer) firmId));
    }

    @PostMapping(params = "action=updateSettings")
    public Map<String, Object> updateSettings(@RequestBody AuthRequest req, HttpSession session, HttpServletResponse resp) {
        Object firmId = session.getAttribute("firmId");
        if (firmId == null) {
            resp.setStatus(401);
            return err("Not logged in");
        }
        Firm firm = authService.getById((Integer) firmId);
        firm.setFirmName(req.getFirmName());
        firm.setPropName(req.getPropName());
        firm.setAddress(req.getAddress());
        firm.setPhone(req.getPhone());
        firm.setGstin(req.getGstin());
        firm.setEmail(req.getEmail());
        return ok(authService.updateSettings(firm));
    }

    // ---- helpers ----

    private Map<String, Object> ok(Firm firm) {
        Map<String, Object> settings = new HashMap<>();
        settings.put("firmName", firm.getFirmName());
        settings.put("propName", firm.getPropName());
        settings.put("address", firm.getAddress());
        settings.put("phone", firm.getPhone());
        settings.put("gstin", firm.getGstin());
        settings.put("email", firm.getEmail());
        settings.put("billCounter", firm.getBillCounter());
        // never include passwordHash — this goes straight to the browser
        return Map.of("status", "ok", "settings", settings);
    }

    private Map<String, Object> err(String message) {
        return Map.of("status", "error", "message", message);
    }
}
