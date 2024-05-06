package db_lab.data;

import db_lab.data.DAOException;
import db_lab.data.DAOUtils;
import db_lab.data.Queries;
import java.sql.Connection;
import java.util.Collections;
import java.util.HashMap;
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
        } else if (other == null) {
            return false;
        } else if (other instanceof Product) {
            var p = (Product) other;
            return p.code == this.code;
        } else {
            return false;
        }
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
                    var code = resultSet.getInt("p.codice");
                    var name = resultSet.getString("p.nome");
                    var description = resultSet.getString("p.descrizione");
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
