package com.ys.service;

import com.ys.model.Firm;
import com.ys.repository.FirmRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final FirmRepository firmRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(FirmRepository firmRepository) {
        this.firmRepository = firmRepository;
    }

    public Optional<Firm> authenticate(String username, String password) {
        return firmRepository.findByUsername(username)
                .filter(f -> encoder.matches(password, f.getPasswordHash()));
    }

    public boolean usernameTaken(String username) {
        return firmRepository.findByUsername(username).isPresent();
    }

    public Firm register(Firm firm, String plainPassword) {
        firm.setPasswordHash(encoder.encode(plainPassword));
        firm.setBillCounter(1);
        return firmRepository.save(firm);
    }

    public Firm getById(Integer id) {
        return firmRepository.findById(id).orElseThrow();
    }

    public Firm updateSettings(Firm updated) {
        return firmRepository.save(updated);
    }

    public void incrementBillCounter(Integer firmId) {
        Firm f = getById(firmId);
        f.setBillCounter(f.getBillCounter() + 1);
        firmRepository.save(f);
    }
}
