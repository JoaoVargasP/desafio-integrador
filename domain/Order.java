package domain;

import java.time.LocalDateTime;
import java.util.List;

public final class Order {
  private final int id;
  private final Customer customer;
  private final OrderStatus status;
  private final LocalDateTime createdAt;
  private final List<OrderItem> items;

  public Order(int id, Customer customer, OrderStatus status, LocalDateTime createdAt, List<OrderItem> items) {
    this.id = id;
    this.customer = customer;
    this.status = status;
    this.createdAt = createdAt;
    this.items = items;
  }

  public Order(Customer customer, List<OrderItem> items) {
    this(0, customer, OrderStatus.ABERTO, null, items);
  }

  public int getId() { return id; }
  public Customer getCustomer() { return customer; }
  public OrderStatus getStatus() { return status; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public List<OrderItem> getItems() { return items; }
}