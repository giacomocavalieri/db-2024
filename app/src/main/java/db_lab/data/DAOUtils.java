package db_lab.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class DAOUtils {

    // Establishes a connection to a MySQL daemon running locally at port 3306.
    //
    public static Connection localMySQLConnection(String database, String username, String password) {
        try {
            var host = "localhost";
            var port = "3306";
            var connectionString = "jdbc:mysql://" + host + ":" + port + "/" + database;
            return DriverManager.getConnection(connectionString, username, password);
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    // We must always prepare a statement to make sure we do not fall victim to SQL injection:
    // https://owasp.org/www-community/attacks/SQL_Injection
    //
    // This is a helper that prepares the statement with all the values we give it:
    //
    //     prepare(connection, MY_QUERY, query_arg1, query_arg2, ...)
    //
    public static PreparedStatement prepare(Connection connection, String query, Object... values) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(query);
            for (int i = 0; i < values.length; i++) {
                statement.setObject(i + 1, values[i]);
            }
            return statement;
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw e;
        }
    }
}
