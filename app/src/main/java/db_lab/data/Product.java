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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class Product {

    public final int code;
    public final String name;
    public final String description;
    public final Map<Material, Float> composition;

    public Product(int code, String name, String description, Map<Material, Float> composition) {
        this.code = code;
        this.name = name;
        this.description = description == null ? "" : description;
        this.composition = composition == null ? Map.of() : Collections.unmodifiableMap(new HashMap<>(composition));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else return switch (other) {
            case Product p -> p.code == this.code;
            default -> false;
        };
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.code);
    }

    public final class DAO {

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
