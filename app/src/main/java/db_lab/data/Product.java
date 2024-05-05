package db_lab.data;

import db_lab.data.DAOException;
import db_lab.data.DAOUtils;
import db_lab.data.Queries;
import db_lab.data.Tag;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record Product(int code, String name, String description, Map<Material, Float> composition) {
    public class DAO {

        public static Optional<Product> find(Connection connection, int productId) {
            try (
                var statement = DAOUtils.prepare(connection, Queries.FIND_PRODUCT, productId);
                var resultSet = statement.executeQuery();
            ) {
                if (resultSet.next()) {
                    var code = resultSet.getInt("codice");
                    var name = resultSet.getString("");
                    var description = resultSet.getString("");
                    var composition = Material.DAO.forProduct(connection, productId);
                    var product = new Product(code, name, description, composition);
                    return Optional.of(product);
                } else {
                    return Optional.empty();
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
    }
}
