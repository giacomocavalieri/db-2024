package db_lab.model;

import db_lab.data.Product;
import db_lab.data.ProductPreview;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// This is the real model implementation that uses the DAOs we've defined to
// actually load data from the underlying database.
//
// As you can see this model doesn't do too much except loading data from the
// database and keeping a cache of the loaded previews.
// A real model might be doing much more, but for the sake of the example we're
// keeping it simple.
//
public final class DBModel implements Model {

    private final Connection connection;
    private Optional<List<ProductPreview>> previews;

    public DBModel(Connection connection) {
        Objects.requireNonNull(connection, "Model created with null connection");
        this.connection = connection;
        this.previews = Optional.empty();
    }

    @Override
    public Optional<Product> find(int productCode) {
        return Product.DAO.find(connection, productCode);
    }

    @Override
    public List<ProductPreview> previews() {
        return this.previews.orElse(List.of());
    }

    @Override
    public boolean loadedPreviews() {
        return this.previews.isPresent();
    }

    @Override
    public List<ProductPreview> loadPreviews() {
        var previews = ProductPreview.DAO.list(this.connection);
        this.previews = Optional.of(previews);
        return previews;
    }
}
