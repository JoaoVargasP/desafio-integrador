package domain;

import java.math.BigDecimal;

public final class Product {
  private final int id;
  private final String name;
  private final BigDecimal price;
  private final int stock;
  private final ProductCategory category;

  public Product(int id, String name, BigDecimal price, int stock, ProductCategory category) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.stock = stock;
    this.category = category;
  }

  // usado ao cadastrar antes do insert
  public Product(String name, BigDecimal price, int stock, ProductCategory category) {
    this(0, name, price, stock, category);
  }

  public int getId() { return id; }
  public String getName() { return name; }
  public BigDecimal getPrice() { return price; }
  public int getStock() { return stock; }
  public ProductCategory getCategory() { return category; }
}