package dao;

import domain.Product;
import domain.ProductCategory;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.sql.SQLException;

public interface ProductDAO {
  void create(Product product) throws SQLException;
  Optional<Product> findById(int id) throws SQLException;
  List<Product> findAll() throws SQLException;
  void updateStock(int productId, int newStock) throws SQLException;
}