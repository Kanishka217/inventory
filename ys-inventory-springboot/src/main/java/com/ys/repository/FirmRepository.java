package com.ys.repository;

import com.ys.model.Firm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// JpaRepository already gives us save(), findById(), findAll(), deleteById(), etc.
// for free — we only need to declare the *extra* lookups we want, like this one.
public interface FirmRepository extends JpaRepository<Firm, Integer> {
    Optional<Firm> findByUsername(String username);
}
