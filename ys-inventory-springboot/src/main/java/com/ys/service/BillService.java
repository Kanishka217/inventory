package com.ys.service;

import com.ys.model.Bill;
import com.ys.model.BillItem;
import com.ys.model.Firm;
import com.ys.repository.BillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final ProductService productService;
    private final AuthService authService;

    public BillService(BillRepository billRepository, ProductService productService, AuthService authService) {
        this.billRepository = billRepository;
        this.productService = productService;
        this.authService = authService;
    }

    public List<Bill> getAllForFirm(Integer firmId) {
        return billRepository.findByFirmIdOrderByDateDescBillNoDesc(firmId);
    }

    /**
     * @Transactional here does exactly what the manual con.setAutoCommit(false) /
     * commit() / rollback() code did in the plain-servlets version — except
     * Spring handles it for us. If anything inside this method throws, EVERYTHING
     * (the bill, its items, and the stock decrements) rolls back automatically.
     */
    @Transactional
    public Bill saveBillAndDecrementStock(Bill bill, Firm firm) {
        if (bill.getId() == null || bill.getId().isEmpty()) bill.setId(UUID.randomUUID().toString());
        bill.setFirm(firm);
        for (BillItem item : bill.getItems()) {
            item.setBill(bill); // link each item back to this bill (needed for the FK column)
        }

        Bill saved = billRepository.save(bill);

        for (BillItem item : bill.getItems()) {
            if (item.getProdId() != null && !item.getProdId().isEmpty()) {
                productService.decrementStock(item.getProdId(), item.getQty());
            }
        }

        authService.incrementBillCounter(firm.getId());
        return saved;
    }

    public void delete(String id) {
        billRepository.deleteById(id);
    }
}
