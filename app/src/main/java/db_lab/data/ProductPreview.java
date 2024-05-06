package db_lab.data;

import db_lab.data.DAOException;
import db_lab.data.DAOUtils;
import db_lab.data.Queries;
import db_lab.data.Tag;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ProductPreview {

    public final int code;
    public final String name;
    public final List<Tag> tags;

    public ProductPreview(int code, String name, List<Tag> tags) {
        this.code = code;
        this.name = name == null ? "" : name;
        this.tags = tags == null ? List.of() : Collections.unmodifiableList(new ArrayList<>(tags));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other instanceof ProductPreview) {
            var p = (ProductPreview) other;
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

        public static final List<ProductPreview> list(Connection connection) {
            var previews = new ArrayList<ProductPreview>();

            try (
                var statement = DAOUtils.prepare(connection, Queries.LIST_PRODUCTS);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var code = resultSet.getInt("p.codProdotto");
                    var name = resultSet.getString("p.nome");

                    // Notice how, for each product we have to run another query to
                    // get all of its tags. We've already implemented the query we
                    // need in the `Tag.DAO`!
                    var tags = Tag.DAO.listForProduct(connection, code);
                    var preview = new ProductPreview(code, name, tags);
                    previews.add(preview);
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }

            return previews;
        }
    }
}
