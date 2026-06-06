# Pedido CLI - Java + MySQL

Requisitos
- Java 17
- MySQL 8+
- JDBC (sem ORM)

Como rodar
1) Ajuste as credenciais em util/DBUtil.java
2) mvn clean package
3) java -jar target/pedidos-cli-1.0.0.jar

Arquitetura resumida
- Domain: Customer, Product, Order, OrderItem, enums
- DAO: implementação com JDBC puro
- Service: lógica de negócio (pedido, estoque)
- Worker: processamento assíncrono
- UI: console (sem java.sql)