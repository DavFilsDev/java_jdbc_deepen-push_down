import db.DBConnection;
import services.DataRetriever;
import models.InvoiceTotal;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        DBConnection db = new DBConnection();
        DataRetriever dataRetriever = new DataRetriever(db);

        try {
            testFindInvoiceTotals(dataRetriever);

        } catch (Exception e) {
            System.err.println("Erreur pendant les tests: " + e.getMessage());
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
}
