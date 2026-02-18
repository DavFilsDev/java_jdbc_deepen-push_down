package models;

import java.math.BigDecimal;

public class InvoiceTotal {

    private int id;
    private String customerName;
    private InvoiceStatus status;
    private BigDecimal totalAmount;

    public InvoiceTotal(int id, String customerName, InvoiceStatus status, BigDecimal totalAmount) {
        this.id = id;
        this.customerName = customerName;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    @Override
    public String toString() {
        return id + " | " + customerName + " | " + totalAmount;
    }
}

