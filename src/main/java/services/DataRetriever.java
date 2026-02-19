package services;

import db.DBConnection;
import models.*;

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

    public List<InvoiceTotal> findConfirmedAndPaidInvoiceTotals() {
        String sql = """
        SELECT i.id, i.customer_name, i.status,
               SUM(il.quantity * il.unit_price) AS total_amount
        FROM invoice i
        JOIN invoice_line il ON i.id = il.invoice_id
        WHERE i.status IN ('CONFIRMED', 'PAID')
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnection.close(connection);
        }
    }

    public InvoiceStatusTotals computeStatusTotals() {
        String sql = """
        SELECT
            SUM(CASE WHEN i.status = 'PAID' THEN il.quantity * il.unit_price ELSE 0 END) AS total_paid,
            SUM(CASE WHEN i.status = 'CONFIRMED' THEN il.quantity * il.unit_price ELSE 0 END) AS total_confirmed,
            SUM(CASE WHEN i.status = 'DRAFT' THEN il.quantity * il.unit_price ELSE 0 END) AS total_draft
        FROM invoice i
        JOIN invoice_line il ON i.id = il.invoice_id;
    """;

        Connection connection = dbConnection.getDBConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                BigDecimal paid = rs.getBigDecimal("total_paid");
                BigDecimal confirmed = rs.getBigDecimal("total_confirmed");
                BigDecimal draft = rs.getBigDecimal("total_draft");

                return new InvoiceStatusTotals(paid, confirmed, draft);
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnection.close(connection);
        }
    }

    public Double computeWeightedTurnover() {
        String sql = """
        SELECT 
            SUM(
                CASE 
                    WHEN i.status = 'PAID' THEN il.quantity * il.unit_price * 1.0
                    WHEN i.status = 'CONFIRMED' THEN il.quantity * il.unit_price * 0.5
                    WHEN i.status = 'DRAFT' THEN 0
                END
            ) AS weighted_turnover
        FROM invoice i
        JOIN invoice_line il ON i.id = il.invoice_id;
    """;

        Connection connection = dbConnection.getDBConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("weighted_turnover");
            }

            return 0.0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnection.close(connection);
        }
    }

    public List<InvoiceTaxSummary> findInvoiceTaxSummaries() {
        String sql = """
            SELECT 
                i.id,
                ROUND(SUM(il.quantity * il.unit_price), 2) AS total_ht,
                ROUND(SUM(il.quantity * il.unit_price) * (t.rate / 100), 2) AS total_tva,
                ROUND(SUM(il.quantity * il.unit_price) * (1 + t.rate / 100), 2) AS total_ttc
            FROM invoice i
            JOIN invoice_line il ON i.id = il.invoice_id
            CROSS JOIN tax_config t
            GROUP BY i.id, t.rate
            ORDER BY i.id;
        """;

        Connection connection = dbConnection.getDBConnection();
        List<InvoiceTaxSummary> results = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                BigDecimal ht = rs.getBigDecimal("total_ht");
                BigDecimal tva = rs.getBigDecimal("total_tva");
                BigDecimal ttc = rs.getBigDecimal("total_ttc");

                results.add(new InvoiceTaxSummary(id, ht, tva, ttc));
            }

            return results;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnection.close(connection);
        }
    }

    public BigDecimal computeWeightedTurnoverTtc() {
        String sql = """
            SELECT 
                ROUND(SUM(
                    CASE\s
                        WHEN i.status = 'PAID' THEN (il.quantity * il.unit_price) * (1 + t.rate / 100) * 1.0
                        WHEN i.status = 'CONFIRMED' THEN (il.quantity * il.unit_price) * (1 + t.rate / 100) * 0.5
                        WHEN i.status = 'DRAFT' THEN 0
                    END
                ), 2) AS weighted_ttc
            FROM invoice i
            JOIN invoice_line il ON i.id = il.invoice_id
            CROSS JOIN tax_config t;
        """;

        Connection connection = dbConnection.getDBConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getBigDecimal("weighted_ttc");
            }
            return BigDecimal.ZERO;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnection.close(connection);
        }
    }


}