package reports;

import java.math.BigDecimal;

public class ReportRow {
  private final String label;
  private final int quantity;
  private final BigDecimal value;

  public ReportRow(String label, int quantity, BigDecimal value) {
    this.label = label;
    this.quantity = quantity;
    this.value = value;
  }

  public String getLabel() { return label; }
  public int getQuantity() { return quantity; }
  public BigDecimal getValue() { return value; }
}