package sql;

import lombok.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.*;


public class Sql {
    public Sql() {
    }


    private static final String url = System.getProperty("db.url");
    private static final String user = System.getProperty("db.user");
    private static final String pass = System.getProperty("db.password");


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class StatusResponse {
        private String status;
    }

    // Проверка статуса последней записи покупки по карте
    @SneakyThrows
    public static String checkStatus() {
        var runner = new QueryRunner();
        var status = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        try (var conn = DriverManager.getConnection(url, user, pass)) {
            var statusCheck = runner.query(conn, status, new BeanHandler<>(StatusResponse.class));
            return statusCheck.getStatus();
        }
    }

    // Проверка статуса последней записи покупки в кредит
    @SneakyThrows
    public static String checkStatusCredit() {
        var runner = new QueryRunner();
        var status = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        try (var conn = DriverManager.getConnection(url, user, pass)) {
            var statusCheck = runner.query(conn, status, new BeanHandler<>(StatusResponse.class));
            return statusCheck.getStatus();
        }
    }

      @SneakyThrows
    public static long getNumberOfRawsFromOrderEntity() {
        QueryRunner runner = new QueryRunner();
        String dataSQL = "SELECT COUNT(id) FROM order_entity";
        long numberOfRaws;
        try (
                Connection connection = DriverManager.getConnection(
                        url, user, pass
                )
        ) {
            numberOfRaws = runner.query(connection, dataSQL, new ScalarHandler<>());
        }
        return numberOfRaws;
    }

       @SneakyThrows
    public static void deleteAllStringsForPaymentEntity() {
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            conn.createStatement().executeUpdate("DELETE FROM payment_entity");
        }
    }

    @SneakyThrows
    public static void deleteAllStringsForCreditRequestEntity() {
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            conn.createStatement().executeUpdate("DELETE FROM credit_request_entity");
        }
    }
}


