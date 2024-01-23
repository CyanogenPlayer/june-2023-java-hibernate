package jdbc;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String url = "jdbc:postgresql://localhost:54320/postgres";
        String user = "postgres";
        String password = "postgres";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            boolean isConnected = connection.isValid(5);
            System.out.println(isConnected);

            Statement statement = connection.createStatement();
            statement.execute("create table if not exists products (id bigint, name varchar(255))");

//            statement.execute("insert into products (id, name) values (1, 'test')");
            executeQuerySafe(connection);

            ResultSet resultSet  = statement.executeQuery("select * from products");

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString(2);

                System.out.println("id: " + id + " name: " + name);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void executeQuerySafe(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into products (id, name) values (?, ?)")) {
            preparedStatement.setLong(1, 100);
            preparedStatement.setString(2, "prod");

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
