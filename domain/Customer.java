package domain;

public final class Customer {
  private final int id;
  private final String name;
  private final String email;

  public Customer(int id, String name, String email) {
    this.id = id;
    this.name = name;
    this.email = email;
  }

  public Customer(String name, String email) {
    this(0, name, email);
  }

  public int getId() { return id; }
  public String getName() { return name; }
  public String getEmail() { return email; }
}