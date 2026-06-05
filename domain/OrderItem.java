package domain;

import java.math.BigDecimal;

public final class OrderItem {
  private final Product product;
  private final int quantity;
  private final BigDecimal priceAtOrder;

  public OrderItem(Product product, int quantity, BigDecimal priceAtOrder) {
    this.product = product;
    this.quantity = quantity;
    this.priceAtOrder = priceAtOrder;
  }

  public Product getProduct() { return product; }
  public int getQuantity() { return quantity; }
  public BigDecimal getPriceAtOrder() { return priceAtOrder; }
}