package dao;

import domain.Customer;
import java.sql.SQLException;
import java.util.Optional;

public interface CustomerDAO {
  void create(Customer customer) throws SQLException;
  Optional<Customer> findById(int id) throws SQLException;
  Optional<Customer> findByEmail(String email) throws SQLException;
}