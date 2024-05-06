package db_lab.data;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

public final class Tag {

    public final String name;

    public Tag(String name) {
        this.name = name == null ? "" : name;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other instanceof Tag) {
            var t = (Tag) other;
            return t.name.equals(this.name);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    public final class DAO {

        public static Set<Tag> ofProduct(Connection connection, int productId) {
            var tags = new HashSet<Tag>();

            try (
                // 1. Prepare the query you want to run
                var statement = DAOUtils.prepare(connection, Queries.TAGS_FOR_PRODUCT, productId);
                // 2. Run the query and get back a `ResultSet`
                var resultSet = statement.executeQuery();
            ) {
                // 3. Accumulate all the values of the result set into a list of tags
                // iterating over it, row by row, until we run out of results.
                while (resultSet.next()) {
                    // 3-a. For each row we read the data we need and create a tag
                    // out of it.
                    var tagName = resultSet.getString("tag_name");
                    var tag = new Tag(tagName);
                    tags.add(tag);
                }
            } catch (Exception e) {
                // Note how we wrap all checked `SQLException`s in `DAOException`s so
                // that they do not bubble up all the function calls.
                throw new DAOException(e);
            }

            return tags;
        }
    }
}
