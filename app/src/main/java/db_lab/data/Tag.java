package db_lab.data;

import java.sql.Connection;
import java.util.HashSet;
import java.util.List;
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

    @Override
    public String toString() {
        return Printer.stringify("Tag", List.of(Printer.field("name", this.name)));
    }

    public final class DAO {

        public static Set<Tag> ofProduct(Connection connection, int productId) {
            // Iterating through a resultSet:
            // https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html
            throw new UnsupportedOperationException("unimplemented");
        }
    }
}
