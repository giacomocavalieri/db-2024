package db_lab.model;

import db_lab.data.Product;
import db_lab.data.ProductPreview;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface Model {
    public Optional<Product> find(int productCode);

    public List<ProductPreview> previews();

    public boolean loadedPreviews();

    public List<ProductPreview> loadPreviews();

    // Create a mocked version of the model.
    //
    public static Model mock() {
        return new MockedModel();
    }

    // Create a model that connects to a database using the given connection.
    //
    public static Model fromConnection(Connection connection) {
        return new DBModel(connection);
    }
}
