package models;

import java.math.BigDecimal;

public class InvoiceLine {

    private int id;
    private int invoiceId;
    private String label;
    private int quantity;
    private BigDecimal unitPrice;

    public InvoiceLine(int invoiceId, String label, int quantity, BigDecimal unitPrice) {
        this.invoiceId = invoiceId;
        this.label = label;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public InvoiceLine(int id, int invoiceId, String label, int quantity, BigDecimal unitPrice) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.label = label;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getId() {
        return id;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public String getLabel() {
        return label;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "InvoiceLine{id=" + id + ", invoiceId=" + invoiceId +
                ", label='" + label + "', quantity=" + quantity +
                ", unitPrice=" + unitPrice + "}";
    }
}

