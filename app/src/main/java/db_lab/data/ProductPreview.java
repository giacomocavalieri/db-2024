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
import java.util.List;
import java.util.Map;

public record ProductPreview(int code, String name, List<Tag> tags) {
    public class DAO {

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
