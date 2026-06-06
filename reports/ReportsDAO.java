package reports;

import java.util.List;
import java.math.BigDecimal;

public interface ReportsDAO {
  List<ReportRow> salesByProduct() throws java.sql.SQLException;
  List<ReportRow> revenueByCustomer() throws java.sql.SQLException;
}