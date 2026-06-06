package service;

import dao.CustomerDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import domain.*;
import java.util.List;
import java.util.Optional;

import java.sql.SQLException;

public class PedidoService {
  private final ProductDAO productDAO;
  private final CustomerDAO customerDAO;
  private final OrderDAO orderDAO;

  public PedidoService(ProductDAO productDAO, CustomerDAO customerDAO, OrderDAO orderDAO) {
    this.productDAO = productDAO;
    this.customerDAO = customerDAO;
    this.orderDAO = orderDAO;
  }

  // Criação de pedido com validação de estoque (sem atualização de estoque neste commit)
  public Order createOrder(int customerId, List<OrderItem> items) throws SQLException {
    Optional<Customer> cOpt = customerDAO.findById(customerId);
    if (!cOpt.isPresent()) {
      throw new IllegalArgumentException("Cliente não encontrado");
    }
    // Validação simples de estoque
    for (OrderItem item : items) {
      int productId = item.getProduct().getId();
      Optional<Product> pOpt = productDAO.findById(productId);
      if (!pOpt.isPresent()) {
        throw new IllegalArgumentException("Produto não encontrado: " + productId);
      }
      Product p = pOpt.get();
      if (p.getStock() < item.getQuantity()) {
        throw new InsufficientStockException("Estoque insuficiente para: " + p.getName());
      }
    }

    Customer customer = cOpt.get();
    Order newOrder = new Order(customer, items); // status inicial ABERTO
    orderDAO.create(newOrder);
    return newOrder;
  }
}