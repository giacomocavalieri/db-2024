package db_lab.data;

import java.util.List;
import java.util.stream.Collectors;

public final class Printer {

    public static class Field {

        public final String name;
        public final Object value;

        public Field(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String toString() {
            if (this.value instanceof String) {
                return this.name + "='" + this.value.toString() + "'";
            } else {
                return this.name + "=" + this.value.toString();
            }
        }
    }

    public static Field field(String name, Object value) {
        return new Field(name, value);
    }

    // This is just a helper function to have a decent looking toString.
    public static String stringify(String name, List<Field> fields) {
        var builder = new StringBuilder(name);
        var fieldsString = fields.stream().map(field -> field.toString()).collect(Collectors.joining(", "));
        builder.append("[");
        builder.append(fieldsString);
        builder.append("]");
        return builder.toString();
    }
}
