import DAO.InvoiceDAO;
import models.*;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) throws Exception {

        Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/invoice_management_db",
                "postgres",
                "password"
        );

        InvoiceDAO dao = new InvoiceDAO(conn);

        for (InvoiceTotal it : dao.findInvoiceTotals()) {
            System.out.println(it);
        }
    }
}
