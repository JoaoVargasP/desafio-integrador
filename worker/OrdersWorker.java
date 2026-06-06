package worker;

import util.DBUtil;
import domain.OrderStatus;
import java.sql.*;

public class OrdersWorker implements Runnable {
  private volatile boolean running = true;

  @Override
  public void run() {
    while (running) {
      try (Connection conn = DBUtil.getConnection()) {
        conn.setAutoCommit(false);

        String selectSql = "SELECT o.id FROM orders o WHERE o.status = ? ORDER BY o.created_at ASC LIMIT 1 FOR UPDATE";
        int orderId = -1;
        try (PreparedStatement psSelect = conn.prepareStatement(selectSql)) {
          psSelect.setString(1, OrderStatus.FILA.name());
          try (ResultSet rs = psSelect.executeQuery()) {
            if (rs.next()) {
              orderId = rs.getInt("id");
            }
          }
        }

        if (orderId != -1) {
          String updateSql = "UPDATE orders SET status = ? WHERE id = ? AND status = ?";
          try (PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {
            psUpdate.setString(1, OrderStatus.PROCESSANDO.name());
            psUpdate.setInt(2, orderId);
            psUpdate.setString(3, OrderStatus.FILA.name());
            int updated = psUpdate.executeUpdate();
            if (updated == 1) {
              conn.commit();

              Thread.sleep(1500);

              String finalizeSql = "UPDATE orders SET status = ? WHERE id = ?";
              try (PreparedStatement psFinal = conn.prepareStatement(finalizeSql)) {
                psFinal.setString(1, OrderStatus.FINALIZADO.name());
                psFinal.setInt(2, orderId);
                psFinal.executeUpdate();
              }
              conn.commit();
            } else {
              conn.rollback();
            }
          }
        } else {
          conn.rollback();
        }
      } catch (Exception e) {
        System.err.println("Worker erro: " + e.getMessage());
        try { Thread.sleep(2000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
      }

      try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
  }

  public void stop() { this.running = false; }
}