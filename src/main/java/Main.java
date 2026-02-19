import db.DBConnection;
import models.*;
import services.DataRetriever;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        DBConnection db = new DBConnection();
        DataRetriever dataRetriever = new DataRetriever(db);

        try {
            testFindInvoiceTotals(dataRetriever);
            testFindConfirmedAndPaidInvoiceTotals(dataRetriever);
            testComputeStatusTotals(dataRetriever);
        } catch (Exception e) {
            System.err.println("Error during all test: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void testFindInvoiceTotals(DataRetriever dataRetriever) {
        System.out.println("=== Test Q1: Invoice Totals ===");

        List<InvoiceTotal> totals = dataRetriever.findInvoiceTotals();

        for (InvoiceTotal it : totals) {
            System.out.println(it);
        }
    }

    public static void testFindConfirmedAndPaidInvoiceTotals(DataRetriever dataRetriever) {
        System.out.println("\n=== Q2 - Confirmed and Paid Invoice Totals ===");

        List<InvoiceTotal> totals = dataRetriever.findConfirmedAndPaidInvoiceTotals();

        for (InvoiceTotal it : totals) {
            System.out.println(it.getId() + " | "
                    + it.getCustomerName() + " | "
                    + it.getStatus() + " | "
                    + it.getTotalAmount());
        }
    }

    public static void testComputeStatusTotals(DataRetriever dataRetriever) {
        System.out.println("\n=== Q3 - Status Totals ===");

        InvoiceStatusTotals totals = dataRetriever.computeStatusTotals();
        System.out.println(totals);
    }

}
