package dao.impl;

import dao.ProductDAO;
import domain.Product;
import domain.ProductCategory;
import util.DBUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAOImpl implements ProductDAO {
  @Override
  public void create(Product product) throws SQLException {
    String sql = "INSERT INTO products (name, price, stock, category) VALUES (?, ?, ?, ?)";
    try (Connection conn = DBUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, product.getName());
      ps.setBigDecimal(2, product.getPrice());
      ps.setInt(3, product.getStock());
      ps.setString(4, product.getCategory().name());
      ps.executeUpdate();
    }
  }

  @Override
  public Optional<Product> findById(int id) throws SQLException {
    String sql = "SELECT id, name, price, stock, category FROM products WHERE id = ?";
    try (Connection conn = DBUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          Product p = new Product(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getBigDecimal("price"),
            rs.getInt("stock"),
            ProductCategory.valueOf(rs.getString("category"))
          );
          return Optional.of(p);
        }
      }
    }
    return Optional.empty();
  }

  @Override
  public List<Product> findAll() throws SQLException {
    String sql = "SELECT id, name, price, stock, category FROM products";
    List<Product> list = new ArrayList<>();
    try (Connection conn = DBUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        list.add(new Product(
          rs.getInt("id"),
          rs.getString("name"),
          rs.getBigDecimal("price"),
          rs.getInt("stock"),
          ProductCategory.valueOf(rs.getString("category"))
        ));
      }
    }
    return list;
  }

  @Override
  public void updateStock(int productId, int newStock) throws SQLException {
    String sql = "UPDATE products SET stock = ? WHERE id = ?";
    try (Connection conn = DBUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, newStock);
      ps.setInt(2, productId);
      ps.executeUpdate();
    }
  }
}