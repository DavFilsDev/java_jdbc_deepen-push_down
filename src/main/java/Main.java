import dao.InvoiceDAO;
import db.DBConnection;
import models.InvoiceTotal;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) throws Exception {

        DBConnection db = new DBConnection();
        Connection conn = db.getDBConnection();

        InvoiceDAO dao = new InvoiceDAO(conn);

        for (InvoiceTotal total : dao.findInvoiceTotals()) {
            System.out.println(total);
        }

        db.close(conn);
    }
}

