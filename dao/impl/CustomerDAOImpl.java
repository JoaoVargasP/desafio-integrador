package dao.impl;

import dao.CustomerDAO;
import domain.Customer;
import util.DBUtil;

import java.sql.*;
import java.util.Optional;

public class CustomerDAOImpl implements CustomerDAO {

  @Override
  public void create(Customer customer) throws SQLException {
    String sql = "INSERT INTO customers (name, email) VALUES (?, ?)";
    try (Connection conn = DBUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, customer.getName());
      ps.setString(2, customer.getEmail());
      ps.executeUpdate();
    }
  }

  @Override
  public Optional<Customer> findById(int id) throws SQLException {
    String sql = "SELECT id, name, email FROM customers WHERE id = ?";
    try (Connection conn = DBUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(new Customer(rs.getInt("id"), rs.getString("name"), rs.getString("email")));
        }
      }
    }
    return Optional.empty();
  }

  @Override
  public Optional<Customer> findByEmail(String email) throws SQLException {
    String sql = "SELECT id, name, email FROM customers WHERE email = ?";
    try (Connection conn = DBUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(new Customer(rs.getInt("id"), rs.getString("name"), rs.getString("email")));
        }
      }
    }
    return Optional.empty();
  }
}