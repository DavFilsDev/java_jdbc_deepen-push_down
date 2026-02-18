package dao;

import models.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {

    private final Connection connection;

    public InvoiceDAO(Connection connection) {
        this.connection = connection;
    }

    public List<InvoiceTotal> findInvoiceTotals() throws SQLException {
        List<InvoiceTotal> results = new ArrayList<>();

        String sql = """
            SELECT i.id, i.customer_name, i.status,
                   SUM(il.quantity * il.unit_price) AS total_amount
            FROM invoice i
            JOIN invoice_line il ON i.id = il.invoice_id
            GROUP BY i.id, i.customer_name, i.status
            ORDER BY i.id;
        """;

        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("customer_name");
            InvoiceStatus status = InvoiceStatus.valueOf(rs.getString("status"));
            BigDecimal total = rs.getBigDecimal("total_amount");

            results.add(new InvoiceTotal(id, name, status, total));
        }

        return results;
    }
}
