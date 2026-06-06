package ui;

import domain.*;
import service.PedidoService;
import dao.*;
import dao.impl.*;
import worker.OrdersWorker;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import dao.OrderDAO;

public class MainMenu {
  public static void main(String[] args) {
    CustomerDAO customerDAO = new dao.impl.CustomerDAOImpl();
    ProductDAO productDAO = new dao.impl.ProductDAOImpl();
    OrderDAO orderDAO = new dao.impl.OrderDAOImpl();
    PedidoService pedidoService = new PedidoService(productDAO, customerDAO, orderDAO);

    OrdersWorker worker = new OrdersWorker();
    Thread workerThread = new Thread(worker, "OrdersWorker");
    workerThread.start();

    Scanner sc = new Scanner(System.in);
    boolean sair = false;
    while (!sair) {
      System.out.println("=== Sistema de Pedidos ===");
      System.out.println("1 - Cadastrar Cliente");
      System.out.println("2 - Cadastrar Produto");
      System.out.println("3 - Criar Pedido");
      System.out.println("4 - Listar Clientes");
      System.out.println("5 - Listar Produtos");
      System.out.println("6 - Encerrar");
      System.out.print("Escolha uma opção: ");

      int opc = Integer.parseInt(sc.nextLine().trim());
      switch (opc) {
        case 1:
          System.out.print("Nome: ");
          String name = sc.nextLine();
          System.out.print("Email: ");
          String email = sc.nextLine();
          Customer c = new Customer(name, email);
          try {
            customerDAO.create(c);
            System.out.println("Cliente cadastrado.");
          } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
          }
          break;
        case 2:
          System.out.print("Nome: ");
          String pname = sc.nextLine();
          System.out.print("Preço (ex: 9.99): ");
          BigDecimal price = new BigDecimal(sc.nextLine());
          System.out.print("Estoque: ");
          int stock = Integer.parseInt(sc.nextLine());
          System.out.print("Categoria (ALIMENTOS/ELETRONICOS/LIVROS): ");
          ProductCategory cat = ProductCategory.valueOf(sc.nextLine().toUpperCase());
          Product p = new Product(pname, price, stock, cat);
          try {
            productDAO.create(p);
            System.out.println("Produto cadastrado.");
          } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
          }
          break;
        case 3:
          System.out.print("Id do Cliente: ");
          int cid = Integer.parseInt(sc.nextLine());
          System.out.println("NOTA: Em uma versão completa, aqui você criaria itens e chamaria pedidoService.createOrder.");
          break;
        case 4:
          System.out.println("Listagem de clientes não implementada neste commit UI.");
          break;
        case 6:
          sair = true;
          break;
        default:
          System.out.println("Opção inválida.");
      }
    }

    worker.stop();
    try {
      workerThread.join();
    } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    sc.close();
  }
}