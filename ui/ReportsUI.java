package ui;

import reports.ReportsDAO;
import dao.impl.ReportsDAOImpl;
import reports.ReportRow;

import java.util.List;

public class ReportsUI {
  public static void main(String[] args) throws Exception {
    ReportsDAO dao = new ReportsDAOImpl();
    List<ReportRow> salesByProduct = dao.salesByProduct();
    System.out.println("Vendas por produto:");
    for (ReportRow r : salesByProduct) {
      System.out.println(r.getLabel() + " | Qtd: " + r.getQuantity() + " | Valor: " + r.getValue());
    }

    List<ReportRow> revenueByCustomer = dao.revenueByCustomer();
    System.out.println("Faturamento por cliente:");
    for (ReportRow r : revenueByCustomer) {
      System.out.println(r.getLabel() + " | Faturamento: " + r.getValue());
    }
  }
}