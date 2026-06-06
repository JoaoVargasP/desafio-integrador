package service;

import dao.CustomerDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import domain.*;
import util.DBUtil;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class PedidoService {
  private final ProductDAO productDAO;
  private final CustomerDAO customerDAO;
  private final OrderDAO orderDAO;

  public PedidoService(ProductDAO productDAO, CustomerDAO customerDAO, OrderDAO orderDAO) {
    this.productDAO = productDAO;
    this.customerDAO = customerDAO;
    this.orderDAO = orderDAO;
  }

  public Order createOrder(int customerId, List<OrderItem> items) throws SQLException {
    Optional<Customer> cOpt = customerDAO.findById(customerId);
    if (!cOpt.isPresent()) {
      throw new IllegalArgumentException("Cliente não encontrado");
    }
    Customer customer = cOpt.get();

    String sqlInsertOrder = "INSERT INTO orders (customer_id, status, created_at) VALUES (?, ?, ?)";
    String sqlInsertItem = "INSERT INTO order_items (order_id, product_id, quantity, price_at_order) VALUES (?, ?, ?, ?)";
    String sqlStock = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";

    try (Connection conn = DBUtil.getConnection()) {
      conn.setAutoCommit(false);
      int orderId;

      try (PreparedStatement psOrder = conn.prepareStatement(sqlInsertOrder, Statement.RETURN_GENERATED_KEYS)) {
        psOrder.setInt(1, customer.getId());
        psOrder.setString(2, OrderStatus.FILA.name()); // salvo com FILA para processamento
        psOrder.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
        psOrder.executeUpdate();
        try (ResultSet rs = psOrder.getGeneratedKeys()) {
          if (rs.next()) orderId = rs.getInt(1);
          else throw new SQLException("Falha ao obter id do pedido");
        }
      }

      try (PreparedStatement psStock = conn.prepareStatement(sqlStock);
           PreparedStatement psItem = conn.prepareStatement(sqlInsertItem)) {
        for (OrderItem item : items) {
          int productId = item.getProduct().getId();
          int qty = item.getQuantity();

          psStock.setInt(1, qty);
          psStock.setInt(2, productId);
          psStock.setInt(3, qty);
          int updated = psStock.executeUpdate();
          if (updated == 0) {
            conn.rollback();
            throw new InsufficientStockException("Estoque insuficiente para: " + item.getProduct().getName());
          }

          // itens do pedido
          psItem.setInt(1, orderId);
          psItem.setInt(2, productId);
          psItem.setInt(3, qty);
          psItem.setBigDecimal(4, item.getPriceAtOrder());
          psItem.addBatch();
        }
        psItem.executeBatch();
      }

      conn.commit();

      Order persisted = new Order(orderId, customer, OrderStatus.FILA, LocalDateTime.now(), items);
      return persisted;

    } catch (SQLException ex) {
      throw ex;
    }
  }
}