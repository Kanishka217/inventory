package com.ys.service;

import com.ys.model.Customer;
import com.ys.model.Firm;
import com.ys.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllForFirm(Integer firmId) {
        return customerRepository.findByFirmIdOrderByName(firmId);
    }

    public Customer save(Customer c, Firm firm) {
        if (c.getId() == null || c.getId().isEmpty()) c.setId(UUID.randomUUID().toString());
        c.setFirm(firm);
        return customerRepository.save(c);
    }

    public void delete(String id) {
        customerRepository.deleteById(id);
    }
}
