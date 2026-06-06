package dao.impl;

import reports.ReportRow;
import reports.ReportsDAO;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportsDAOImpl implements ReportsDAO {

  @Override
  public List<ReportRow> salesByProduct() throws SQLException {
    String sql = "SELECT p.name AS label, SUM(oi.quantity) AS quantity, SUM(oi.quantity * oi.price_at_order) AS value " +
                 "FROM order_items oi JOIN products p ON oi.product_id = p.id " +
                 "JOIN orders o ON oi.order_id = o.id " +
                 "WHERE o.status = 'FINALIZADO' " +
                 "GROUP BY p.name ORDER BY value DESC";
    List<ReportRow> rows = new ArrayList<>();
    try (Connection conn = util.DBUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        rows.add(new ReportRow(rs.getString("label"), rs.getInt("quantity"),
                rs.getBigDecimal("value")));
      }
    }
    return rows;
  }

  @Override
  public List<ReportRow> revenueByCustomer() throws SQLException {
    String sql = "SELECT c.name AS label, SUM(oi.quantity * oi.price_at_order) AS value " +
                 "FROM orders o JOIN customers c ON o.customer_id = c.id " +
                 "JOIN order_items oi ON oi.order_id = o.id " +
                 "WHERE o.status = 'FINALIZADO' " +
                 "GROUP BY c.id, c.name ORDER BY value DESC";
    List<ReportRow> rows = new ArrayList<>();
    try (Connection conn = util.DBUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        rows.add(new ReportRow(rs.getString("label"), 0, rs.getBigDecimal("value")));
      }
    }
    return rows;
  }
}