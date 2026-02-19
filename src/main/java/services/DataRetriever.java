package services;

import db.DBConnection;
import models.InvoiceStatus;
import models.InvoiceTotal;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    private final DBConnection dbConnection;

    public DataRetriever(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<InvoiceTotal> findInvoiceTotals() {
        String sql = """
            SELECT i.id, i.customer_name, i.status,
                   SUM(il.quantity * il.unit_price) AS total_amount
            FROM invoice i
            JOIN invoice_line il ON i.id = il.invoice_id
            GROUP BY i.id, i.customer_name, i.status
            ORDER BY i.id;
        """;

        Connection connection = dbConnection.getDBConnection();
        List<InvoiceTotal> results = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("customer_name");
                InvoiceStatus status = InvoiceStatus.valueOf(rs.getString("status"));
                BigDecimal total = rs.getBigDecimal("total_amount");

                results.add(new InvoiceTotal(id, name, status, total));
            }

            return results;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnection.close(connection);
        }
    }

}